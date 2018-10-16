package com.dmall.product.service;

import com.dmall.product.entity.SkuProperty;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * SKU属性值 服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface SkuPropertyService extends IService<SkuProperty> {

    void batchInsert(List<SkuProperty> skuPropertyList);
}
