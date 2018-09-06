package com.dmall.plat.product.controller;


import com.dmall.common.entity.Tree;
import com.dmall.common.enums.ResultEnum;
import com.dmall.product.entity.ProductType;
import com.dmall.product.service.ProductTypeService;
import com.dmall.web.common.result.ReturnResult;
import com.dmall.web.common.utils.ResultUtil;
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
@RequestMapping("/type")
public class ProductTypeController {

    @Autowired
    private ProductTypeService productTypeService;

    @RequestMapping("tree")
    @ResponseBody
    public ReturnResult tree(){
        List<ProductType> result=productTypeService.tree(0L);
        return ResultUtil.buildResult(ResultEnum.SUCC,result);
    }
}

