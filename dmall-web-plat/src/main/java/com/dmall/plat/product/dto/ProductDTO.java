package com.dmall.plat.product.dto;

import com.dmall.product.entity.Product;
import lombok.Data;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品基本属性
 * @author: yuhang
 * @date: 2018/9/27
 */
@Data
public class ProductDTO implements Serializable {

    private static final long serialVersionUID = 8387231244000395070L;

    private Long id;

    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    private String name;
    /**
     * 商品副名称
     */
    @NotBlank(message = "商品副名称不能为空")
    private String subName;
    /**
     * 商品描述 ext
     */
    private String description;
    /**
     * 商品类型
     */
    @NotBlank(message = "商品类型不能为空")
    private String productType;
    /**
     * 品牌id
     */
    @NotNull(message = "品牌不能为空")
    private Long brandId;

    /**
     * 商品状态
     */
    @NotNull(message = "商品状态不能为空")
    private Integer status;

    /**
     * 上市时间
     */
    private Date onCityTime;

    /**
     * 商品编码
     */
    private String productCode;


}
