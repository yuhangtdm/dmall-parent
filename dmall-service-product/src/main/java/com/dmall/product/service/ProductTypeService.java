package com.dmall.product.service;

import com.dmall.common.entity.Tree;
import com.dmall.product.entity.ProductType;
import com.baomidou.mybatisplus.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品类型 服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface ProductTypeService extends IService<ProductType> {

    ProductType selectById(Long id);

    /**
     * 加载树
     * @return
     */
    List<ProductType> tree(Long pid);

    /**
     * 获取后代元素
     */
    List<ProductType> getLater(Long pid);
}
