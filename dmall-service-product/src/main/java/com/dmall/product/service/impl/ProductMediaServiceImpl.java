package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.common.utils.StringUtil;
import com.dmall.product.entity.ProductMedia;
import com.dmall.product.mapper.ProductMediaMapper;
import com.dmall.product.service.ProductMediaService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品媒体 服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Service
public class ProductMediaServiceImpl extends ServiceImpl<ProductMediaMapper, ProductMedia> implements ProductMediaService {

    @Override
    public void deleteByKey(String key) {
        EntityWrapper<ProductMedia> wrapper=new EntityWrapper<>();
        wrapper.eq("img_key",key);
        this.delete(wrapper);
    }

    @Override
    public ProductMedia selectByKey(String key) {
        EntityWrapper<ProductMedia> wrapper=new EntityWrapper<>();
        wrapper.eq("img_key",key);
        List<ProductMedia> productMediaList = this.selectList(wrapper);
        if(StringUtil.isEmptyObj(productMediaList)){
            throw new BusinessException(ResultEnum.SERVER_ERROR.getCode(),"img_key不存在");
        }
        if(productMediaList.size()>1){
            throw new BusinessException(ResultEnum.SERVER_ERROR.getCode(),"数据异常 key必须唯一");
        }
        return productMediaList.get(0);
    }

    @Override
    public List<ProductMedia> selectByProductCode(String productCode) {
        EntityWrapper<ProductMedia> wrapper=new EntityWrapper<>();
        wrapper.eq("product_code",productCode);
        wrapper.orderBy("sort_index");
        List<ProductMedia> productMediaList = this.selectList(wrapper);
        return productMediaList;
    }
}
