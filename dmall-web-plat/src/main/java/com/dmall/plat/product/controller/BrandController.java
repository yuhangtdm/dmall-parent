package com.dmall.plat.product.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.enums.ImageTypeEnum;
import com.dmall.common.enums.MediaEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.common.utils.StringUtil;
import com.dmall.plat.product.dto.BrandDTO;
import com.dmall.product.entity.Brand;
import com.dmall.product.entity.ProductTypeBrand;
import com.dmall.product.entity.SkuMedia;
import com.dmall.product.service.BrandService;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.annotation.TransBean;
import com.dmall.product.service.ProductService;
import com.dmall.product.service.ProductTypeBrandService;
import com.dmall.util.ValidUtil;
import com.dmall.web.common.result.ReturnResult;
import com.dmall.web.common.utils.QiniuUtil;
import com.dmall.web.common.utils.ResultUtil;
import com.qiniu.storage.model.DefaultPutRet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 品牌控制器
 * @author yuhang
 * @since 2018-08-31
 */
@Controller
@RequestMapping("brand")
@Validated
public class BrandController {

    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductTypeBrandService productTypeBrandService;
    @Autowired
    private QiniuUtil qiniuUtil;

    /**
     *品牌分页列表
     */
    @RequestMapping("page")
    @ResponseBody
    public ReturnResult page(Brand brand, Page page){
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
            List<ProductTypeBrand> typeBrandList = productTypeBrandService.queryByBrandId(brand.getId());
            List<Long> collect = typeBrandList.stream().map(ProductTypeBrand::getProductTypeId).collect(Collectors.toList());
            String productType = StringUtil.join(collect,",");
            brand.setProductType(productType);
            request.setAttribute("bean",brand);
        }
        return "commodity/brand/edit";
    }

    /**
     * 保存或更新品牌 且维护和商品分类的关系
     */
    @RequestMapping("save")
    @ResponseBody
    public ReturnResult save(@Validated BrandDTO brandDTO){
        Brand brand=new Brand();
        BeanUtils.copyProperties(brandDTO,brand);
        if(!ValidUtil.valid(brand,"brandServiceImpl","name")){
            throw new BusinessException(ResultEnum.BAD_REQUEST,"品牌名称必须唯一");
        }
        brandService.saveOrUpdate(brand);
        return ResultUtil.buildResult(ResultEnum.SUCC);
    }

    /**
     * 删除品牌
     */
    @RequestMapping("delete")
    @ResponseBody
    public ReturnResult delete(@NotNull(message = "id不能为空") Long id){
        brandService.delete(id);
        return ResultUtil.buildResult(ResultEnum.SUCC);
    }

    /**
     * 根据商品分类查询品牌列表
     */
    @RequestMapping("listAll")
    @ResponseBody
    public ReturnResult listAll(Long productTypeId){
        List<Brand> result=null;
        if(productTypeId==null){
            result=brandService.list();
        }else{
            result=brandService.list(productTypeId);
        }
        return ResultUtil.buildResult(ResultEnum.SUCC,result);
    }

    /**
     * 品牌logo上传
     */
    @RequestMapping("/upload")
    @ResponseBody
    public ReturnResult fileUpload(MultipartFile file){
        Map<String,String> result=new HashMap<>();
        try {
            String originalFilename = file.getOriginalFilename();
            String fileType=originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            DefaultPutRet defaultPutRet = qiniuUtil.uploadFile(file.getInputStream(), qiniuUtil.getKey(ImageTypeEnum.BRAND.getCode(),fileType));
            result.put("src",qiniuUtil.getUrl(defaultPutRet.key));
            result.put("key",defaultPutRet.key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultEnum.SERVER_ERROR,"上传品牌logo失败");
        }
        return ResultUtil.buildResult(ResultEnum.SUCC,"上传品牌logo成功",result);
    }

}

