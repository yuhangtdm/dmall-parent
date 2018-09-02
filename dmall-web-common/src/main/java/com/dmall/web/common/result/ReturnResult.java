package com.dmall.web.common.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * layui分页返回实体 接口返回
 *
 * @author: yuhang
 * @date: 2018/8/31
 */
@Data
public class ReturnResult<T> implements Serializable {

    private static final long serialVersionUID = -5061242067855702147L;

    /**
     * 返回码
     */
    private Integer code;
    /**
     * 返回消息
     */
    private String msg;

    /**
     * 详细信息
     */
    private String summary;
    /**
     * 返回条数
     */
    private Long count;
    /**
     * 返回数据
     */
    private List<T> data;


}
