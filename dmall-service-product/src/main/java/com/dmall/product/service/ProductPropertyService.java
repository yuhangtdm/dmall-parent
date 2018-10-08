package com.dmall.product.service;

import com.dmall.product.entity.ProductProperty;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 商品属性集 服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface ProductPropertyService extends IService<ProductProperty> {

    /**
     * 根据商品编码查询商品属性
     */
    List<ProductProperty> queryByProductCode(String productCode);

    /**
     * 根据商品编码查询数据
     */
    List<Map<String,Object>> queryByParam(String productCode);

    /**
     * 根据商品编码和组id删除
     */
    void deleteByGroupId(String productCode,Set<Long> groupId);

    /**
     * 根据商品编码和属性id删除
     */
    void deleteByPropertyId(String productCode,Set<Long> propertyId);

    /**
     * 根据商品编码和组id查询
     */
    List<ProductProperty> queryByProductCodeAndGroupId(String productCode,Long groupId);

    /**
     * 根据商品编码和属性id查询
     */
    List<ProductProperty> queryByProductCodeAndPropertyId(String productCode,Long propertyId);


}
