package com.dmall.product.mapper;

import com.dmall.product.entity.Brand;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 品牌表 Mapper 接口
 * </p>
 *
 * @author yuhang
 * @since 2018-08-31
 */
public interface BrandMapper extends BaseMapper<Brand> {

    List<Brand> listAll(@Param("productType") Long productType);
}
