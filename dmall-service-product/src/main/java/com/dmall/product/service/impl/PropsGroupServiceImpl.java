package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.product.entity.PropsGroup;
import com.dmall.product.mapper.PropsGroupMapper;
import com.dmall.product.service.PropsGroupService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dmall.util.QueryUtil;
import org.springframework.stereotype.Service;

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

    @Override
    public Page pageList(PropsGroup group, Page page) {
        EntityWrapper<PropsGroup> wrapper=new EntityWrapper<>();
        wrapper.orderBy("update_time",false);
        QueryUtil.queryForm(wrapper,group);
        page = this.selectPage(page,wrapper);
        return page;
    }
}
