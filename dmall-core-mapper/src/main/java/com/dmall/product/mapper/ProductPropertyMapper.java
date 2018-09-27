package com.dmall.product.mapper;

import com.dmall.product.entity.ProductProperty;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品属性集 Mapper 接口
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface ProductPropertyMapper extends BaseMapper<ProductProperty> {

    List<Map<String,Object>> queryByParam(Long productId);
}
