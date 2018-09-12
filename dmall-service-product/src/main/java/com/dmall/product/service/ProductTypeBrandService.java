package com.dmall.product.service;

import com.dmall.product.entity.ProductTypeBrand;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-09-12
 */
public interface ProductTypeBrandService extends IService<ProductTypeBrand> {

    void deleteByBrandId(Long id);

    List<ProductTypeBrand> queryByBrandId(Long brandId);

    List<ProductTypeBrand> queryByProductTypeid(Long productTypeId);
}
