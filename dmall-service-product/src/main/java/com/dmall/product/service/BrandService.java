package com.dmall.product.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.product.entity.Brand;
import com.baomidou.mybatisplus.service.IService;

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
    void saveOrUpdate(Brand brand);
}
