package com.dmall.plat.product.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

/**
 * @author: yuhang
 * @date: 2018/9/9
 */
@Data
public class PropsDTO implements Serializable {

    private static final long serialVersionUID = 4092276563120191943L;
    private Long id;
    /**
     * 属性名称
     */
    @NotBlank(message = "属性名称不能为空")
    private String propName;

    private Long groupId;

    private String remark;

    /**
     * 属性值
     */
    private List<String> propValues;
}
