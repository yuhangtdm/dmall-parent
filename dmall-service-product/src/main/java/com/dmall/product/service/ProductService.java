package com.dmall.product.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.product.entity.Product;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface ProductService extends IService<Product> {

    Page pageList(Product product, Page page);
}
