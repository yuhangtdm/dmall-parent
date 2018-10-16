package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.Constants;
import com.dmall.common.enums.MediaEnum;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.common.utils.DateUtil;
import com.dmall.common.utils.StringUtil;
import com.dmall.product.entity.Sku;
import com.dmall.product.entity.SkuMedia;
import com.dmall.product.entity.SkuProperty;
import com.dmall.product.mapper.SkuMapper;
import com.dmall.product.service.SkuMediaService;
import com.dmall.product.service.SkuPropertyService;
import com.dmall.product.service.SkuService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dmall.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * SKU 服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {

    @Autowired
    private SkuMediaService mediaService;

    @Autowired
    private SkuPropertyService skuPropertyService;

    @Override
    public Page pageList(Sku sku, Page page) {
        EntityWrapper<Sku> wrapper=new EntityWrapper<>();
        wrapper.orderBy("sort_index",false);
        QueryUtil.queryForm(wrapper,sku);
        page = this.selectPage(page,wrapper);
        return page;
    }

    @Override
    public List<Sku> list(String productCode) {
        EntityWrapper<Sku> wrapper=new EntityWrapper<>();
        wrapper.orderBy("sort_index",false);
        wrapper.eq("product_code",productCode);
        return this.selectList(wrapper);
    }

    @Override
    public void saveFullSku(Sku sku, List<String> imgVoArray,List<SkuProperty> skuPropertyList) {
        if(sku.getId()==null){
            initSku(sku);
            this.insert(sku);
        }else {
            this.updateById(sku);
        }
        // 维护商品图片信息
        if (StringUtil.isNotEmptyObj(imgVoArray)) {
            for (int i=0;i<imgVoArray.size();i++) {
                String key= imgVoArray.get(i);
                SkuMedia skuMedia=mediaService.selectByKey(key);
                skuMedia.setSkuCode(sku.getSkuCode());
                if(i==0){
                    skuMedia.setMediaType(MediaEnum.MAIN_IMAGE.getCode());
                    //数据库存 key  方便前端根据大小查询
                    sku.setSkuMainPic(key);
                    this.updateById(sku);
                }
                skuMedia.setSortIndex(i+1);
                mediaService.updateById(skuMedia);
            }
        }
        //todu 定时上架功能
        if(StringUtil.isNotEmptyObj(skuPropertyList)){
            for (SkuProperty skuProperty : skuPropertyList) {
                skuProperty.setSkuId(sku.getId());
                if("yes".equals(skuProperty.getSkuImage())){
                    skuProperty.setSkuImage(sku.getSkuMainPic());
                }
            }
            skuPropertyService.batchInsert(skuPropertyList);
        }
    }

    @Override
    public Sku selectBySkuCode(String skuCode) {
        EntityWrapper<Sku> wrapper=new EntityWrapper<>();
        wrapper.eq("sku_code",skuCode);
        List<Sku> skus = this.selectList(wrapper);
        if(StringUtil.isEmptyObj(skus)){
            throw new BusinessException(ResultEnum.SERVER_ERROR,"sku编码不存在");
        }

        if(skus.size()>0){
            throw new BusinessException(ResultEnum.SERVER_ERROR,"sku编码必须唯一");
        }
        return skus.get(0);
    }



    /**
     * 初始化sku
     */
    private void initSku(Sku sku) {
        // SKU编码 S201810040001
        Sku todaySku=queryToday();
        String skuCode="";
        if(todaySku==null){
            skuCode="S"+DateUtil.formatDate(DateUtil.YYYYMMDD)+"0001";
        }else {
            String code = todaySku.getSkuCode();
            int i = Integer.parseInt(code.substring(code.length() - 4));
            skuCode="P"+DateUtil.formatDate(DateUtil.YYYYMMDD)+String.format("%04d",i+1);
        }
        sku.setSkuCode(skuCode);
        // 锁定库存
        sku.setFrozenStock(0);
        // 初始化 评论数量 好评数量 中评数量 差评数量 好评率 评分 销量 浏览量 是否可用
        sku.setCommentCount(0);
        sku.setGoodCommentCount(0);
        sku.setMiddleCommentCount(0);
        sku.setBadCommentCount(0);
        sku.setGoodRate(0.0d);
        sku.setScore(0.0d);
        sku.setSaleCount(0);
        sku.setViewCount(0);
        sku.setIsDel(Constants.YES);
        if(Constants.YES.equals(sku.getState())){
            sku.setOnSaleTime(System.currentTimeMillis());
        }
    }

    private Sku queryToday() {
        EntityWrapper<Sku> wrapper=new EntityWrapper<>();
        wrapper.like("product_code","P"+ DateUtil.formatDate(DateUtil.YYYYMMDD));
        wrapper.orderBy(true,"create_time",false);
        List<Sku> products = this.selectList(wrapper);
        if(StringUtil.isEmptyObj(products)){
            return null;
        }else {
            return products.get(0);
        }
    }
}
