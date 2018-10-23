package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.common.utils.StringUtil;
import com.dmall.product.entity.PropsGroup;
import com.dmall.product.mapper.PropsGroupMapper;
import com.dmall.product.service.PropsGroupService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dmall.product.service.PropsOptionService;
import com.dmall.product.service.PropsService;
import com.dmall.util.QueryUtil;
import com.dmall.util.ValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-09-12
 */
@Service
public class PropsGroupServiceImpl extends ServiceImpl<PropsGroupMapper, PropsGroup> implements PropsGroupService {

    @Autowired
    private PropsGroupMapper propsGroupMapper;

    @Autowired
    private PropsService propsService;

    @Autowired
    private PropsOptionService propsOptionService;

    @Override
    public Page pageList(PropsGroup group, Page page) {
        List<Map<String, Object>> maps = propsGroupMapper.pageList(page, group);
        return page.setRecords(maps);
    }

    @Override
    public void saveOrUpdate(PropsGroup group) {
        if(!ValidUtil.valid(group,"propsGroupServiceImpl","name","productType")){
            throw new BusinessException(ResultEnum.BAD_REQUEST,"商品类型下的组名称必须唯一");
        }
        if(group.getId()!=null){
            this.updateById(group);
        }else {
            this.insert(group);
        }
    }

    @Override
    public List<PropsGroup> listAll(String productTypeId) {
        EntityWrapper<PropsGroup> wrapper=new EntityWrapper();
        if(StringUtil.isNotBlank(productTypeId)){
            wrapper.eq("product_type",productTypeId);
        }
        return this.selectList(wrapper);
    }

    @Override
    public void deleteByProductType(String type) {
        EntityWrapper<PropsGroup> wrapper=new EntityWrapper();
        wrapper.eq("product_type",type);
        this.delete(wrapper);
    }

    @Override
    @Transactional
    public void deleteObj(Long id) {
        propsService.deleteByGroupId(id);
        propsOptionService.deleteByGroupId(id);
        this.deleteById(id);
    }
}
