package com.dmall.product.service;

import com.alibaba.fastjson.JSONObject;
import com.dmall.product.entity.PropsGroup;
import com.dmall.product.entity.PropsOption;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 属性选项 服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface PropsOptionService extends IService<PropsOption> {

    List<PropsOption> selectByPropId(Long id);

    void batchDelete(Long id, List<String> delete);

    List<JSONObject> queryOptionsByPropsId(Long propsId);

    void deleteByProductType(String type);

    /**
     * 根据属性组删除属性值
     */
    void deleteByGroupId(Long groupId);

    /**
     * 根据属性删除属性值
     */
    void deleteByPropertyId(Long id);

    /**
     * 修改商品分类
     */
    void updateByGroup(PropsGroup group);
}
