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


    void batchInsert(Long brandId,List<Long> typeIds);

    void batchInsert(List<Long> brandIds,Long typeId);

    List<ProductTypeBrand> selectByParam(Long brandId, Long typeId);

    /**
     * 删除品牌的类型
     */
    void deleteByTypeIds(Long brandId,List<Long> typeIds);

    /**
     * 删除类型的品牌
     */
    void deleteByBrandId(List<Long> delBrandIds,Long typeId);
}
