package com.dmall.plat.product.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: yuhang
 * @date: 2018/9/2
 */
@Data
public class BrandDTO implements Serializable {

    private static final long serialVersionUID = -3731481667314987817L;

    private Long id;
    /**
     * 品牌名
     */
    @NotBlank(message = "品牌名称不能为空")
    private String name;
    /**
     * 英文名
     */
    private String englishName;

    /**
     * 品牌简介
     */
    private String description;

    /**
     * 商品分类ID
     */
    private String productType;

    /**
     * 品牌LOGO
     */
    private String logo;


}
