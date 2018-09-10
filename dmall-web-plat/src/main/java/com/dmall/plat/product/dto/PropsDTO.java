package com.dmall.plat.product.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: yuhang
 * @date: 2018/9/9
 */
@Data
public class PropsDTO implements Serializable {

    private Long id;
    /**
     * 属性名称
     */
    private String propName;
    /**
     * 商品分类
     */
    private Long productType;

    /**
     * 备注
     */
    private String remark;
}
