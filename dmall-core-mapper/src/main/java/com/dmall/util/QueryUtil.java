package com.dmall.util;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dmall.common.utils.StringUtil;

import java.lang.reflect.Field;

/**
 * @author: yuhang
 * @date: 2018/9/1
 */
public class QueryUtil {

    /**
     * 查询表单的公共方法 模糊查询 页面上传递的参数 适合于单表
     */
    public static void queryForm(EntityWrapper wrapper,Object entity){
        try {
            if(entity!=null){
                Field[] declaredFields = entity.getClass().getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    declaredField.setAccessible(true);
                    Object field = declaredField.get(entity);
                    if(StringUtil.isNotEmptyObj(field)){
                        TableField annotation = declaredField.getAnnotation(TableField.class);
                        if(StringUtil.isNotEmptyObj(annotation)){
                            wrapper.and().like(annotation.value(),String.valueOf(field));
                        }

                    }
                }
            }
        }catch (Exception e){

        }

    }
}
