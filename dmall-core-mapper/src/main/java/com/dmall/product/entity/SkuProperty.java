package com.dmall.product.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * SKU属性值
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@RequiredArgsConstructor
@TableName("p_sku_property")
public class SkuProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * SKU ID
     */
    @TableField("sku_id")
    private Long skuId;
    /**
     * 组id
     */
    @TableField("group_id")
    private Long groupId;

    @TableField("group_name")
    private String groupName;
    /**
     * 属性ID
     */
    @TableField("property_id")
    private Long propertyId;
    /**
     * 属性名称
     */
    @TableField("property_name")
    private String propertyName;
    /**
     * 选项ID
     */
    @TableField("option_id")
    private Long optionId;
    /**
     * 选项值
     */
    @TableField("option_value")
    private String optionValue;
    /**
     * sku图片
     */
    @TableField("sku_image")
    private String skuImage;
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


}
