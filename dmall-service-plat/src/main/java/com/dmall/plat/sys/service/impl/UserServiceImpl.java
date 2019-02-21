package com.dmall.plat.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dmall.plat.sys.service.UserService;
import com.dmall.sys.entity.User;
import com.dmall.sys.mapper.UserMapper;
import com.dmall.util.QueryUtil;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2019-02-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{
    /**
     * 公共的分页查询方法
     */
    public Page pageList(User user, Page page) {
        EntityWrapper wrapper=new EntityWrapper();
        QueryUtil.queryForm(wrapper,user);
        wrapper.orderBy("update_time",false);
        page=this.selectPage(page);
        return page;
    }

    /**
     * 公共的新增或更新方法
     */
    public void saveOrUpdate(User user){
        if(user.getId()!=null){
            this.updateById(user);
        }else {
            this.insert(user);
        }

    }

}
