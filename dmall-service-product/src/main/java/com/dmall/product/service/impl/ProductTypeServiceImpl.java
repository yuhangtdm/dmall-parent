package com.dmall.product.service.impl;

import com.dmall.common.entity.Tree;
import com.dmall.product.entity.ProductType;
import com.dmall.product.mapper.ProductTypeMapper;
import com.dmall.product.service.ProductTypeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品类型 服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Service
@CacheConfig(cacheNames = "product")
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductType> implements ProductTypeService {

    @Override
    public ProductType selectById(Long id) {
        return super.selectById(id);
    }

    @Override
    public List<Tree> tree() {
        return null;
    }
}
