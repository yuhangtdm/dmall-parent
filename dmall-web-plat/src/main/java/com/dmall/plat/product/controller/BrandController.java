package com.dmall.plat.product.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.exception.BusinessException;
import com.dmall.plat.product.dto.BrandDTO;
import com.dmall.product.entity.Brand;
import com.dmall.product.service.BrandService;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.annotation.TransBean;
import com.dmall.web.common.result.ReturnResult;
import com.dmall.web.common.utils.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 品牌表 前端控制器
 * </p>
 *
 * @author yuhang
 * @since 2018-08-31
 */
@Controller
@RequestMapping("brand")
@Validated
public class BrandController {

    @Autowired
    BrandService brandService;

    /**
     *品牌列表
     */
    @RequestMapping("list")
    @ResponseBody
    @TransBean
    public ReturnResult list(Brand brand, Page page){
        page=brandService.pageList(brand,page);
        return ResultUtil.buildResult(ResultEnum.SUCC,page.getTotal(),page.getRecords());
    }

    /**
     * 跳转到品牌编辑页面
     */
    @RequestMapping("edit")
    public String edit(Long id, HttpServletRequest request){
        if(id!=null){
            Brand brand = brandService.selectById(id);
            if(brand==null){
                 throw new BusinessException(ResultEnum.BAD_REQUEST,"该品牌不存在,请刷新列表");
            }
            request.setAttribute("brand",brand);
        }
        return "product/brand/edit";
    }

    /**
     * 保存或更新品牌
     */
    @RequestMapping("save")
    @ResponseBody
    public ReturnResult save(@Validated BrandDTO brandDTO){
        Brand brand=new Brand();
        BeanUtils.copyProperties(brandDTO,brand);
        brandService.saveOrUpdate(brand);
        return ResultUtil.buildResult(ResultEnum.SUCC);
    }

    /**
     * 删除品牌
     */
    @RequestMapping("delete")
    @ResponseBody
    public ReturnResult delete(@NotNull(message = "id不能为空") Long id){
        brandService.deleteById(id);
        return ResultUtil.buildResult(ResultEnum.SUCC);
    }


}

