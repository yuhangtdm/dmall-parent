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
 * 商品评价
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("p_product_comment")
public class ProductComment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 商品ID
     */
    @TableField("product_id")
    private Long productId;
    /**
     * 评分
     */
    private Integer score;
    /**
     * 评论
     */
    private String comment;
    /**
     * 等级 0好评 1中评 2 差评
     */
    private Integer level;
    /**
     * 评论人id
     */
    @TableField("comment_user")
    private Long commentUser;
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
