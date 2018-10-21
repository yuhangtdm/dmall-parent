package com.dmall.product.service;

import com.dmall.product.entity.SkuMedia;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * sku媒体 服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-10-14
 */
public interface SkuMediaService extends IService<SkuMedia> {

    SkuMedia selectByKey(String key);

    void deleteByKey(String key);

    List<SkuMedia> selectBySkuCode(String skuCode);
}
