package com.dmall.sys.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.sys.entity.Dict;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.dmall.sys.entity.DictValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典目录 Mapper 接口
 * </p>
 *
 * @author yuhang
 * @since 2018-09-03
 */
public interface DictMapper extends BaseMapper<Dict> {

    List<Map> dictPage(Page page, @Param("dict") Dict dict,@Param("dictValue") DictValue dictValue);

    List<Dict> selectDict();
}
