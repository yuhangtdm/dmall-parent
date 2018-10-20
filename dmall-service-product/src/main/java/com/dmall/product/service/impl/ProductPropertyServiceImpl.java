package com.dmall.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dmall.common.utils.MathUtil;
import com.dmall.product.entity.Product;
import com.dmall.product.entity.ProductProperty;
import com.dmall.product.mapper.ProductPropertyMapper;
import com.dmall.product.service.ProductPropertyService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.omg.CORBA.MARSHAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 商品属性集 服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Service
public class ProductPropertyServiceImpl extends ServiceImpl<ProductPropertyMapper, ProductProperty> implements ProductPropertyService {

    @Autowired
    private ProductPropertyMapper mapper;

    @Override
    public List<ProductProperty> queryByProductCode(String productCode) {
        EntityWrapper<ProductProperty> wrapper=new EntityWrapper<>();
        wrapper.eq("product_code",productCode);
        return this.selectList(wrapper);
    }

    @Override
    public List<JSONObject> queryGroupByProductCode(String productCode) {
        List<JSONObject> result=mapper.queryGroupByProductCode(productCode);
        return result;
    }

    @Override
    public List<JSONObject> queryPropsByProductCode(String productCode, String groupId) {
        List<JSONObject> result=mapper.queryPropsByProductCode(productCode,groupId);
        return result;
    }

    @Override
    public List<Map<String, Object>> queryByParam(String productCode) {
        return mapper.queryByParam(productCode);
    }

    @Override
    public void deleteByGroupId(String productCode, Set<Long> groups) {
        EntityWrapper<ProductProperty> wrapper=new EntityWrapper<>();
        wrapper.eq("product_code",productCode);
        wrapper.in("group_id",groups);
        this.delete(wrapper);
    }

    @Override
    public void deleteByPropertyId(String productCode, Set<Long> props) {
        EntityWrapper<ProductProperty> wrapper=new EntityWrapper<>();
        wrapper.eq("product_code",productCode);
        wrapper.in("property_id",props);
        this.delete(wrapper);
    }

    @Override
    public List<ProductProperty> queryByProductCodeAndGroupId(String productCode, Long groupId) {
        EntityWrapper<ProductProperty> wrapper=new EntityWrapper<>();
        wrapper.eq("product_code",productCode);
        wrapper.eq("group_id",groupId);
        return  this.selectList(wrapper);
    }

    @Override
    public List<ProductProperty> queryByProductCodeAndPropertyId(String productCode, Long propertyId) {
        EntityWrapper<ProductProperty> wrapper=new EntityWrapper<>();
        wrapper.eq("product_code",productCode);
        wrapper.eq("property_id",propertyId);
        return this.selectList(wrapper);
    }

    @Override
    public List<Map<String,Object>> selectByProductCode(String productCode) {
        List<Map<String,Object>> result = new ArrayList<>();
        List<JSONObject> groupArray=mapper.queryGroupByProductCode(productCode);
        for (JSONObject object : groupArray) {
            List<JSONObject> propsList=mapper.queryPropsByProductCode(productCode,object.getString("id"));
            for (JSONObject jsonObject : propsList) {
                Map<String,Object> map=new HashMap<>();
                map.put("groupId",object.getLong("id"));
                map.put("groupName",object.getString("name"));
                map.put("id",jsonObject.getLong("id"));
                map.put("name",jsonObject.getString("name"));
                result.add(map);
            }
        }
        return result;
    }
}
