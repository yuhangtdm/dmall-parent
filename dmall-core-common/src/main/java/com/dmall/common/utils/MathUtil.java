package com.dmall.common.utils;

import java.math.BigDecimal;

/**
 * @author: yuhang
 * @date: 2018/10/17
 */
public class MathUtil {

    public static final int num=2;
    public static final int rounding=BigDecimal.ROUND_HALF_UP;

    /**
     * 加
     */
    public static Double add(Double a,Double b){
        BigDecimal add1=new BigDecimal(a);
        BigDecimal add2=new BigDecimal(b);
        return add1.add(add2).doubleValue();
    }

    /**
     * 减
     */
    public static Double sub(Double a,Double b){
        BigDecimal a1=new BigDecimal(a);
        BigDecimal b1=new BigDecimal(b);
        return a1.subtract(b1).doubleValue();
    }


    /**
     * 乘
     */
    public static Double mul(Double a,Double b){
        BigDecimal a1=new BigDecimal(a);
        BigDecimal b1=new BigDecimal(b);
        return a1.multiply(b1).doubleValue();
    }

    /**
     * 除
     */
    public static Double div(Double a,Double b){
        BigDecimal a1=new BigDecimal(a);
        BigDecimal b1=new BigDecimal(b);
        return a1.divide(b1).doubleValue();
    }

    /**
     * 格式化小数
     */
    public static Double format(Double a,int number, int roundingMode){
        BigDecimal a1=new BigDecimal(a);
        return a1.setScale(number,roundingMode).doubleValue();
    }

    /**
     * 保留两位小数
     */
    public static Double format(Double a, int roundingMode){
        BigDecimal a1=new BigDecimal(a);
        return a1.setScale(num,roundingMode).doubleValue();
    }

    /**
     * 默认四舍五入 保留两位小数
     */
    public static Double format(Double a){
        BigDecimal a1=new BigDecimal(a);
        return a1.setScale(num,rounding).doubleValue();
    }
}
