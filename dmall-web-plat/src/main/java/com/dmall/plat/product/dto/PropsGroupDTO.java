package com.dmall.plat.product.dto;

import com.dmall.product.entity.Props;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:yuhang
 * @Date:2018/9/17
 */
@Data
public class PropsGroupDTO implements Serializable {

    private Long id;

    @NotBlank(message = "属性组名称不可为空")
    private String name;

    @NotBlank(message = "商品类型不可为空")
    private String productType;

    private String remark;

    private Integer isSale;

    private List<Props> propsList=new ArrayList<>();


}
