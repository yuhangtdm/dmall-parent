package com.dmall.product.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.product.entity.Brand;
import com.baomidou.mybatisplus.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 品牌表 服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-31
 */
public interface BrandService extends IService<Brand> {

    /**
     * 品牌列表 分页
     */
    Page pageList(Brand brand, Page page);

    /**
     * 新增或更新品牌
     * @param brand
     */
    List<Brand> saveOrUpdate(Brand brand);

    /**
     * 获取所有品牌 加入redis缓存
     * @return
     */
    List<Brand> list();

    /**
     * 根据商品分类获取品牌
     * @param productType
     * @return
     */
    List<Brand> list(Long productType);

    /**
     * 删除品牌
     * @param id
     */
    List<Brand> delete(Long id);
}
