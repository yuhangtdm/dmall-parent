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
    public List<ProductProperty> selectByProductId(Long productId) {
        EntityWrapper<ProductProperty> wrapper=new EntityWrapper<>();
        wrapper.eq("product_id",productId);
        return this.selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> queryByParam(Long productId) {
        return mapper.queryByParam(productId);
    }
}
