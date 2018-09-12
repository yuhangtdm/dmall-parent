package com.dmall.product.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;


import com.dmall.common.annotation.ChangeColumn;
import com.dmall.common.annotation.SelectKey;
import com.dmall.common.annotation.SelectValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 品牌表
 * </p>
 *
 * @author yuhang
 * @since 2018-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("p_brand")
public class Brand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @SelectKey
    private Long id;
    /**
     * 品牌名
     */
    @TableField("brand_name")
    @SelectValue
    private String brandName;
    /**
     * 英文名
     */
    @TableField("english_name")
    private String englishName;
    /**
     * 首字母
     */
    @TableField("first_letter")
    private String firstLetter;
    /**
     * 品牌简介
     */
    @ChangeColumn("productTypeCn")
    private String description;

    /**
     * 品牌LOGO
     */
    private String logo;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;
    /**
     * 创建时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;


    /**
     * 多个商品分类ID
     */
    @TableField(value = "product_type")
    private String productType;
}
