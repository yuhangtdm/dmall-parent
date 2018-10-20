package com.dmall.product.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.product.entity.Sku;
import com.baomidou.mybatisplus.service.IService;
import com.dmall.product.entity.SkuProperty;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * SKU 服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface SkuService extends IService<Sku> {

    Page pageList(Sku sku, Page page);

    List<Sku> list(String productCode);

    void saveFullSku(Sku sku, List<String> imgVoArray,List<SkuProperty> skuPropertyList);

    Sku selectBySkuCode(String skuCode);

    Page skuPageList(Map<String,Object> param, Page page);

    void onSale(Long id);

    void offSale(Long id);
}
