package com.dmall.plat.product.controller;


import com.dmall.common.entity.Tree;
import com.dmall.common.enums.ResultEnum;
import com.dmall.plat.product.dto.ProductTypeDto;
import com.dmall.product.entity.ProductType;
import com.dmall.product.service.ProductTypeService;
import com.dmall.web.common.result.ReturnResult;
import com.dmall.web.common.utils.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

    /**
     *
     * @param level 添加节点的等级
     * @return
     */
    @RequestMapping("tree")
    @ResponseBody
    public List<ProductType> tree(Integer level,String flag){
        List<ProductType> result=productTypeService.tree(0L,level,flag);
        return result;
    }

    @RequestMapping("save")
    @ResponseBody
    public ReturnResult save(@Valid ProductTypeDto dto){
        ProductType type=new ProductType();
        BeanUtils.copyProperties(dto,type);
        if(type.getPid()==null){
            type.setPid(0L);
        }
        productTypeService.saveOrUpdate(type);
        return ResultUtil.buildResult(ResultEnum.SUCC);
    }

    @RequestMapping("delete")
    @ResponseBody
    public ReturnResult save(@NotNull(message = "id不能为空") Long id){
        productTypeService.batchDelete(id);
        return ResultUtil.buildResult(ResultEnum.SUCC);
    }
}

