package com.dmall.plat.product.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author:yuhang
 * @Date:2018/10/4
 */
@Data
public class FullProductDTO implements Serializable {
    /**
     * 商品的完整对象
     */
    private static final long serialVersionUID = 7850485968698279159L;

    @Valid
    @NotNull(message = "商品基本信息不能为空")
    private ProductDTO product;

    @Valid
    @NotNull(message = "商品扩展信息不能为空")
    private ProductExtDTO productExt;


    private List<PropsGroupDTO> propsVoList;

}
