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
import com.dmall.common.utils.StringUtil;
import com.dmall.plat.product.dto.FullProductDTO;
import com.dmall.plat.product.dto.PropsDTO;
import com.dmall.plat.product.dto.PropsGroupDTO;
import com.dmall.plat.product.vo.ProductVo;
import com.dmall.product.entity.*;
import com.dmall.product.service.*;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.swing.event.ListDataEvent;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;

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

    @Autowired
    private ProductExtService productExtService;

    @Autowired
    private ProductPropertyService productPropertyService;

    @Autowired
    private ProductMediaService productMediaService;

    @Autowired
    private PropsOptionService propsOptionService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private QiniuUtil qiniuUtil;


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
            Product product = productService.selectById(id);
            if(product==null){
                throw new BusinessException(ResultEnum.SERVER_ERROR,"商品信息不存在");
            }
            ProductExt productExt = productExtService.selectByProductCode(product.getProductCode());
            if(productExt==null){
                throw new BusinessException(ResultEnum.SERVER_ERROR,"商品扩展信息不存在");
            }
            List<PropsGroupDTO> propsDTOList= getProps(product.getProductCode());
            List<Map<String,String>> imgUrls=new ArrayList<>();
            List<ProductMedia> mediaList=productMediaService.selectByProductCode(product.getProductCode());
            for (ProductMedia productMedia : mediaList) {
                Map<String,String> map=new HashMap<>();
                map.put("imgSrc",qiniuUtil.getModelUrl(productMedia.getImgKey(),qiniuUtil.PREVIEW_SIZE));
                map.put("layerSrc",productMedia.getImgUrl());
                map.put("imgKey",productMedia.getImgKey());
                imgUrls.add(map);
            }
            ProductVo productVo=new ProductVo();
            productVo.setProduct(product);
            productVo.setProductExt(productExt);
            productVo.setPropsVoList(propsDTOList);
            productVo.setImgUrls(imgUrls);
            request.setAttribute("productVo",productVo);
        }else {
            ProductVo productVo=new ProductVo();
            Product product=new Product();
            ProductExt productExt=new ProductExt();
            productVo.setProduct(product);
            productVo.setProductExt(productExt);
            productVo.setPropsVoList(null);
            productVo.setImgUrls(null);
            request.setAttribute("productVo",productVo);
        }
        return "commodity/product/edit";
    }

    /**
     * 商品信息的保存
     * @param fullProductDTO
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ReturnResult save(@Validated @RequestBody FullProductDTO fullProductDTO){
        Product product=new Product();
        BeanUtils.copyProperties(fullProductDTO.getProduct(),product);
        product.setOnCityTime(fullProductDTO.getProduct().getOnCityTime().getTime());
        ProductExt ext=new ProductExt();
        ext.setId(fullProductDTO.getProductExt().getId());
        ext.setRichContent(fullProductDTO.getProductExt().getRichContent());
        List<PropsGroupDTO> propsVoList = fullProductDTO.getPropsVoList();
        JSONArray propsGroupArray=new JSONArray();
        for (PropsGroupDTO propsGroupDTO : propsVoList) {
            JSONObject propsGroup=new JSONObject();
            propsGroup.put("groupId",propsGroupDTO.getId());
            propsGroup.put("isSale",propsGroupDTO.getIsSale());
            JSONArray propsArray=new JSONArray();
            for (Props props : propsGroupDTO.getPropsList()) {
                propsArray.add(props.getId());
            }
            propsGroup.put("props",propsArray);
            propsGroupArray.add(propsGroup);
        }
        productService.saveFullProduct(product,ext,propsGroupArray,fullProductDTO.getImgVoArray());
        return ResultUtil.buildResult(ResultEnum.SUCC.getCode(),"商品保存成功");
    }

    /**
     * 文件上传
     */
   /* @RequestMapping("/upload")
    @ResponseBody
    public ReturnResult fileUpload(HttpServletRequest request){
        Map<String,String> result=new HashMap<>();
        try {

            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            //判断 request 是否有文件上传,即多部分请求
            if(multipartResolver.isMultipart(request)) {
                //转换成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                //取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    //取得上传文件
                    MultipartFile file = multiRequest.getFile(iter.next());
                    if (file != null) {
                        String originalFilename = file.getOriginalFilename();
                        String fileType=originalFilename.substring(originalFilename.lastIndexOf(".")+1);
                        DefaultPutRet defaultPutRet = qiniuUtil.uploadFile(file.getInputStream(), qiniuUtil.getKey(fileType));
                        //预览图
                        result.put("src",qiniuUtil.getModelUrl(defaultPutRet.key,160));
                        // 大图 原图
                        result.put("layerSrc",qiniuUtil.getUrl(defaultPutRet.key));
                    }
                }
            }

         } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultEnum.SERVER_ERROR,"上传文件失败");
        }

        return ResultUtil.buildResult(ResultEnum.SUCC,result);
    }*/

    @RequestMapping("/upload")
    @ResponseBody
    public ReturnResult fileUpload(MultipartFile file,String productCode){
        Map<String,String> result=new HashMap<>();
        try {

            String originalFilename = file.getOriginalFilename();
            String fileType=originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            DefaultPutRet defaultPutRet = qiniuUtil.uploadFile(file.getInputStream(), qiniuUtil.getKey(ImageTypeEnum.PRODUCT.getCode(),fileType));
            //预览图
            result.put("src",qiniuUtil.getModelUrl(defaultPutRet.key,qiniuUtil.PREVIEW_SIZE));
            result.put("key",defaultPutRet.key);
            // 大图 原图
            result.put("layerSrc",qiniuUtil.getUrl(defaultPutRet.key));
            ProductMedia productMedia=new ProductMedia();
            productMedia.setMediaType(MediaEnum.IMAGE.getCode());
            productMedia.setProductCode(productCode);
            productMedia.setImgKey(defaultPutRet.key);
            //存原图路径
            productMedia.setImgUrl(qiniuUtil.getUrl(defaultPutRet.key));
            productMediaService.insert(productMedia);
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
            DefaultPutRet defaultPutRet = qiniuUtil.uploadFile(file.getInputStream(), qiniuUtil.getKey(ImageTypeEnum.PRODUCT.getCode(),fileType));
            //预览图
            result.put("src",qiniuUtil.getUrl(defaultPutRet.key));
            result.put("title","图片详情");
        } catch (Exception e) {
            throw new BusinessException(ResultEnum.SERVER_ERROR,"上传文件失败");
        }

        return ResultUtil.buildResult(ResultEnum.SUCC,"上传成功",result);
    }
    /**
     * 更新文件
     * @return
     */
    @RequestMapping("/updateUpload")
    @ResponseBody
    public ReturnResult updateUpload(MultipartFile file,String key){
        Map<String,String> result=new HashMap<>();
        try {

            DefaultPutRet defaultPutRet = qiniuUtil.uploadFile(file.getInputStream(), key);
            //预览图
            result.put("src",qiniuUtil.getModelUrl(defaultPutRet.key,qiniuUtil.PREVIEW_SIZE));
            // 大图 原图
            result.put("layerSrc",qiniuUtil.getUrl(defaultPutRet.key));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultEnum.SERVER_ERROR,"上传文件失败");
        }

        return ResultUtil.buildResult(ResultEnum.SUCC,result);
    }

    /**
     * 删除文件
     */
    @RequestMapping("/deleteFile")
    @ResponseBody
    public ReturnResult deleteFile(String key){
        qiniuUtil.deleteFile(key);
        ProductMedia productMedia = productMediaService.selectByKey(key);
        if(MediaEnum.MAIN_IMAGE.getCode().equals(productMedia.getMediaType())){
            String productCode = productMedia.getProductCode();
            Product product = productService.selectByProductCode(productCode);
            product.setMainImage(null);
            productService.updateAllColumnById(product);
        }
        productMediaService.deleteByKey(key);
        return ResultUtil.buildResult(ResultEnum.SUCC.getCode(),"删除成功");
    }

    /**
     * 获取属性组列表
     */
    @RequestMapping("/getGroup")
    @ResponseBody
    public ReturnResult getGroup(@NotBlank(message = "商品编码不能为空") String productCode){
        List<JSONObject> list = productPropertyService.queryGroupByProductCode(productCode);
        return ResultUtil.buildResult(ResultEnum.SUCC,list);
    }

    /**
     * 获取属性列表
     */
    @RequestMapping("/getProps")
    @ResponseBody
    public ReturnResult getProp(@NotBlank(message = "商品编码不能为空") String productCode,String groupId){
        List<JSONObject> list = productPropertyService.queryPropsByProductCode(productCode,groupId);
        return ResultUtil.buildResult(ResultEnum.SUCC,list);
    }

    /**
     * 获取属性值列表
     */
    @RequestMapping("/getOptions")
    @ResponseBody
    public ReturnResult getOptions(@NotNull(message = "属性id不能为空") Long propsId){
        List<JSONObject> list = propsOptionService.queryOptionsByPropsId(propsId);
        return ResultUtil.buildResult(ResultEnum.SUCC,list);
    }


    /**
     * 跳转到SKU添加页面
     */
    @RequestMapping("skuEdit")
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
        // 查询商品下的属性组 属性
        List<Map<String,Object>> jsonObjects = productPropertyService.selectByProductCode(productCode);
        request.setAttribute("groupPropsArray",jsonObjects);
        request.setAttribute("productCode",productCode);
        request.setAttribute("productType",product.getProductType());
        request.setAttribute("brandId",product.getBrandId());
        request.setAttribute("sortIndex",sortIndex);
        return "commodity/product/skuEdit";
    }

    private List<PropsGroupDTO> getProps(String productCode) {
        List<PropsGroupDTO> propsDTOList=new ArrayList<>();
        List<Map<String,Object>> list= productPropertyService.queryByParam(productCode);
        Map<Long,List<Map<String,Object>>> map=new HashMap<>();
        for (Map<String,Object> stringObjectMap : list) {
            Long groupId=(Long) stringObjectMap.get("groupId");
            if(map.containsKey(groupId)){
                map.get(groupId).add(stringObjectMap);
            }else {
                List<Map<String,Object>> mapList=new ArrayList<>();
                mapList.add(stringObjectMap);
                map.put(groupId,mapList);
            }
        }
        PropsGroupDTO saleDTO=new PropsGroupDTO();
        for (Map.Entry<Long, List<Map<String, Object>>> longListEntry : map.entrySet()) {
            Long groupId=longListEntry.getKey();
            PropsGroupDTO groupDTO=new PropsGroupDTO();
            groupDTO.setId(groupId);
            List<Map<String, Object>> value = longListEntry.getValue();
            for (Map<String, Object> stringObjectMap : value) {
                groupDTO.setName((String) stringObjectMap.get("groupName"));
                groupDTO.setIsSale(Constants.NO);
                groupDTO.setProductType((String) stringObjectMap.get("productType"));
                Props props=new Props();
                props.setId((Long) stringObjectMap.get("propsId"));
                props.setName((String) stringObjectMap.get("propsName"));
                groupDTO.getPropsList().add(props);
                Integer isSale= (Integer) stringObjectMap.get("isSale");
                if(Constants.YES.equals(isSale)){
                    saleDTO.setIsSale(Constants.YES);
                    saleDTO.setProductType((String) stringObjectMap.get("productType"));
                    saleDTO.getPropsList().add(props);
                }
            }
            propsDTOList.add(groupDTO);
        }
        propsDTOList.add(saleDTO);

        return propsDTOList;
    }
}

