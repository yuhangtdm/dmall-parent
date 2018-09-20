package com.dmall.product.service;

import com.dmall.common.entity.Tree;
import com.dmall.product.entity.ProductType;
import com.baomidou.mybatisplus.service.IService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
    List<ProductType> saveOrUpdate(ProductType type);

    /**
     * 获取子代元素
     */
    List<ProductType> getSun(Long pid);


    /**
     * 删除节点以及后代节点
     * @param id
     */
    List<ProductType> batchDelete(Long id);

    /**
     * 设置品牌
     * @param build
     */
    void setBrand(Map<String,List<Long>> build);

    /**
     * 根据分类id查询分类列表
     * @param typeIds
     * @return
     */
    List<ProductType> selectByParam(List<Long> typeIds);

    List<ProductType> ztree(long pid);
}
