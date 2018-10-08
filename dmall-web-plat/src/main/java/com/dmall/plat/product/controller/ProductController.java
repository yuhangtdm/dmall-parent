package com.dmall.plat.product.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.Constants;
import com.dmall.common.annotation.TransBean;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.plat.product.dto.FullProductDTO;
import com.dmall.plat.product.dto.PropsDTO;
import com.dmall.plat.product.dto.PropsGroupDTO;
import com.dmall.plat.product.vo.ProductVo;
import com.dmall.product.entity.Product;
import com.dmall.product.entity.ProductExt;
import com.dmall.product.entity.ProductProperty;
import com.dmall.product.entity.Props;
import com.dmall.product.service.ProductExtService;
import com.dmall.product.service.ProductPropertyService;
import com.dmall.product.service.ProductService;
import com.dmall.web.common.result.ReturnResult;
import com.dmall.web.common.utils.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.swing.event.ListDataEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            ProductVo productVo=new ProductVo();
            productVo.setProduct(product);
            productVo.setProductExt(productExt);
            productVo.setPropsVoList(propsDTOList);
            request.setAttribute("productVo",productVo);
        }
        return "commodity/product/edit";
    }


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
        productService.saveFullProduct(product,ext,propsGroupArray);

        return ResultUtil.buildResult(ResultEnum.SUCC);
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

