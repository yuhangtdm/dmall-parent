package com.dmall.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author: yuhang
 * @date: 2018/8/31
 */
public class StringUtil extends StringUtils {

    /**
     * 按照规则判断对象是否为空
     */
    public static boolean isEmptyObj(Object obj){
        if (obj==null){
            return true;
        }
        if (obj instanceof String){
            if(StringUtil.isBlank((String)obj)){
                return true;
            }
        }
        if (obj instanceof Collection){
            Collection collection= (Collection) obj;
            if(collection.size()==0){
                return true;
            }
            for (Object o : collection) {
                if(o!=null){
                    return false;
                }
            }
            return true;
        }
        if(obj instanceof Map){
            Map map= (Map) obj;
            if(map.size()==0){
                return true;
            }
        }
        if(obj.getClass().isArray()){
            if(Array.getLength(obj)==0){
                return true;
            }
        }
        return false;
    }

    /**
     * 按照规则判断对象是否不为空
     */
    public static boolean isNotEmptyObj(Object obj){
        return !isEmptyObj(obj);
    }

    /**
     * 一组数据有一个为空返回为true
     * @param obj
     * @return
     */
    public static boolean isEmptyObj(Object... obj){
        for (Object o : obj) {
            if(isEmptyObj(o)){
                return true;
            }
        }
        return false;
    }

    /**
     * 一组数据都不为空 则返回为true
     * @param obj
     * @return
     */
    public static boolean isNotEmptyObj(Object... obj){
        for (Object o : obj) {
            if(isEmptyObj(o)){
                return false;
            }
        }
        return true;
    }



}
