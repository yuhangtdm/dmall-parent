package com.dmall.plat.product.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:yuhang
 * @Date:2018/10/4
 */
@Data
public class ProductExtDTO implements Serializable {
    /**
     * 商品扩展信息
     */
    private static final long serialVersionUID = -5891392877187505974L;

    private Long id;

    /**
     * 富文本内容
     */
    private String richContent;
}
