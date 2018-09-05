package com.dmall.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: yuhang
 * @date: 2018/9/5
 */
@Data
public class Tree implements Serializable {

    private static final long serialVersionUID = 2134282994409164562L;

    private Long id;

    private String name;

    private Boolean open;

    private List<Tree>  children;


}
