package com.dmall.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author: yuhang
 * @date: 2018/9/3
 */
public class JsonUtil {

    public static String toJson(Object obj){
        if(obj==null){
            return "{}";
        }else {
            return JSONObject.toJSONString(obj);
        }
    }

    public static <T> T toBean(String json,Class<T> tClass){
        return JSON.parseObject(json,tClass);
    }
}
