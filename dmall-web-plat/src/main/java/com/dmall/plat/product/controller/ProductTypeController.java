package com.dmall.plat.product.controller;


import com.dmall.common.entity.Tree;
import com.dmall.common.enums.ResultEnum;
import com.dmall.plat.product.dto.ProductTypeDto;
import com.dmall.plat.product.dto.TypeBrandDTO;
import com.dmall.product.entity.ProductType;
import com.dmall.product.entity.ProductTypeBrand;
import com.dmall.product.service.ProductTypeBrandService;
import com.dmall.product.service.ProductTypeService;
import com.dmall.web.common.result.ReturnResult;
import com.dmall.web.common.utils.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private ProductTypeBrandService productTypeBrandService;

    /**
     * 商品分类的树数据
     */
    @RequestMapping("tree")
    @ResponseBody
    public ReturnResult tree(Integer level,String flag){
        List<ProductType> result=productTypeService.tree(0L,level,flag);
        return ResultUtil.buildResult(ResultEnum.SUCC,"success",result);
    }

    @RequestMapping("ztree")
    @ResponseBody
    public List<ProductType> ztree(){
        List<ProductType> result=productTypeService.ztree(0L);
        return result;
    }

    /**
     * 商品分类保存
     */
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

    /**
     * 商品分类删除
     */
    @RequestMapping("delete")
    @ResponseBody
    public ReturnResult delete(@NotNull(message = "id不能为空") Long id){
        //todo 类型下维护了商品 维护了属性 不可删除
        productTypeService.batchDelete(id);
        return ResultUtil.buildResult(ResultEnum.SUCC);
    }

    /**
     * 前往设置品牌编辑页面
     */
    @GetMapping("/setBrand")
    public String setBrand(Long typeId, HttpServletRequest request){
        List<ProductTypeBrand> productTypeBrands = productTypeBrandService.queryByProductTypeid(typeId);
        List<Long> collect = productTypeBrands.stream().map(ProductTypeBrand::getBrandId).collect(Collectors.toList());
        Map<String,Object> bean=new HashMap<>();
        bean.put("brandIds",collect);
        request.setAttribute("bean",bean);
        ProductType productType = productTypeService.selectById(typeId);
        request.setAttribute("typeName",productType.getName());
        return "commodity/type/setBrand";
    }

    /**
     * 设置品牌
     */
    @PostMapping("setBrand")
    @ResponseBody
    public ReturnResult setBrand(@Valid TypeBrandDTO typeBrandDTO){
        productTypeService.setBrand(build(typeBrandDTO));
        return ResultUtil.buildResult(ResultEnum.SUCC);
    }

    private Map<String,List<Long>> build(TypeBrandDTO typeBrandDTO){
        Map<String,List<Long>> build=new HashMap<>();
        List<Long> brandIds=new ArrayList<>();
        for (String s : typeBrandDTO.getBrandIds().split(",")) {
            brandIds.add(Long.parseLong(s));
        }
        build.put("brandIds",brandIds);

        List<Long> list=new ArrayList<>();
        list.add(Long.parseLong(typeBrandDTO.getTypeIds()));
        build.put("typeIds",list);
        return build;
    }

}

