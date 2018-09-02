package com.dmall.web.common.utils;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * a拷贝到b b对象的属性为null的才可以拷贝
 * @author: yuhang
 * @date: 2018/9/2
 */
public class BeanUtilTwo extends BeanUtilsBean {
    @Override
    public void copyProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
        try {
            Object simpleProperty = PropertyUtils.getSimpleProperty(bean, name);
            if(simpleProperty==null){
                super.copyProperty(bean,name,value);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
