package com.dmall.product.mapper;

import com.alibaba.fastjson.JSONObject;
import com.dmall.product.entity.ProductProperty;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品属性集 Mapper 接口
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface ProductPropertyMapper extends BaseMapper<ProductProperty> {

    List<Map<String,Object>> queryByParam(@Param("productCode") String productCode);

    List<JSONObject> queryGroupByProductCode(@Param("productCode") String productCode);

    List<JSONObject> queryPropsByProductCode(@Param("productCode") String productCode,@Param("groupId") String groupId);

}
