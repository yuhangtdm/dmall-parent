package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.common.utils.StringUtil;
import com.dmall.product.entity.SkuMedia;
import com.dmall.product.mapper.SkuMediaMapper;
import com.dmall.product.service.SkuMediaService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * sku媒体 服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-10-14
 */
@Service
public class SkuMediaServiceImpl extends ServiceImpl<SkuMediaMapper, SkuMedia> implements SkuMediaService {

    @Override
    public SkuMedia selectByKey(String key) {
        EntityWrapper<SkuMedia> wrapper=new EntityWrapper<>();
        wrapper.eq("img_key",key);
        List<SkuMedia> skuMedia = this.selectList(wrapper);
        if(StringUtil.isEmptyObj(skuMedia)){
            throw new BusinessException(ResultEnum.SERVER_ERROR,"key不存在");
        }
        if(skuMedia.size()>1){
            throw new BusinessException(ResultEnum.SERVER_ERROR,"key必须唯一");
        }
        return skuMedia.get(0);
    }

    @Override
    public void deleteByKey(String key) {
        EntityWrapper<SkuMedia> wrapper=new EntityWrapper<>();
        wrapper.eq("img_key",key);
        this.delete(wrapper);
    }

    @Override
    public List<SkuMedia> selectBySkuCode(String skuCode) {
        EntityWrapper<SkuMedia> wrapper=new EntityWrapper<>();
        wrapper.eq("sku_code",skuCode);
        List<SkuMedia> skuMedia = this.selectList(wrapper);
        return skuMedia;
    }
}
