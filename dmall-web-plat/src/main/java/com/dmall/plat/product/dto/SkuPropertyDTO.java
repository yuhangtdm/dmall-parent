package com.dmall.plat.product.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: yuhang
 * @date: 2018/10/16
 */
@Data
public class SkuPropertyDTO implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * SKU ID
     */
    private Long skuId;
    /**
     * 组id
     */
    private Long groupId;
    /**
     * 组名称
     */
    private String groupName;
    /**
     * 属性ID
     */
    private Long propertyId;
    /**
     * 属性名称
     */
    private String propertyName;
    /**
     * 选项ID
     */
    private Long optionId;
    /**
     * 选项值
     */
    private String optionValue;
    /**
     * 是否需要配图
     */
    private Integer needPic;


}
