package com.dmall.product.mapper;

import com.dmall.product.entity.SkuProperty;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * SKU属性值 Mapper 接口
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface SkuPropertyMapper extends BaseMapper<SkuProperty> {

    void batchInsert(List<SkuProperty> skuPropertyList);

}
