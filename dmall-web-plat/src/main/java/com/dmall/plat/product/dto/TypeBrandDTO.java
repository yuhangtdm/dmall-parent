package com.dmall.plat.product.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: yuhang
 * @date: 2018/9/10
 */
@Data
public class TypeBrandDTO implements Serializable {

    @NotBlank(message = "商品类型id不可为空")
    private String typeIds;

    @NotBlank(message = "品牌id不可为空")
    private String brandIds;

}
