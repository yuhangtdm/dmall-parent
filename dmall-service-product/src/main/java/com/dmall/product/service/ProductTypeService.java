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


    /**
     * 加载树
     * @return
     */
    List<ProductType> tree(Long pid,Integer level,String flag);

    /**
     * 获取后代元素
     */
    List<ProductType> getLater(Long pid);

    /**
     * 更新或保存
     */
    void saveOrUpdate(ProductType type);

    /**
     * 获取子代元素
     */
    List<ProductType> getSun(Long pid);

    /**
     * 修改数据
     * @param pid
     */
    void updateSort(Long pid);

    /**
     * 删除节点以及后代节点
     * @param id
     */
    void batchDelete(Long id);
}
