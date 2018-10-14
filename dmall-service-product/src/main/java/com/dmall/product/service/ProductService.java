package com.dmall.product.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.product.entity.Product;
import com.baomidou.mybatisplus.service.IService;
import com.dmall.product.entity.ProductExt;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface ProductService extends IService<Product> {

    /**
     * 商品分页
     */
    Page pageList(Product product, Page page);

    /**
     * 保存完整的商品信息
     * 包括商品基本信息 扩展信息 商品-属性信息
     * @param product
     * @param ext
     * @param propsGroupArray
     */
    void saveFullProduct(Product product, ProductExt ext, JSONArray propsGroupArray, List<String> imgVoArray);

    /**
     * 查询今日最近添加的商品
     * @return
     */
    Product queryToday();

    Product selectByProductCode(String productCode);

}
