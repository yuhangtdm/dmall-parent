package com.dmall.product.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.product.entity.Props;
import com.baomidou.mybatisplus.service.IService;
import com.dmall.product.entity.PropsGroup;

import java.util.List;

/**
 * <p>
 * 商品属性 服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface PropsService extends IService<Props> {

    Page pageList(Props props, Page page);

    void saveOrUpdate(Props props, List<String> propValues);

    List<Props> listAll(String productTypeId, Long groupId);

    /**
     * 根据商品分类删除属性
     */
    void deleteByProductType(String type);

    /**
     * 根据属性组删除属性
     * @param id
     */
    void deleteByGroupId(Long id);

    /**
     * 删除属性及其属性值
     */
    void deleteObj(Long id);

    /**
     * 修改商品分类
     */
    void updateByGroup(PropsGroup group);

    /**
     * 事务测试
     * @param group
     */
    void txTest(PropsGroup group);
}
