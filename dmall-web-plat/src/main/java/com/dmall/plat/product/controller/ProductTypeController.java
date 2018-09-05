package com.dmall.plat.product.controller;


import com.dmall.common.entity.Tree;
import com.dmall.product.service.ProductTypeService;
import com.dmall.web.common.result.ReturnResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 * 商品类型 前端控制器
 * </p>
 *
 * @author yuhang
 * @since 2018-08-25
 */
@Controller
@RequestMapping("/productType")
public class ProductTypeController {

    @Autowired
    private ProductTypeService productTypeService;

    @RequestMapping("tree")
    public ReturnResult tree(){
        List<Tree> result=productTypeService.tree();
        return null;
    }
}

