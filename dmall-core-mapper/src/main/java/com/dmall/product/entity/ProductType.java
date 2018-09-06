package com.dmall.product.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotations.Version;

import com.dmall.common.entity.Tree;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品类型
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("p_product_type")
public class ProductType  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 类型名
     */
    private String name;
    /**
     * 父ID
     */
    private Long pid;
    /**
     * 图标
     */
    private String logo;
    /**
     * 等级
     */
    private Integer level;
    /**
     * 描述
     */
    private String description;
    /**
     * 排序号
     */
    @TableField("sort_index")
    private Integer sortIndex;
    /**
     * 路径
     */
    private String path;
    /**
     * 商品数量
     */
    @TableField("total_count")
    private Integer totalCount;
    /**
     * 分类标题（SEO）
     */
    @TableField("seo_title")
    private String seoTitle;
    /**
     * 分类关键字（SEO）
     */
    @TableField("seo_keywords")
    private String seoKeywords;
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
    private List<ProductType> children=new ArrayList<>();

    @TableField(exist = false)
    private Boolean open=false;

    @TableField(exist = false)
    private Boolean isParent=false;
}
