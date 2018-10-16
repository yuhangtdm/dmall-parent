package com.dmall.product.service.impl;

import com.dmall.product.entity.SkuProperty;
import com.dmall.product.mapper.SkuPropertyMapper;
import com.dmall.product.service.SkuPropertyService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Override
    public void batchInsert(List<SkuProperty> skuPropertyList) {
        mapper.batchInsert(skuPropertyList);
    }
}
