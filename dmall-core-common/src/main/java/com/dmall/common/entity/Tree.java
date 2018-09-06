package com.dmall.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 树的基类
 * @author: yuhang
 * @date: 2018/9/5
 */
@Data
@Accessors(chain = true)
public class Tree implements Serializable {

    private static final long serialVersionUID = 2134282994409164562L;

    private Long id;

    private String name;

    private Long pid;


    private Boolean open=false;

    private Boolean isParent=false;


}
