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
     * 数据源在spring工厂中的名称
     * @return
     */
    String beanName()default "";

    /**
     * 查询数据源的方法名称
     */
    String methodName()default "selectById";

    /**
     * 数据字典时需要传入数据字典key
     * @return
     */
    String dictType()default "";

}
