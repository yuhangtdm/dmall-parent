package com.dmall.product.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yuhang
 * @since 2018-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("p_props_group")
public class PropsGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 组名称
     */
    @TableField("name")
    private String name;
    /**
     * 商品分类id 一级/二级/三级
     */
    @TableField("product_type")
    private String productType;
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

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序
     */
    private Integer sortIndex;

}
