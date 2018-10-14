package com.dmall.plat.product.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: yuhang
 * @date: 2018/10/14
 */
@Data
public class SkuDTO implements Serializable {

    /**
     * SKU 名称
     */
    @NotBlank(message = "sku名称不能为空")
    private String skuName;

    /**
     * 商品编码
     */
    @NotBlank(message = "商品编码不能为空")
    private String productCode;

    /**
     * 成本价
     */
    @NotNull(message = "成本价不能为空")
    private Double costPrice;

    /**
     * 市场价不能为空
     */
    @NotNull(message = "市场价不能为空")
    private Double marketPrice;

    /**
     * 实际价格
     */
    @NotNull(message = "优惠价不能为空")
    private Double price;

    /**
     * 可用库存
     */
    @NotNull(message = "可用库存不能为空")
    private Integer availableStock;

    /**
     * 商品状态 0-下架 1-上架
     */
    @NotNull(message = "商品状态不能为空")
    private  Integer state;

    /**
     * 上架时间
     */
    private Date onSaleTime;

    /**
     * sku简介
     */
    private String skuDescription;

    /**
     * sku图文详情
     */
    private String skuContent;


}
