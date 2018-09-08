package com.dmall.plat.product.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: yuhang
 * @date: 2018/9/8
 */
@Data
public class ProductTypeDto implements Serializable {

    private static final long serialVersionUID = 7937211498799083802L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 类型名
     */
    @NotBlank(message = "类型名称不能为空")
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
    @NotNull(message = "等级不能为空")
    private Integer level;
    /**
     * 描述
     */
    private String description;
    /**
     * 排序号
     */
    @NotNull(message = "排序不能为空")
    private Integer sortIndex;

    /**
     * 分类标题（SEO）
     */
    private String seoTitle;
    /**
     * 分类关键字（SEO）
     */
    private String seoKeywords;

}
