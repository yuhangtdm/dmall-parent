package com.dmall.plat.product.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.Constants;
import com.dmall.common.annotation.TransBean;
import com.dmall.common.enums.ImageTypeEnum;
import com.dmall.common.enums.MediaEnum;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.common.utils.MathUtil;
import com.dmall.common.utils.StringUtil;
import com.dmall.plat.product.dto.FullSkuDTO;
import com.dmall.plat.product.dto.SkuDTO;
import com.dmall.plat.product.dto.SkuPropertyDTO;
import com.dmall.product.entity.*;
import com.dmall.product.service.ProductPropertyService;
import com.dmall.product.service.ProductService;
import com.dmall.product.service.SkuMediaService;
import com.dmall.product.service.SkuService;
import com.dmall.web.common.result.ReturnResult;
import com.dmall.web.common.utils.QiniuUtil;
import com.dmall.web.common.utils.ResultUtil;
import com.qiniu.storage.model.DefaultPutRet;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * SKU 前端控制器
 * </p>
 *
 * @author yuhang
 * @since 2018-08-25
 */
@Controller
@RequestMapping("/sku")
@Validated
public class SkuController {

    @Autowired
    private SkuService skuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SkuMediaService skuMediaService;

    @Autowired
    private ProductPropertyService productPropertyService;

    @Autowired
    private QiniuUtil qiniuUtil;

    /**
     * 跳转到SKU添加页面
     */
    @RequestMapping("edit")
    public String edit(@NotBlank(message = "商品编码不能为空") String productCode, HttpServletRequest request){
        Product product = productService.selectByProductCode(productCode);
        List<Sku> list = skuService.list(productCode);
        Integer sortIndex=1;
        if(StringUtil.isNotEmptyObj(list)){
            Sku sku = list.get(0);
            if(sku!=null && sku.getSortIndex()!=null){
                sortIndex=sku.getSortIndex()+1;
            }
        }
        List<JSONObject> jsonObjects = productPropertyService.selectByProductCode(productCode);
        request.setAttribute("groupPropsArray",jsonObjects);
        request.setAttribute("productCode",productCode);
        request.setAttribute("productType",product.getProductType());
        request.setAttribute("brandId",product.getBrandId());
        request.setAttribute("sortIndex",sortIndex);
        return "commodity/sku/edit";
    }


    /**
     *商品列表
     */
    @RequestMapping("page")
    @ResponseBody
    @TransBean
    public ReturnResult page(Sku sku, Page page){
        page=skuService.pageList(sku,page);
        return ResultUtil.buildResult(ResultEnum.SUCC,page.getTotal(),page.getRecords());
    }

    @RequestMapping("save")
    @ResponseBody
    public ReturnResult save(@RequestBody @Validated FullSkuDTO fullSkuDTO){
        Sku sku=new Sku();
        SkuDTO skuDTO = fullSkuDTO.getSkuDTO();
        BeanUtils.copyProperties(skuDTO,sku);
        if(skuDTO.getOnSaleTime()!=null){
            sku.setOnSaleTime(skuDTO.getOnSaleTime().getTime());
        }
        sku.setCostPrice(MathUtil.format(skuDTO.getCostPrice(),BigDecimal.ROUND_DOWN));
        sku.setMarketPrice(MathUtil.format(skuDTO.getMarketPrice(),BigDecimal.ROUND_DOWN));
        sku.setPrice(MathUtil.format(skuDTO.getPrice(),BigDecimal.ROUND_DOWN));
        List<SkuProperty> skuProperties=genSkuProperty(fullSkuDTO);
        skuService.saveFullSku(sku,fullSkuDTO.getImgVoArray(),skuProperties);
        return ResultUtil.buildResult(ResultEnum.SUCC.getCode(),"sku保存成功");
    }

    private List<SkuProperty> genSkuProperty(@RequestBody @Validated FullSkuDTO fullSkuDTO) {
        List<SkuProperty> skuProperties=new ArrayList<>();
        JSONObject skuPropertyObj=new JSONObject();
        List<SkuPropertyDTO> skuPropertyList = fullSkuDTO.getSkuPropertyList();
        for (SkuPropertyDTO skuPropertyDTO : skuPropertyList) {
            SkuProperty skuProperty=new SkuProperty();
            BeanUtils.copyProperties(skuPropertyDTO,skuProperty);
            if(Constants.YES.equals(skuPropertyDTO.getNeedPic())){
                skuProperty.setSkuImage("yes");
            }
            skuProperties.add(skuProperty);
            JSONArray array=null;
            if(skuPropertyObj.containsKey(skuPropertyDTO.getGroupId())){
                array=skuPropertyObj.getJSONArray(skuPropertyDTO.getGroupId().toString());
            }else {
                array=new JSONArray();
                skuPropertyObj.put(skuPropertyDTO.getGroupId().toString(),array);
            }
            JSONObject subObject=new JSONObject();
            subObject.put(skuPropertyDTO.getPropertyName(),skuPropertyDTO.getOptionValue());
            array.add(subObject);
        }
        fullSkuDTO.getSkuDTO().setSkuProperties(skuPropertyObj.toJSONString());
        return skuProperties;
    }


    @RequestMapping("/upload")
    @ResponseBody
    public ReturnResult fileUpload(MultipartFile file, String productCode,String skuCode){
        Map<String,String> result=new HashMap<>();
        try {

            String originalFilename = file.getOriginalFilename();
            String fileType=originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            DefaultPutRet defaultPutRet = qiniuUtil.uploadFile(file.getInputStream(), qiniuUtil.getKey(ImageTypeEnum.SKU.getCode(),fileType));
            //预览图
            result.put("src",qiniuUtil.getModelUrl(defaultPutRet.key,qiniuUtil.PREVIEW_SIZE));
            result.put("key",defaultPutRet.key);
            // 大图 原图
            result.put("layerSrc",qiniuUtil.getUrl(defaultPutRet.key));
            SkuMedia skuMedia=new SkuMedia();
            skuMedia.setMediaType(MediaEnum.IMAGE.getCode());
            skuMedia.setProductCode(productCode);
            skuMedia.setSkuCode(skuCode);
            skuMedia.setImgKey(defaultPutRet.key);
            //存原图路径
            skuMedia.setImgUrl(qiniuUtil.getUrl(defaultPutRet.key));
            skuMediaService.insert(skuMedia);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultEnum.SERVER_ERROR,"上传文件失败");
        }

        return ResultUtil.buildResult(ResultEnum.SUCC,"上传成功",result);
    }

    /**
     * layEdit 的文件上传
     * @param file
     */
    @RequestMapping("/layEditUpload")
    @ResponseBody
    public ReturnResult layEditUpload(MultipartFile file){
        Map<String,String> result=new HashMap<>();
        try {
            String originalFilename = file.getOriginalFilename();
            String fileType=originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            DefaultPutRet defaultPutRet = qiniuUtil.uploadFile(file.getInputStream(), qiniuUtil.getKey(ImageTypeEnum.SKU.getCode(),fileType));
            //预览图
            result.put("src",qiniuUtil.getUrl(defaultPutRet.key));
            result.put("title","图片详情");
        } catch (Exception e) {
            throw new BusinessException(ResultEnum.SERVER_ERROR,"上传文件失败");
        }

        return ResultUtil.buildResult(ResultEnum.SUCC,"上传成功",result);
    }

    /**
     * 删除文件
     */
    @RequestMapping("/deleteFile")
    @ResponseBody
    public ReturnResult deleteFile(String key){
        qiniuUtil.deleteFile(key);
        SkuMedia skuMedia = skuMediaService.selectByKey(key);
        if(MediaEnum.MAIN_IMAGE.getCode().equals(skuMedia.getMediaType())){
            String skuCode = skuMedia.getSkuCode();
            Sku sku=skuService.selectBySkuCode(skuCode);
            sku.setSkuMainPic(null);
            skuService.updateAllColumnById(sku);
        }
        skuMediaService.deleteByKey(key);
        return ResultUtil.buildResult(ResultEnum.SUCC.getCode(),"删除成功");
    }
}

