package com.dmall.product.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.product.entity.PropsGroup;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-09-12
 */
public interface PropsGroupService extends IService<PropsGroup> {

    Page pageList(PropsGroup group, Page page);

    void saveOrUpdate(PropsGroup group);
}
