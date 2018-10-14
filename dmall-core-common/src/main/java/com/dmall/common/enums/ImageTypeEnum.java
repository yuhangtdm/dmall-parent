package com.dmall.common.enums;

/**
 * @author: yuhang
 * @date: 2018/10/14
 */
public enum ImageTypeEnum {
    PRODUCT("product","商品图片"),
    SKU("sku","SKU图片"),
    BRAND("brand","品牌logo"),
    PROPERTY("property","商品属性图片"),
    ;
    private String code;
    private String msg;

    private ImageTypeEnum(String code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public String
    getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
