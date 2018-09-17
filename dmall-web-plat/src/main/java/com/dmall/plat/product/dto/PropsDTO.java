package com.dmall.plat.product.dto;

import lombok.Data;

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
    private String propName;

    /**
     * 属性值
     */
    private String[] propValues;
}
