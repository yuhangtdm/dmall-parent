package com.dmall.plat.product.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: yuhang
 * @date: 2018/9/10
 */
@Data
public class TypeBrandDTO implements Serializable {

    @NotNull(message = "商品类型id不可为空")
    private Long[] typeId;

    @NotNull(message = "品牌id不可为空")
    private Long[] brandIds;

}
