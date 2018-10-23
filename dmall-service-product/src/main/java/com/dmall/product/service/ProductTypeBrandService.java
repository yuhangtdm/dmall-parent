package com.dmall.product.service;

import com.dmall.product.entity.ProductTypeBrand;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  商品分类-品牌关系类
 *  多对多
 * </p>
 *
 * @author yuhang
 * @since 2018-09-12
 */
public interface ProductTypeBrandService extends IService<ProductTypeBrand> {

    /**
     * 根据品牌id删除
     */
    void deleteByBrandId(Long id);

    /**
     * 根据商品分类删除
     */
    void deleteByProductType(Long id);

    /**
     * 根据品牌id查询
     */
    List<ProductTypeBrand> queryByBrandId(Long brandId);

    /**
     * 根据商品分类查询
     */
    List<ProductTypeBrand> queryByProductTypeid(Long productTypeId);

    /**
     * 批量插入
     */
    void batchInsert(Long brandId,List<Long> typeIds);

    /**
     * 批量插入
     */
    void batchInsert(List<Long> brandIds,Long typeId);


    /**
     * 删除品牌的类型
     */
    void deleteByTypeIds(Long brandId,List<Long> typeIds);

    /**
     * 删除类型的品牌
     */
    void deleteByBrandId(List<Long> delBrandIds,Long typeId);


}
