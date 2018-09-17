package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.product.entity.PropsGroup;
import com.dmall.product.mapper.PropsGroupMapper;
import com.dmall.product.service.PropsGroupService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dmall.util.QueryUtil;
import com.dmall.util.ValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Page pageList(PropsGroup group, Page page) {
        return page.setRecords(propsGroupMapper.pageList(page,group));
    }

    @Override
    public void saveOrUpdate(PropsGroup group) {
        if(!ValidUtil.valid(group,"propsGroupServiceImpl","groupName","productType")){
            throw new BusinessException(ResultEnum.BAD_REQUEST,"商品类型下的组名称必须唯一");
        }
        if(group.getId()!=null){
            this.updateById(group);
        }else {
            this.insert(group);
        }
    }
}
