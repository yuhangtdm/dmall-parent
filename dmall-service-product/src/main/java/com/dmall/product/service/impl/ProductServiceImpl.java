package com.dmall.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.Constants;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.common.utils.DateUtil;
import com.dmall.common.utils.StringUtil;
import com.dmall.product.entity.Product;
import com.dmall.product.entity.ProductExt;
import com.dmall.product.entity.ProductProperty;
import com.dmall.product.mapper.ProductExtMapper;
import com.dmall.product.mapper.ProductMapper;
import com.dmall.product.mapper.ProductPropertyMapper;
import com.dmall.product.service.ProductPropertyService;
import com.dmall.product.service.ProductService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dmall.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductExtMapper extMapper;

    @Autowired
    private ProductPropertyService productPropertyService;

    @Override
    public Page pageList(Product product, Page page) {
        EntityWrapper<Product> wrapper=new EntityWrapper<>();
        wrapper.orderBy("update_time",false);
        QueryUtil.queryForm(wrapper,product);
        page = this.selectPage(page,wrapper);
        return page;
    }

    @Override
    @Transactional
    public void saveFullProduct(Product product, ProductExt ext, JSONArray propsGroupArray) {
        // 属性组与是否销售属性的容器
        Map<Long,Integer> saleMap=new HashMap<>();
        // 新增商品
        if(product.getId()==null){
            // 商品编码设值 初始化各种属性值
            initProduct(product);
            this.insert(product);
            // 维护商品扩展信息
            ext.setProductCode(product.getProductCode());
            extMapper.insert(ext);
            // 维护商品属性信息
            for (int i=0;i<propsGroupArray.size();i++) {
                JSONObject group = propsGroupArray.getJSONObject(i);
                Long groupId = group.getLong("groupId");
                Integer isSale = group.getInteger("isSale");
                if(Constants.NO.equals(isSale) && groupId==null){
                    throw new BusinessException(ResultEnum.BAD_REQUEST,"非销售属性必须选中属性组");
                }
                // 新增非销售属性
                if(Constants.NO.equals(isSale)){
                    insertProductProperty(group,product.getProductCode(),saleMap,isSale);
                }
            }
        }else{
            // 更新商品以及附件信息、属性信息
            this.updateById(product);
            extMapper.updateById(ext);
            String productCode=product.getProductCode();
            // 查询该商品的属性集合
            List<ProductProperty> productProperties = productPropertyService.queryByProductCode(productCode);
            for (int i=0;i<propsGroupArray.size();i++) {
                JSONObject group = propsGroupArray.getJSONObject(i);
                Long groupId = group.getLong("groupId");
                Integer isSale = group.getInteger("isSale");
                if(Constants.NO.equals(isSale) && groupId==null){
                    throw new BusinessException(ResultEnum.BAD_REQUEST,"非销售属性必须选中属性组");
                }
                if(Constants.NO.equals(isSale)){
                    Set<Long> oldProps=new HashSet<>();
                    Set<Long> insertProps=new HashSet<>();
                    Set<Long> deleteProps=new HashSet<>();
                    JSONArray propsArray = group.getJSONArray("props");
                    for (ProductProperty productProperty : productProperties) {
                        oldProps.add(productProperty.getPropertyId());
                        if(!propsArray.contains(productProperty.getPropertyId())){
                            deleteProps.add(productProperty.getPropertyId());
                        }
                    }
                    for (int j=0;j<propsArray.size();j++) {
                        if(!oldProps.contains(propsArray.getLong(j))){
                            insertProps.add(propsArray.getLong(j));
                        }
                    }
                    productPropertyService.deleteByPropertyId(product.getProductCode(),deleteProps);
                    for (Long insertProp : insertProps) {
                        ProductProperty productProperty=new ProductProperty();
                        productProperty.setProductCode(productCode);
                        productProperty.setGroupId(groupId);
                        productProperty.setPropertyId(insertProp);
                        productProperty.setIsSale(Constants.NO);
                        productPropertyService.insert(productProperty);
                        saleMap.put(insertProp,isSale);
                    }
                }
            }
        }
        // 修改是销售属性的数据
        List<ProductProperty> productPropertyList = productPropertyService.queryByProductCode(product.getProductCode());
        for (ProductProperty productProperty : productPropertyList) {
            Integer isSale = saleMap.get(productProperty.getPropertyId());
            if(Constants.YES.equals(isSale)){
                productProperty.setIsSale(isSale);
                productPropertyService.updateById(productProperty);
            }
        }
    }

    private void insertProductProperty(JSONObject jsonGroup,String productCode,Map<Long,Integer> saleMap,Integer isSale) {
        Long groupId=jsonGroup.getLong("groupId");
        JSONArray props=jsonGroup.getJSONArray("props");
        for(int j=0;j<props.size();j++){
            Long propertyId=props.getLong(j);
            ProductProperty productProperty=new ProductProperty();
            productProperty.setProductCode(productCode);
            productProperty.setGroupId(groupId);
            productProperty.setPropertyId(propertyId);
            productProperty.setIsSale(Constants.NO);
            productPropertyService.insert(productProperty);
            saleMap.put(propertyId,isSale);
        }
    }

    private synchronized void initProduct(Product product){
        // 商品编码 P20181004000001
        Product todayProduct=queryToday();
        String productCode="";
        if(todayProduct==null){
            productCode="P"+DateUtil.formatDate(DateUtil.YYYYMMDD)+"0001";
        }else {
            String code = todayProduct.getProductCode();
            int i = Integer.parseInt(code.substring(code.length() - 4));
            productCode=String.format("%04d",i+1);
        }
        product.setProductCode(productCode);
        // 初始化商品最高价和最低价
        product.setMaxPrice(0.0d);
        product.setMinPrice(0.0d);
        // 初始化商品销量、浏览量、评论数、评分、好评数、好评率、中评数、差评数
        product.setSaleCount(0);
        product.setViewCount(0);
        product.setCommentCount(0);
        product.setCommentScore(0.0d);
        product.setGoodCommentCount(0);
        product.setGoodRate(0.0d);
        product.setMiddleCommentCount(0);
        product.setBadCommentCount(0);
    }


    public Product queryToday() {
        EntityWrapper<Product> wrapper=new EntityWrapper<>();
        wrapper.like("product_code","P"+ DateUtil.formatDate(DateUtil.YYYYMMDD));
        wrapper.orderBy(true,"create_time",false);
        List<Product> products = this.selectList(wrapper);
        if(StringUtil.isEmptyObj(products)){
            return null;
        }else {
            return products.get(0);
        }
    }


}
