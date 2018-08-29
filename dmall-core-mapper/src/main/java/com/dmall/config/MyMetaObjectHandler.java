package com.dmall.config;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

public class MyMetaObjectHandler extends MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Object testType = getFieldValByName("createTime", metaObject);
        if (testType == null) {
            setFieldValByName("createTime", System.currentTimeMillis(), metaObject);//mybatis-plus版本2.0.9+
        }
        Object testType2 = getFieldValByName("updateTime", metaObject);
        if (testType2 == null) {
            setFieldValByName("updateTime", System.currentTimeMillis(), metaObject);//mybatis-plus版本2.0.9+
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("updateTime", System.currentTimeMillis(), metaObject);//mybatis-plus版本2.0.9+
    }
}
