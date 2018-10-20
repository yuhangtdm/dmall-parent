package com.dmall.product.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.product.entity.Sku;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * SKU Mapper 接口
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface SkuMapper extends BaseMapper<Sku> {

    List<Map<String,Object>> pageList(Page page, Map<String,Object> param);
}
