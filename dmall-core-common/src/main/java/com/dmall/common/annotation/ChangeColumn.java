package com.dmall.common.annotation;

import java.lang.annotation.*;

/**
 * 主要转换列表中需要转换字典由 key得到value
 * @author: yuhang
 * @date: 2018/9/2
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ChangeColumn {
    /**
     * 转换对象中需要赋值的字段
     */
    String value() default "";

    /**
     * 数据字典时需要传入数据字典key
     * @return
     */
    String dictType()default "";


    /**
     * 查询的bean名称
     * @return
     */
    String beanName()default "";

    /**
     * 查询的方法名称 默认查询集合
     */
    String methodName()default "selectList";

    /**
     * 查询对象的id
     */
    String key()default "id";
    /**
     * 展示的字段名
     * @return
     */
    String display()default "dictName";


}
