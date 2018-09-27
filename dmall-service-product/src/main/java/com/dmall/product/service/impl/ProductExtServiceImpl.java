package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dmall.common.utils.StringUtil;
import com.dmall.product.entity.ProductExt;
import com.dmall.product.mapper.ProductExtMapper;
import com.dmall.product.service.ProductExtService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品扩展 服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Service
public class ProductExtServiceImpl extends ServiceImpl<ProductExtMapper, ProductExt> implements ProductExtService {

    @Override
    public ProductExt selectByProductId(Long productId) {
        EntityWrapper<ProductExt> wrapper=new EntityWrapper<>();
        wrapper.eq("product_id",productId);
        List<ProductExt> productExts = this.selectList(wrapper);
        if(StringUtil.isNotEmptyObj(productExts)){
            return productExts.get(0);
        }
        return null;
    }
}
