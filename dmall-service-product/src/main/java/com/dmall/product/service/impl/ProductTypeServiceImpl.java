package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dmall.common.Constants;
import com.dmall.common.entity.Tree;
import com.dmall.common.utils.StringUtil;
import com.dmall.product.entity.ProductType;
import com.dmall.product.entity.ProductTypeBrand;
import com.dmall.product.mapper.ProductTypeBrandMapper;
import com.dmall.product.mapper.ProductTypeMapper;
import com.dmall.product.service.ProductTypeBrandService;
import com.dmall.product.service.ProductTypeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
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
@CacheConfig(cacheNames = "product")
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductType> implements ProductTypeService {

    @Autowired
    private ProductTypeMapper mapper;

    @Autowired
    private ProductTypeBrandService productTypeBrandService;

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
    public void saveOrUpdate(ProductType type) {
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
    }

    @Override
    public List<ProductType> getSun(Long pid) {
        EntityWrapper<ProductType> wrapper=new EntityWrapper<>();
        wrapper.eq("pid",pid);
        return  this.selectList(wrapper);
    }

    @Override
    public void updateSort(Long pid) {
        List<ProductType> tree = tree(pid, null, null);
        update(tree);


    }

    @Override
    @Transactional
    public void batchDelete(Long id) {
        List<ProductType> later = getLater(id);
        for (ProductType productType : later) {
            this.deleteById(productType.getId());
        }


    }

    // 为类型设置品牌
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


    private void update(List<ProductType> tree) {
        for (int i=0;i<tree.size();i++) {
            ProductType productType = tree.get(i);
            productType.setSortIndex((i+1));
            this.updateById(productType);
            if(productType.getChildren().size()>0){
                update(productType.getChildren());
            }
        }
    }


    /**
     * 保存路径
     * @param type
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
