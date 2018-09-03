package com.dmall.product.service;

import com.dmall.product.entity.ProductType;
import com.baomidou.mybatisplus.service.IService;

import java.io.Serializable;

/**
 * <p>
 * 商品类型 服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface ProductTypeService extends IService<ProductType> {

    ProductType selectById(Long id);
}
