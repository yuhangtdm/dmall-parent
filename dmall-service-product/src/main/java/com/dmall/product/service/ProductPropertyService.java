package com.dmall.product.service;

import com.dmall.product.entity.ProductProperty;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品属性集 服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface ProductPropertyService extends IService<ProductProperty> {

    List<ProductProperty> selectByProductId(Long productId);

    List<Map<String,Object>> queryByParam(Long productId);
}
