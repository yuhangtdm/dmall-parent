package com.dmall.product.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.product.entity.PropsGroup;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-09-12
 */
public interface PropsGroupService extends IService<PropsGroup> {

    /**
     * 属性组分页
     */
    Page pageList(PropsGroup group, Page page);

    /**
     * 新增或修改
     */
    void saveOrUpdate(PropsGroup group);

    /**
     * 属性组列表
     */
    List<PropsGroup> listAll(String productTypeId);

    /**
     * 根据商品分类删除属性组
     */
    void deleteByProductType(String type);

    /**
     * 删除属性组下的属性及属性值
     */
    void deleteObj(Long id);

    /**
     * 事务测试方法
     */
    void txTest(PropsGroup group);
}
