package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dmall.product.entity.ProductProperty;
import com.dmall.product.mapper.ProductPropertyMapper;
import com.dmall.product.service.ProductPropertyService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
}
