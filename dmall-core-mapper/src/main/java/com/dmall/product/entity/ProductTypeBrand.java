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
@TableName("p_product_type_brand")
public class ProductTypeBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("product_type_id")
    private Long productTypeId;
    @TableField("brand_id")
    private Long brandId;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;


}
