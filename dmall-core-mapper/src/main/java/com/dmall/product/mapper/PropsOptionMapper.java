package com.dmall.product.mapper;

import com.alibaba.fastjson.JSONObject;
import com.dmall.product.entity.PropsOption;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 属性选项 Mapper 接口
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface PropsOptionMapper extends BaseMapper<PropsOption> {

    List<JSONObject> queryOptionsByPropsId(Long propsId);
}
