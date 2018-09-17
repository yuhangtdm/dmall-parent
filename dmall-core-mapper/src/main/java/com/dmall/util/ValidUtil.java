package com.dmall.util;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.common.utils.SpringContextUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 唯一性校验
 * @author:yuhang
 * @Date:2018/9/17
 */
public class ValidUtil{

    public static <T> boolean valid(T t,String beanName,String... props){
        try {
            EntityWrapper<T> wrapper=new EntityWrapper<>();
            for (String prop : props) {
                Field field = t.getClass().getDeclaredField(prop);
                field.setAccessible(true);
                TableField annotation = field.getAnnotation(TableField.class);
                if(annotation!=null){
                    wrapper.eq(annotation.value(),field.get(t));
                }else {
                    wrapper.eq(prop,field.get(t));
                }
            }
            Object bean = SpringContextUtil.getBean(beanName);
            Method selectList = bean.getClass().getMethod("selectList",Wrapper.class);
            List<T> list= (List<T>) selectList.invoke(bean,wrapper);
            if(list==null || list.size()==0){
                return true;
            }else {
                Field idField = t.getClass().getDeclaredField("id");
                Object id = idField.get(t);
                T t2 = list.get(0);
                Field listField = t2.getClass().getDeclaredField("id");
                Object id2=listField.get(t2);
                if(id!=null && id.equals(id2)){
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(ResultEnum.SERVER_ERROR,"校验出现异常:");
        }

        return false;
    }
}
