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
 * 商品属性
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("p_props")
public class Props implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 属性名称
     */
    @TableField("name")
    private String name;
    /**
     * 组id
     */
    @TableField("group_id")
    private Long groupId;
    /**
     * 商品分类 一级/二级/三级
     */
    @TableField("product_type")
    private String productType;
    /**
     * 输入模式 0-文本框 1-下拉列表 2-复选框
     */
    @TableField("input_mode")
    private Integer inputMode;
    /**
     * 输入类型 0-文本内容 1-数字  2-日期
     */
    @TableField("input_type")
    private Integer inputType;
    /**
     * 验证规则 数据验证的正则表达式，如：^[A-Za-z]+$ 
     */
    @TableField("validate_pattern")
    private String validatePattern;
    /**
     * 备注
     */
    private String remark;
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

  /*  @TableField(exist = false)
    private String optionValues;*/

}
