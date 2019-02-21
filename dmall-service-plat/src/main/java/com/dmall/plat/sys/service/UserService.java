package com.dmall.plat.sys.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.sys.entity.User;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yuhang
 * @since 2019-02-21
 */
public interface UserService extends IService<User> {
    /**
     * 公共分页查询
     */
    Page pageList(User user, Page page);

    /**
     * 新增或保存用户
     */
    void saveOrUpdate(User user);
}
