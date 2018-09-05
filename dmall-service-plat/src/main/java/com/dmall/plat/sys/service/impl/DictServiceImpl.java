package com.dmall.plat.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.plat.sys.service.DictService;
import com.dmall.sys.entity.Dict;
import com.dmall.sys.mapper.DictMapper;
import com.dmall.util.QueryUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字典目录 服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-09-03
 */
@Service
@CacheConfig(cacheNames = "dictCache")
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {


    @Override
    public Page pageList(Dict dict, Page page) {
        EntityWrapper wrapper=new EntityWrapper();
        QueryUtil.queryForm(wrapper,dict);
        wrapper.orderBy("update_time",false);
        page=this.selectPage(page);
        return page;
    }

    /**
     * 先从redis查询 在查数据库
     */
    @Override
    @Cacheable(key = "'dict:'+#root.args[0]")
    public List<Dict> queryDictByType(String dictType) {
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.eq("dict_type",dictType);
        wrapper.orderBy("sort_index");
        List list = this.selectList(wrapper);
        return  list;
    }

    /**
     * 保存或更新数据字典
     * 保存后刷新redis
     * 字典code要唯一
     * 新增的时候根据code不能查到一条记录
     * 更新的时候根据code只能查到本身这条记录
     */
    @Override
    @CachePut(key = "'dict:'+#root.args[0].dictType")
    public List<Dict> saveOrUpdate(Dict dict) {
        if(!validCode(dict)){
            throw new BusinessException(ResultEnum.BAD_REQUEST,"字典码code必须唯一");
        }
        if(dict.getId()!=null){
            this.updateById(dict);
        }else {
            this.insert(dict);
        }
        return queryDictByType(dict.getDictType());
    }

    @Override
    @CachePut(key = "'dict:'+#result[0].dictType")
    public List<Dict> deleteById(Long id) {
        Dict dict = this.selectById(id);
        this.deleteById(id);
        List<Dict> result=this.queryDictByType(dict.getDictType());
        return result;
    }

    /**
     * 校验字典code是否唯一
     */
    private boolean validCode(Dict dict){
        EntityWrapper<Dict> wrapper=new EntityWrapper();
        wrapper.eq("dict_code",dict.getDictCode());
        List<Dict> list = this.selectList(wrapper);
        if(list==null || list.size()==0){
            return true;
        }else {
            if(dict.getId()!=null && dict.getId().equals(list.get(0).getId())){
                return true;
            }
        }
        return false;
    }


}
