package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.utils.ChineseCharToEnUtil;
import com.dmall.common.utils.StringUtil;
import com.dmall.product.entity.Brand;
import com.dmall.product.entity.ProductTypeBrand;
import com.dmall.product.mapper.BrandMapper;
import com.dmall.product.service.BrandService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dmall.product.service.ProductTypeBrandService;
import com.dmall.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-31
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Autowired
    private ProductTypeBrandService productTypeBrandService;

    @Override
    public Page pageList(Brand brand, Page page) {
        EntityWrapper<Brand> wrapper=new EntityWrapper<>();
        wrapper.orderBy("update_time",false);
        QueryUtil.queryForm(wrapper,brand);
        page = this.selectPage(page,wrapper);
        return page;
    }

    @Override
    @Transactional
    @CachePut(value = "brandCache",key = "'select:com.dmall.product.service.impl.BrandServiceImpl.list()'")
    public List<Brand> saveOrUpdate(Brand brand) {
        brand.setFirstLetter(ChineseCharToEnUtil.getFirstLetter(brand.getBrandName()));
        if (brand.getId()!=null){
            productTypeBrandService.deleteByBrandId(brand.getId());
            this.updateById(brand);
        }else {
            this.insert(brand);
        }
        if(StringUtil.isNotBlank(brand.getProductType())){
            String[] split = brand.getProductType().split(",");
            for (String s : split) {
                ProductTypeBrand productTypeBrand=new ProductTypeBrand();
                productTypeBrand.setBrandId(brand.getId());
                productTypeBrand.setProductTypeId(Long.parseLong(s));
                productTypeBrandService.insert(productTypeBrand);
            }
        }
        return list();
    }

    @Cacheable("brandCache")
    public List<Brand> list() {
        EntityWrapper<Brand> wrapper=new EntityWrapper<>();
        return super.selectList(wrapper);
    }

    @Override
    @CachePut(value = "brandCache",key = "'select:com.dmall.product.service.impl.BrandServiceImpl.list()'")
    public List<Brand> deleteById(Long id) {
        super.deleteById(id);
        return list();
    }
}
