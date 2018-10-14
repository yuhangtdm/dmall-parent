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
 * sku媒体
 * </p>
 *
 * @author yuhang
 * @since 2018-10-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("p_sku_media")
public class SkuMedia implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品code
     */
    @TableField("product_code")
    private String productCode;
    /**
     * skuCode
     */
    @TableField("sku_code")
    private String skuCode;
    /**
     * 媒体类型：0-图片集 1-主图 2-视频
     */
    @TableField("media_type")
    private Integer mediaType;
    /**
     * 排序号
     */
    @TableField("sort_index")
    private Integer sortIndex;
    /**
     * 资源key
     */
    @TableField("img_key")
    private String imgKey;
    /**
     * 资源地址
     */
    @TableField("img_url")
    private String imgUrl;
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
