package com.dmall.plat.product.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.annotation.TransBean;
import com.dmall.common.enums.ResultEnum;
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
import org.springframework.beans.factory.annotation.Autowired;
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
            ProductExt productExt = productExtService.selectByProductId(id);
            List<PropsGroupDTO> propsDTOList= getProps(id);
            ProductVo productVo=new ProductVo();
            productVo.setProduct(product);
            productVo.setProductExt(productExt);
            productVo.setPropsVoList(propsDTOList);
            request.setAttribute("productVo",productVo);
        }
        return "commodity/product/edit";
    }

    private List<PropsGroupDTO> getProps(Long productId) {
        List<PropsGroupDTO> propsDTOList=new ArrayList<>();
        List<Map<String,Object>> list= productPropertyService.queryByParam(productId);
        Map<Long,Map<String,Object>> map=new HashMap<>();
        for (Map<String,Object> stringObjectMap : list) {
            map.put((Long) stringObjectMap.get("groupId"),stringObjectMap);
        }
        for (Map<String, Object> obj : list) {
            PropsGroupDTO groupDTO=new PropsGroupDTO();
            groupDTO.setId((Long) obj.get("groupId"));
            groupDTO.setName((String) obj.get("groupName"));
            groupDTO.setIsSale((Integer) obj.get("isSale"));
            groupDTO.setProductType((String) obj.get("productType"));
            for(Map.Entry<Long,Map<String,Object>> entry:map.entrySet()){
                if(entry.getKey().equals(obj.get("groupId"))){
                    Map<String,Object> value = entry.getValue();
                    Props props=new Props();
                    props.setId((Long) value.get("propsId"));
                    props.setName((String) value.get("propsName"));
                    groupDTO.getPropsList().add(props);
                }
            }
            propsDTOList.add(groupDTO);
        }
        return propsDTOList;
    }
}

