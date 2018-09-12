package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dmall.product.entity.ProductTypeBrand;
import com.dmall.product.mapper.ProductTypeBrandMapper;
import com.dmall.product.service.ProductTypeBrandService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-09-12
 */
@Service
public class ProductTypeBrandServiceImpl extends ServiceImpl<ProductTypeBrandMapper, ProductTypeBrand> implements ProductTypeBrandService {

    @Override
    public void deleteByBrandId(Long id) {
        EntityWrapper<ProductTypeBrand> wrapper=new EntityWrapper();
        wrapper.eq("brand_id",id);
        this.delete(wrapper);
    }

    @Override
    public List<ProductTypeBrand> queryByBrandId(Long brandId) {
        EntityWrapper<ProductTypeBrand> wrapper=new EntityWrapper();
        wrapper.eq("brand_id",brandId);
        return this.selectList(wrapper);
    }

    @Override
    public List<ProductTypeBrand> queryByProductTypeid(Long productTypeId) {
        EntityWrapper<ProductTypeBrand> wrapper=new EntityWrapper();
        wrapper.eq("product_type_id",productTypeId);
        return this.selectList(wrapper);
    }
}
