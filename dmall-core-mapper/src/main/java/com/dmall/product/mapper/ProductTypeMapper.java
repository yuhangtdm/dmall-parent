package com.dmall.product.mapper;

import com.dmall.product.entity.ProductType;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 商品类型 Mapper 接口
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface ProductTypeMapper extends BaseMapper<ProductType> {

    void updateBatch(List<ProductType> later);
}
