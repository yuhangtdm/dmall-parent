package com.dmall.product.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.product.entity.PropsGroup;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yuhang
 * @since 2018-09-12
 */
public interface PropsGroupMapper extends BaseMapper<PropsGroup> {

    /**
     * 属性组 分页查询
     */
    List<Map<String,Object>> pageList(Page page, @Param("group") PropsGroup group);
}
