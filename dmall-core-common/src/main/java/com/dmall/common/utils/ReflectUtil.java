package com.dmall.common.utils;

import com.dmall.common.annotation.SelectKey;
import com.dmall.common.annotation.SelectValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author:yuhang
 * @Date:2018/9/3
 */
public class ReflectUtil {

    public static Logger logger= LoggerFactory.getLogger(ReflectUtil.class);

    /**
     * 对象转map
     * @param obj
     * @return
     */
    public static Map<String,Object> beanToMap(Object obj){
        Map<String,Object> map=new LinkedHashMap<>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        try {
            for (Field declaredField : declaredFields) {
                SelectKey selectKey = declaredField.getAnnotation(SelectKey.class);
                if(selectKey!=null){
                    declaredField.setAccessible(true);
                    Object o = declaredField.get(obj);
                    if(StringUtil.isNotEmptyObj(o)){
                        map.put("code",o);
                        continue;
                    }
                }
                SelectValue selectValue = declaredField.getAnnotation(SelectValue.class);
                if(selectValue!=null){
                    declaredField.setAccessible(true);
                    Object o = declaredField.get(obj);
                    if(StringUtil.isNotEmptyObj(o)){
                        map.put("value",o);
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("反射遇到异常{}",e.getMessage());
        }


        return map;
    }

    /**
     * 根据属性名获取对象的值
     */
    public static Object getValue(Object object,String propertyName){
        try {
            Field declaredField = object.getClass().getDeclaredField(propertyName);
            declaredField.setAccessible(true);
            Object o = declaredField.get(object);
            return o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
