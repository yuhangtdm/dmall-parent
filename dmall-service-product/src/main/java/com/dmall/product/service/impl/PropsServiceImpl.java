package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.product.entity.Props;
import com.dmall.product.mapper.PropsMapper;
import com.dmall.product.service.PropsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dmall.util.QueryUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品属性 服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Service
public class PropsServiceImpl extends ServiceImpl<PropsMapper, Props> implements PropsService {

    @Override
    public Page pageList(Props props, Page page) {
        EntityWrapper<Props> wrapper=new EntityWrapper<>();
        wrapper.orderBy("update_time",false);
        QueryUtil.queryForm(wrapper,props);
        page = this.selectPage(page,wrapper);
        return page;
    }

    @Override
    public void saveOrUpdate(Props props) {
        if (props.getId()!=null){
            this.updateById(props);
        }else {
            this.insert(props);
        }
    }
}
