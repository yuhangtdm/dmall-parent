package com.dmall.plat.product.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author: yuhang
 * @date: 2018/10/14
 */
@Data
public class FullSkuDTO implements Serializable {

    @Valid
    @NotNull(message = "商品sku信息不能为空")
    private SkuDTO skuDTO;

    /**
     * sku图片信息
     */
    private List<String> imgVoArray;

    /**
     * sku属性值列表
     */
    private List<SkuPropertyDTO> skuPropertyList;
}
