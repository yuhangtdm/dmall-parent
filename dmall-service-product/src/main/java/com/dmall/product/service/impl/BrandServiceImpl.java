package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.Constants;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.common.utils.ChineseCharToEnUtil;
import com.dmall.common.utils.StringUtil;
import com.dmall.product.entity.Brand;
import com.dmall.product.entity.ProductType;
import com.dmall.product.entity.ProductTypeBrand;
import com.dmall.product.mapper.BrandMapper;
import com.dmall.product.service.BrandService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dmall.product.service.ProductTypeBrandService;
import com.dmall.product.service.ProductTypeService;
import com.dmall.util.QueryUtil;
import com.dmall.util.ValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    private BrandMapper brandMapper;

    @Autowired
    private ProductTypeBrandService productTypeBrandService;

    @Autowired
    private ProductTypeService productTypeService;

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
        if(!ValidUtil.valid(brand,"brandServiceImpl","name")){
            throw new BusinessException(ResultEnum.BAD_REQUEST,"品牌名称必须唯一");
        }
        List<Long> typeIds=buildTypeIds(brand);
        List<ProductType> valid=productTypeService.selectByParam(typeIds);
        if(StringUtil.isNotEmptyObj(valid)){
            throw new BusinessException(ResultEnum.BAD_REQUEST,"只可以设置三级商品类型");
        }
        brand.setFirstLetter(ChineseCharToEnUtil.getFirstLetter(brand.getName()));
        if (brand.getId()!=null){
            this.updateById(brand);
            List<ProductTypeBrand> productTypeBrands = productTypeBrandService.queryByBrandId(brand.getId());
            if(StringUtil.isNotEmptyObj(productTypeBrands)){
                List<Long> collect = productTypeBrands.stream().map(ProductTypeBrand::getProductTypeId).collect(Collectors.toList());
                List<Long> insertTypeIds=new ArrayList<>();
                List<Long> delTypeIds=new ArrayList<>();
                for (Long typeId : typeIds) {
                    if(!collect.contains(typeId)){
                        insertTypeIds.add(typeId);
                    }
                }
                for (ProductTypeBrand productTypeBrand : productTypeBrands) {
                    if(!typeIds.contains(productTypeBrand.getProductTypeId())){
                        delTypeIds.add(productTypeBrand.getProductTypeId());
                    }
                }
                if(StringUtil.isNotEmptyObj(delTypeIds)){
                    productTypeBrandService.deleteByTypeIds(brand.getId(),delTypeIds);
                }
                if(StringUtil.isNotEmptyObj(insertTypeIds)){
                    productTypeBrandService.batchInsert(brand.getId(),insertTypeIds);
                }
            }else {
                productTypeBrandService.batchInsert(brand.getId(),typeIds);
            }
        }else {
            this.insert(brand);
            productTypeBrandService.batchInsert(brand.getId(),typeIds);
        }

        return list();
    }

    @Cacheable(value = "brandCache",key = "'select:com.dmall.product.service.impl.BrandServiceImpl.list()'")
    public List<Brand> list() {
        EntityWrapper<Brand> wrapper=new EntityWrapper<>();
        return super.selectList(wrapper);
    }

    @Override
    public List<Brand> list(Long productType) {
        return brandMapper.listAll(productType);
    }



    @Override
    @CachePut(value = "brandCache",key = "'select:com.dmall.product.service.impl.BrandServiceImpl.list()'")
    @Transactional
    public List<Brand> delete(Long id) {
        //品牌维护了商品则不能删除 维护了类型 则删除关联数据
        if(ValidUtil.validList("productServiceImpl","brand_id",id)){
            throw new BusinessException(ResultEnum.SERVER_ERROR,"该品牌下包含商品，不可删除");
        }
        this.deleteById(id);
        productTypeBrandService.deleteByBrandId(id);
        return list();
    }

    private List<Long> buildTypeIds(Brand brand){
        List<Long> typeIds=new ArrayList<>();
        if(StringUtil.isNotBlank(brand.getProductType())){
            String[] split = brand.getProductType().split(",");
            for (String s : split) {
                typeIds.add(Long.parseLong(s));
            }
        }
        return typeIds;
    }

}
