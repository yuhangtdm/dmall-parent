package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dmall.common.entity.Tree;
import com.dmall.product.entity.ProductType;
import com.dmall.product.mapper.ProductTypeMapper;
import com.dmall.product.service.ProductTypeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 循环的方式得到树结构
     */
    @Override
    public List<ProductType> tree(Long pid) {
        List<ProductType> result=new ArrayList<>();
        List<ProductType> typeList =null;
        if(pid==0L){
            EntityWrapper<ProductType> wrapper=new EntityWrapper<>();
            typeList= this.selectList(wrapper);
        }else {
            typeList=this.getLater(pid);
        }
        Map<Long,ProductType> map=new HashMap<>();
        for (ProductType productType : typeList) {
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
                map.get(productType.getPid()).setIsParent(true);
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
        List<ProductType> productTypes = this.selectList(wrapper);
        return productTypes;
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
            List<ProductType> children = tree(productType.getId());
            productType.setChildren(children);
        }
        return productTypes;
    }
}
