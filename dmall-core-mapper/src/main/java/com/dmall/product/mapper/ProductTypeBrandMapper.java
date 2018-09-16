package com.dmall.product.mapper;

import com.dmall.product.entity.ProductTypeBrand;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yuhang
 * @since 2018-09-12
 */
public interface ProductTypeBrandMapper extends BaseMapper<ProductTypeBrand> {

    void batchInsert1(@Param("brandId") Long brandId,@Param("typeIds") List<Long> typeIds);

    void batchInsert2(@Param("brandIds") List<Long> brandIds,@Param("typeId") Long typeId);
}
