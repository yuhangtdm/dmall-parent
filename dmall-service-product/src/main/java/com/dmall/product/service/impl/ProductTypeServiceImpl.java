package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dmall.common.Constants;
import com.dmall.common.entity.Tree;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.common.utils.StringUtil;
import com.dmall.product.entity.Product;
import com.dmall.product.entity.ProductType;
import com.dmall.product.entity.ProductTypeBrand;
import com.dmall.product.mapper.ProductTypeBrandMapper;
import com.dmall.product.mapper.ProductTypeMapper;
import com.dmall.product.service.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dmall.util.ValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品类型 服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Service
@CacheConfig(cacheNames = "productType")
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductType> implements ProductTypeService {

    @Autowired
    private ProductTypeMapper mapper;
    @Autowired
    private ProductTypeBrandService productTypeBrandService;
    @Autowired
    private PropsGroupService propsGroupService;
    @Autowired
    private PropsService propsService;
    @Autowired
    private PropsOptionService propsOptionService;

    /**
     * 循环的方式得到树结构
     */
    @Override
    public List<ProductType> tree(Long pid,Integer level,String flag) {
        List<ProductType> result=new ArrayList<>();
        List<ProductType> typeList =null;
        if(pid.equals(0L)){
            EntityWrapper<ProductType> wrapper=new EntityWrapper<>();
            if("add".equals(flag)){
                wrapper.le("level",level);
            }else if("edit".equals(flag)){
                wrapper.lt("level",level);
            }
            wrapper.orderBy("sort_index");

            typeList= this.selectList(wrapper);
        }else {
            typeList=this.getLater(pid);
        }
        Map<Long,ProductType> map=new HashMap<>();
        for (ProductType productType : typeList) {
            if(Constants.LEVEL_ONE.equals(productType.getLevel()) || Constants.LEVEL_TWO.equals(productType.getLevel())){
                productType.setIsParent(true);
            }
            map.put(productType.getId(),productType);
        }

        /**
         * 遍历所有的类型
         */
        for (ProductType productType : typeList) {
            if(productType.getPid().equals(0L)){
                result.add(productType);
            }else if(productType.getId().equals(pid)){
                result.add(productType);
            }
            if(map.get(productType.getPid())!=null){
                map.get(productType.getPid()).getChildren().add(productType);
            }
            /**
             * 双重循环 效率较低
             * 以空间换时间 构建map
            for (ProductType type : typeList) {
                if(type.getPid().equals(productType.getId())){
                    productType.getChildren().add(type);
                }
            }*/
        }
        return result;
    }

    @Override
    @Cacheable(key = "'productType:ztree'")
    public List<ProductType> ztree(long pid) {
        return tree(pid,null,null);
    }
    @Override
    public List<ProductType> getLater(Long pid) {
        ProductType productType = this.selectById(pid);
        EntityWrapper<ProductType> wrapper=new EntityWrapper<>();
        wrapper.like("path",productType.getPath());
        wrapper.orderBy("sort_index");
        List<ProductType> productTypes = this.selectList(wrapper);
        return productTypes;
    }

    @Override
    @Transactional
    @CachePut(key = "'productType:ztree'")
    public List<ProductType> saveOrUpdate(ProductType type) {
        if(!ValidUtil.valid(type,"productTypeServiceImpl","pid","name")){
            throw new BusinessException(ResultEnum.BAD_REQUEST,"同一父商品类型下的名称必须唯一");
        }
        //编辑时需要修改所有后代元素的path
        if(type.getId()!=null){
            savePath(type);
            if(Constants.LEVEL_TWO.equals(type.getLevel())){
                List<ProductType> sun = getSun(type.getId());
                if(sun.size()>0){
                    for (ProductType productType : sun) {
                        productType.setPath(type.getPath()+productType.getId()+".");
                    }
                    mapper.updateBatch(sun);
                }
            }
            this.updateById(type);
        }else {
            this.insert(type);
            savePath(type);
            this.updateById(type);
        }
        return ztree(0L);
    }

    @Override
    public List<ProductType> getSun(Long pid) {
        EntityWrapper<ProductType> wrapper=new EntityWrapper<>();
        wrapper.eq("pid",pid);
        return  this.selectList(wrapper);
    }

    @Override
    @Transactional
    @CachePut(key = "'productType:ztree'")
    public List<ProductType> batchDelete(Long id) {
        //类型下维护了商品 维护了属性 不可删除
        List<ProductType> later = getLater(id);
        for (ProductType productType : later) {
             if(!valid(productType)){
                throw new BusinessException(ResultEnum.SERVER_ERROR,productType.getName()+"下有商品,不可删除");
             }
        }
        // 删除分类 分类下的属性组 属性 属性值 品牌
        for (ProductType productType : later) {
            if(Constants.LEVEL_ONE.equals(productType.getLevel()) || Constants.LEVEL_TWO.equals(productType.getLevel())){
               this.deleteById(productType.getId());
            }else{
                this.deleteObj(productType);
            }
        }
        return ztree(0L);
    }

    @Override
    @Transactional
    public void setBrand(Map<String, List<Long>> build) {
        List<Long> typeIds = build.get("typeIds");
        Long typeId = typeIds.get(0);
        List<Long> brandIds = build.get("brandIds");
        List<ProductTypeBrand> productTypeBrands = productTypeBrandService.queryByProductTypeid(typeId);
        if(StringUtil.isEmptyObj(productTypeBrands)){
            productTypeBrandService.batchInsert(brandIds,typeId);
        }else {
            List<Long> insertBrandIds=new ArrayList<>();
            List<Long> delBrandIds=new ArrayList<>();
            List<Long> collect = productTypeBrands.stream().map(ProductTypeBrand::getBrandId).collect(Collectors.toList());
            for (Long brandId : brandIds) {
                if(!collect.contains(brandId)){
                    insertBrandIds.add(brandId);
                }
            }
            for (Long brandId : collect) {
                if(!brandIds.contains(brandId)){
                    delBrandIds.add(brandId);
                }
            }
            if(StringUtil.isNotEmptyObj(insertBrandIds)){
                productTypeBrandService.batchInsert(insertBrandIds,typeId);
            }
            if(StringUtil.isNotEmptyObj(delBrandIds)){
                productTypeBrandService.deleteByBrandId(delBrandIds,typeId);
            }
        }
    }

    @Override
    public List<ProductType> selectByParam(List<Long> typeIds) {
        EntityWrapper<ProductType> wrapper=new EntityWrapper<>();
        wrapper.in("id",typeIds);
        wrapper.in("level",Arrays.asList(Constants.LEVEL_ONE,Constants.LEVEL_TWO));
        List<ProductType> productTypes = this.selectList(wrapper);
        return productTypes;
    }

    private void deleteObj(ProductType productType) {
        ProductType father = this.selectById(productType.getPid());
        ProductType grandFather=this.selectById(father.getPid());
        String type=grandFather.getId()+"/"+father.getId()+"/"+productType.getId();
        propsGroupService.deleteByProductType(type);
        propsService.deleteByProductType(type);
        propsOptionService.deleteByProductType(type);
        productTypeBrandService.deleteByProductType(productType.getId());
        this.deleteById(productType.getId());
    }

    /**
     * 判断商品分类下是否有商品
     */
    private boolean valid(ProductType productType) {
        if(Constants.LEVEL_ONE.equals(productType.getLevel()) || Constants.LEVEL_TWO.equals(productType.getLevel())){
            return true;
        }else {
            ProductType father = this.selectById(productType.getPid());
            ProductType grandFather=this.selectById(father.getPid());
            String type=grandFather.getId()+"/"+father.getId()+"/"+productType.getId();
            return ValidUtil.validList("productServiceImpl","product_type",type);
        }
    }
    /**
     * 设置path
     */
    private void savePath(ProductType type) {
        //一级分类
        if(type.getPid()==0L){
            type.setPath("."+type.getId()+".");
            type.setLevel(Constants.LEVEL_ONE);
        }else {
            ProductType parent = this.selectById(type.getPid());
            if(parent.getPid()==0L){
                type.setLevel(Constants.LEVEL_TWO);
            }else {
                type.setLevel(Constants.LEVEL_THREE);
            }
            type.setPath(parent.getPath()+type.getId()+".");
        }
    }
    /**
     * 递归的方式得到树结构
     * 此种方式查询次数过多 不宜使用
     */
    private List<ProductType> tree1(Long pid) {
        // 根据pid查询它的下级
        EntityWrapper<ProductType> wrapper=new EntityWrapper<>();
        wrapper.eq("pid",pid);
        List<ProductType> productTypes = this.selectList(wrapper);
        //遍历下级 循环隐含了递归的退出条件
        for (ProductType productType : productTypes) {
            // 下级的下级
            List<ProductType> children = tree1(productType.getId());
            productType.setChildren(children);
        }
        return productTypes;
    }
}
