package com.dmall.plat.product.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.annotation.TransBean;
import com.dmall.common.enums.ResultEnum;
import com.dmall.product.entity.Product;
import com.dmall.product.service.ProductService;
import com.dmall.web.common.result.ReturnResult;
import com.dmall.web.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author yuhang
 * @since 2018-08-25
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     *商品列表
     */
    @RequestMapping("page")
    @ResponseBody
    @TransBean
    public ReturnResult page(Product product, Page page){
        page=productService.pageList(product,page);
        return ResultUtil.buildResult(ResultEnum.SUCC,page.getTotal(),page.getRecords());
    }

    /**
     * 跳转到商品编辑页面
     */
    @RequestMapping("edit")
    public String edit(Long id, HttpServletRequest request){
        if(id!=null){

        }
        return "commodity/product/edit";
    }
}

