package com.dmall.service;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dmall.sys.entity.Dict;
import com.dmall.util.QueryUtil;

import java.lang.reflect.Field;

/**
 * 添加业务方法
 * @author:yuhang
 * @Date:2019/2/21
 */
public class BusinessServiceImpl<T> extends ServiceImpl{

    /**
     * 公共的分页查询方法
     * @param t
     * @param page
     * @return
     */
    public Page pageList(T t, Page page) {
        EntityWrapper wrapper=new EntityWrapper();
        QueryUtil.queryForm(wrapper,t);
        wrapper.orderBy("update_time",false);
        page=this.selectPage(page);
        return page;
    }

    /**
     * 公共的新增或更新方法
     * @param t
     */
    public void saveOrUpdate(T t){
        try {
            Field idField = t.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object id = idField.get(t);
            if(id!=null){
                this.updateById(t);
            }else {
                this.insert(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
