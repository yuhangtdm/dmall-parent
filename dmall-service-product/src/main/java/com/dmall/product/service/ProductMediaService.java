package com.dmall.product.service;

import com.dmall.product.entity.ProductExt;
import com.dmall.product.entity.ProductMedia;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 商品媒体 服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface ProductMediaService extends IService<ProductMedia> {

    void deleteByKey(String key);

    ProductMedia selectByKey(String key);

    List<ProductMedia> selectByProductCode(String productCode);
}
