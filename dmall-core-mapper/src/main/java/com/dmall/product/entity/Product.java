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
 * 商品表
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("p_product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品编码
     */
    @TableField("product_code")
    private String productCode;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 副名称
     */
    @TableField("sub_name")
    private String subName;
    /**
     * 商品类型
     */
    @TableField("product_type")
//    @ChangeColumn(beanName = "productTypeServiceImpl",methodName = "selectTypeName")
    private String productType;

    /**
     * 最高价
     */
    @TableField("max_price")
    private Double maxPrice;
    /**
     * 最低价
     */
    @TableField("min_price")
    private Double minPrice;
    /**
     * 销量
     */
    @TableField("sale_count")
    private Integer saleCount;
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
     * 评分
     */
    @TableField("comment_score")
    private Double commentScore;
    /**
     * 好评率
     */
    @TableField("good_rate")
    private Double goodRate;
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
     * 品牌
     */
    @TableField("brand_id")
    private Long brandId;
    /**
     * 主图
     */
    @TableField("main_image")
    private String mainImage;
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
