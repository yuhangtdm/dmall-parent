package com.dmall.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.Constants;
import com.dmall.common.enums.MediaEnum;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.common.utils.DateUtil;
import com.dmall.common.utils.StringUtil;
import com.dmall.product.entity.*;
import com.dmall.product.mapper.ProductExtMapper;
import com.dmall.product.mapper.ProductMapper;
import com.dmall.product.mapper.ProductPropertyMapper;
import com.dmall.product.service.ProductMediaService;
import com.dmall.product.service.ProductPropertyService;
import com.dmall.product.service.ProductService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dmall.product.service.PropsService;
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

    @Autowired
    private PropsService propsService;

    @Autowired
    private ProductMediaService mediaService;

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
    public void saveFullProduct(Product product, ProductExt ext, JSONArray propsGroupArray, List<String> imgVoArray) {
        // 属性组与是销售属性的容器
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
                insertProductProperty(group,product.getProductCode(),saleMap,isSale);
            }
        }else {
            // 更新商品以及附件信息、属性信息
            this.updateById(product);
            extMapper.updateById(ext);
            String productCode = product.getProductCode();
            // 存放前端传递的非销售属性的属性集合
            JSONArray nowPropsArray = new JSONArray();
            for (int i = 0; i < propsGroupArray.size(); i++) {
                JSONObject group = propsGroupArray.getJSONObject(i);
                Integer isSale = group.getInteger("isSale");
                JSONArray propsArray = group.getJSONArray("props");
                for (int j = 0; j < propsArray.size(); j++) {
                    if(Constants.NO.equals(isSale)) {
                        nowPropsArray.add(propsArray.getLong(j));
                    }else{
                        // 销售属性
                        saleMap.put(propsArray.getLong(j), isSale);
                    }
                }
            }
            // 该商品下的所有销售属性
            List<ProductProperty> productProperties = productPropertyService.queryByProductCode(productCode);
            Set<Long> oldProps = new HashSet<>();
            Set<Long> insertProps = new HashSet<>();
            Set<Long> deleteProps = new HashSet<>();

            for (ProductProperty productProperty : productProperties) {
                oldProps.add(productProperty.getPropertyId());
                if (!nowPropsArray.contains(productProperty.getPropertyId())) {
                    deleteProps.add(productProperty.getPropertyId());
                }
            }
            for (int j = 0; j < nowPropsArray.size(); j++) {
                if (!oldProps.contains(nowPropsArray.getLong(j))) {
                    insertProps.add(nowPropsArray.getLong(j));
                }
            }
            if (StringUtil.isNotEmptyObj(deleteProps)) {
                productPropertyService.deleteByPropertyId(product.getProductCode(), deleteProps);
            }
            if(StringUtil.isNotEmptyObj(insertProps)){
                for (Long insertProp : insertProps) {
                    Props props = propsService.selectById(insertProp);
                    ProductProperty productProperty = new ProductProperty();
                    productProperty.setProductCode(productCode);
                    productProperty.setGroupId(props.getGroupId());
                    productProperty.setPropertyId(insertProp);
                    productProperty.setIsSale(Constants.NO);
                    productPropertyService.insert(productProperty);
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
        // 维护商品图片信息
        if (StringUtil.isNotEmptyObj(imgVoArray)) {
            for (int i=0;i<imgVoArray.size();i++) {
                String key= imgVoArray.get(i);
                ProductMedia productMedia=mediaService.selectByKey(key);
                productMedia.setProductCode(product.getProductCode());
                if(i==0){
                    productMedia.setMediaType(MediaEnum.MAIN_IMAGE.getCode());
                    //数据库存 key  方便前端根据大小查询
                    product.setMainImage(key);
                    this.updateById(product);
                }
                productMedia.setSortIndex(i+1);
                mediaService.updateById(productMedia);
            }
        }

    }

    private void insertProductProperty(JSONObject jsonGroup,String productCode,Map<Long,Integer> saleMap,Integer isSale) {
        Long groupId=jsonGroup.getLong("groupId");
        JSONArray props=jsonGroup.getJSONArray("props");
        for(int j=0;j<props.size();j++){
            Long propertyId=props.getLong(j);
            if(Constants.NO.equals(isSale)){
                ProductProperty productProperty=new ProductProperty();
                productProperty.setProductCode(productCode);
                productProperty.setGroupId(groupId);
                productProperty.setPropertyId(propertyId);
                productProperty.setIsSale(Constants.NO);
                productPropertyService.insert(productProperty);
            }else{
                saleMap.put(propertyId,isSale);
            }
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

    @Override
    public Product selectByProductCode(String productCode) {
        EntityWrapper<Product> wrapper=new EntityWrapper<>();
        wrapper.like("product_code",productCode);
        List<Product> products = this.selectList(wrapper);
        if(StringUtil.isEmptyObj(products)){
            throw new BusinessException(ResultEnum.SERVER_ERROR.getCode(),"商品编码不存在");
        }
        if(products.size()>1){
            throw new BusinessException(ResultEnum.SERVER_ERROR.getCode(),"商品编码必须唯一");
        }
        return products.get(0);
    }


}
