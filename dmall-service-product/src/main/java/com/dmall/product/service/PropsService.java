package com.dmall.product.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.product.entity.Props;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 商品属性 服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface PropsService extends IService<Props> {

    
    Page pageList(Props props, Page page);

    void saveOrUpdate(Props props, List<String> propValues);
}
