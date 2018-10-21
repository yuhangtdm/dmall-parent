package com.dmall.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dmall.product.entity.Sku;
import com.dmall.product.entity.SkuProperty;
import com.dmall.product.mapper.SkuPropertyMapper;
import com.dmall.product.service.ProductPropertyService;
import com.dmall.product.service.SkuPropertyService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * SKU属性值 服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Service
public class SkuPropertyServiceImpl extends ServiceImpl<SkuPropertyMapper, SkuProperty> implements SkuPropertyService {

    @Autowired
    private SkuPropertyMapper mapper;
    @Autowired
    private ProductPropertyService productPropertyService;
    @Override
    public void batchInsert(List<SkuProperty> skuPropertyList) {
        mapper.batchInsert(skuPropertyList);
    }

    @Override
    public List<SkuProperty> selectBySkuId(Long id) {
        EntityWrapper<SkuProperty> wrapper=new EntityWrapper<>();
        wrapper.eq("sku_id",id);
        return this.selectList(wrapper);
    }

    @Override
    public JSONArray selectSkuPropertySkuId(Sku sku) {
        JSONArray result = new JSONArray();
        // 查询商品下的分组 按照组排序
        List<JSONObject> jsonObjects = productPropertyService.queryGroupByProductCode(sku.getProductCode());
        for (JSONObject groupObj : jsonObjects) {
            // 根据组查询属性 按照属性顺序排序
            List<JSONObject> propsList=productPropertyService.queryPropsByProductCode(sku.getProductCode(),groupObj.getString("id"));
            for (JSONObject propObj : propsList) {
                propObj.put("groupId",groupObj.getLong("id"));
                propObj.put("groupName",groupObj.getString("name"));
                SkuProperty skuProperty=this.selectOption(sku.getId(),propObj.getLong("id"));
                if(skuProperty!=null){
                    propObj.put("optionId",skuProperty.getOptionId());
                    propObj.put("optionValue",skuProperty.getOptionValue());
                    if(skuProperty.getSkuImage()!=null){
                        propObj.put("needPic",true);
                    }else {
                        propObj.put("needPic",false);
                    }
                }
                result.add(propObj);
            }
        }
        return result;
    }

    @Override
    public void batchDelete(List<Long> deleteList,Long skuId) {
        EntityWrapper<SkuProperty> wrapper=new EntityWrapper<>();
        wrapper.eq("sku_id",skuId);
        wrapper.in("option_id",deleteList);
        this.delete(wrapper);
    }

    private SkuProperty selectOption(Long skuId, Long propertyId) {
        EntityWrapper<SkuProperty> wrapper=new EntityWrapper<>();
        wrapper.eq("sku_id",skuId);
        wrapper.eq("property_id",propertyId);
        return this.selectOne(wrapper);
    }
}
