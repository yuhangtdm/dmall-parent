package com.dmall.product.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.dmall.common.annotation.ChangeColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * SKU
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("p_sku")
public class Sku implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * SKU编码
     */
    @TableField("sku_code")
    private String skuCode;
    /**
     * Sku名称
     */
    @TableField("sku_name")
    private String skuName;
    /**
     * 商品编码
     */
    @TableField("product_code")
    private String productCode;
    /**
     * 市场价
     */
    @TableField("market_price")
    private Double marketPrice;
    /**
     * 优惠价
     */
    private Double price;
    /**
     * 成本价
     */
    @TableField("cost_price")
    private Double costPrice;
    /**
     * 销量
     */
    @TableField("sale_count")
    private Integer saleCount;
    /**
     * 排序
     */
    @TableField("sort_index")
    private Integer sortIndex;
    /**
     * 可用库存
     */
    @TableField("available_stock")
    private Integer availableStock;
    /**
     * 锁定库存
     */
    @TableField("frozen_stock")
    private Integer frozenStock;
    /**
     * 预览图
     */
    @TableField("sku_main_pic")
    private String skuMainPic;
    /**
     * SKU属性摘要
     */
    @TableField("sku_properties")
    private String skuProperties;
    /**
     * 浏览量
     */
    @TableField("view_count")
    private Integer viewCount;
    /**
     * 评论数
     */
    @TableField("comment_count")
    private Integer commentCount;
    /**
     * 好评数
     */
    @TableField("good_comment_count")
    private Integer goodCommentCount;
    /**
     * 中评数
     */
    @TableField("middle_comment_count")
    private Integer middleCommentCount;
    /**
     * 差评数
     */
    @TableField("bad_comment_count")
    private Integer badCommentCount;
    /**
     * 好评率
     */
    @TableField("good_rate")
    private Double goodRate;
    /**
     * 评分
     */
    private Double score;
    /**
     * 品牌id
     */
    @TableField("brand_id")
    private Long brandId;

    /**
     * 状态 0 下架 1 上架
     */
    @ChangeColumn(value = "stateName",dictType = "_product_state")
    private Integer state;
    /**
     * 上架时间
     */
    @TableField("on_sale_time")
    private Long onSaleTime;
    /**
     * 下架时间
     */
    @TableField("off_sale_time")
    private Long offSaleTime;

    /**
     * sku简介
     */
    @TableField("sku_description")
    private String skuDescription;

    /**
     * sku图文详情
     */
    @TableField("sku_content")
    private String skuContent;

    /**
     * 是否启用 0 否 1 是
     */
    @TableField("is_del")
    private Integer isDel;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

    @TableField(exist = false)
    private String stateName;


}
