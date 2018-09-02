package com.dmall.web.common.utils;

import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;

/**
 * a拷贝到b  a的非null属性才进行拷贝
 * @author: yuhang
 * @date: 2018/9/2
 */
public class BeanUtilOne extends BeanUtilsBean {

    @Override
    public void copyProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
        if(value==null){
            return;
        }
        super.copyProperty(bean, name, value);
    }
}
