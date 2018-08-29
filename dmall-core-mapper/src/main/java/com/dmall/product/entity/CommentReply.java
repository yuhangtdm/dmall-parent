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
 * 评论回复表
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("p_comment_reply")
public class CommentReply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 订单id
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 评论id
     */
    @TableField("comment_id")
    private Long commentId;
    /**
     * 回复内容
     */
    @TableField("reply_content")
    private String replyContent;
    /**
     * 回复人
     */
    @TableField("reply_user")
    private Long replyUser;
    /**
     * 回复人姓名
     */
    @TableField("reply_user_name")
    private String replyUserName;
    /**
     * 回复时间
     */
    @TableField("reply_time")
    private Long replyTime;
    /**
     *  被回复人
     */
    @TableField("cover_reply_user")
    private Long coverReplyUser;
    /**
     * 被回复人姓名
     */
    @TableField("cover_reply_user_name")
    private String coverReplyUserName;
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
