package com.dmall.plat.product.controller;


import com.alibaba.fastjson.JSON;
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
import com.dmall.plat.product.dto.FullSkuDTO;
import com.dmall.plat.product.dto.SkuDTO;
import com.dmall.plat.product.dto.SkuPropertyDTO;
import com.dmall.product.entity.*;
import com.dmall.product.service.*;
import com.dmall.util.ValidUtil;
import com.dmall.web.common.result.ReturnResult;
import com.dmall.web.common.utils.QiniuUtil;
import com.dmall.web.common.utils.ResultUtil;
import com.qiniu.storage.model.DefaultPutRet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
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
    private SkuPropertyService skuPropertyService;
    @Autowired
    private QiniuUtil qiniuUtil;

    /**
     * 商品编辑页面的sku列表
     */
    @RequestMapping("page")
    @ResponseBody
    @TransBean
    public ReturnResult page(Sku sku, Page page){
        page=skuService.pageList(sku,page);
        return ResultUtil.buildResult(ResultEnum.SUCC,page.getTotal(),page.getRecords());
    }

    /**
     * sku分页查询
     */
    @GetMapping("skuPage")
    @ResponseBody
    public ReturnResult skuPage(@RequestParam  Map<String,Object> param, Page page){
        page=skuService.skuPageList(param,page);
        return ResultUtil.buildResult(ResultEnum.SUCC,page.getTotal(),page.getRecords());
    }

    /**
     * 跳转到SKU编辑页面
     */
    @RequestMapping("edit")
    public String edit(@NotNull(message = "skuId不能为空") Long id, HttpServletRequest request){
        Sku sku = skuService.selectById(id);
        if (sku==null){
            throw new BusinessException(ResultEnum.SERVER_ERROR,"skuId不存在");
        }
        Product product = productService.selectByProductCode(sku.getProductCode());
        if(product==null){
            throw new BusinessException(ResultEnum.SERVER_ERROR,"商品信息为空");
        }
        // sku属性
        JSONArray groupPropsArray = skuPropertyService.selectSkuPropertySkuId(sku);
        // sku图片
        JSONArray imgArray=new JSONArray();
        List<SkuMedia> mediaList=skuMediaService.selectBySkuCode(sku.getSkuCode());
        for (SkuMedia skuMedia : mediaList) {
            JSONObject imgObj=new JSONObject();
            imgObj.put("imgSrc",qiniuUtil.getModelUrl(skuMedia.getImgKey(),qiniuUtil.PREVIEW_SIZE));
            imgObj.put("layerSrc",skuMedia.getImgUrl());
            imgObj.put("imgKey",skuMedia.getImgKey());
            imgArray.add(imgObj);
        }
        request.setAttribute("sku",sku);
        request.setAttribute("productName",product.getName());
        request.setAttribute("groupPropsArray",groupPropsArray);
        request.setAttribute("imgArray",imgArray);
        request.setAttribute("code", JSON.toJSONString(JSONObject.parseObject(sku.getSkuProperties()),true));
        return "commodity/sku/edit";
    }

    /**
     * sku保存或更新
     */
    @RequestMapping("save")
    @ResponseBody
    public ReturnResult save(@RequestBody @Validated FullSkuDTO fullSkuDTO){
        Sku sku=new Sku();
        SkuDTO skuDTO = fullSkuDTO.getSkuDTO();
        BeanUtils.copyProperties(skuDTO,sku);
        if(!ValidUtil.valid(sku,"skuServiceImpl","skuName")){
            throw new BusinessException(ResultEnum.BAD_REQUEST,"sku名称必须唯一");
        }
        if(skuDTO.getOnSaleTime()!=null){
            sku.setOnSaleTime(skuDTO.getOnSaleTime().getTime());
        }
        sku.setCostPrice(MathUtil.format(skuDTO.getCostPrice(),BigDecimal.ROUND_DOWN));
        sku.setMarketPrice(MathUtil.format(skuDTO.getMarketPrice(),BigDecimal.ROUND_DOWN));
        sku.setPrice(MathUtil.format(skuDTO.getPrice(),BigDecimal.ROUND_DOWN));
        List<SkuProperty> skuProperties=genSkuProperty(sku,fullSkuDTO.getSkuPropertyList());
        skuService.saveFullSku(sku,fullSkuDTO.getImgVoArray(),skuProperties);
        return ResultUtil.buildResult(ResultEnum.SUCC.getCode(),"sku保存成功");
    }

    /**
     * 图片上传
     */
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

    /**
     * 上架
     */
    @RequestMapping("/on")
    @ResponseBody
    public ReturnResult onSale(@RequestParam @NotNull(message = "skuId不能为空") Long id){
        skuService.onSale(id);
        return ResultUtil.buildResult(ResultEnum.SUCC.getCode(),"上架成功");
    }

    /**
     * 下架
     */
    @RequestMapping("/off")
    @ResponseBody
    public ReturnResult offSale(@RequestParam @NotNull(message = "skuId不能为空") Long id){
        skuService.offSale(id);
        return ResultUtil.buildResult(ResultEnum.SUCC.getCode(),"下架成功");
    }


    private List<SkuProperty> genSkuProperty(Sku sku,List<SkuPropertyDTO> skuPropertyList)  {
        List<SkuProperty> skuProperties=new ArrayList<>();
        JSONObject skuPropertyObj=new JSONObject();
        for (SkuPropertyDTO skuPropertyDTO : skuPropertyList) {
            SkuProperty skuProperty=new SkuProperty();
            BeanUtils.copyProperties(skuPropertyDTO,skuProperty);
            if(Constants.YES.equals(skuPropertyDTO.getNeedPic())){
                skuProperty.setSkuImage("yes");
            }
            skuProperties.add(skuProperty);
            JSONArray array;
            if(skuPropertyObj.containsKey(skuPropertyDTO.getGroupName())){
                array=skuPropertyObj.getJSONArray(skuPropertyDTO.getGroupName());
            }else {
                array=new JSONArray();
                skuPropertyObj.put(skuPropertyDTO.getGroupName(),array);
            }
            JSONObject subObject=new JSONObject();
            subObject.put(skuPropertyDTO.getPropertyName(),skuPropertyDTO.getOptionValue());
            array.add(subObject);
        }
        sku.setSkuProperties(skuPropertyObj.toJSONString());
        return skuProperties;
    }

}

