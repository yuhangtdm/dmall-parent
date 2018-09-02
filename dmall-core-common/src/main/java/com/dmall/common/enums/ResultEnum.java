package com.dmall.common.enums;

/**
 * @author: yuhang
 * @date: 2018/8/31
 */
public enum  ResultEnum {
    SUCC(0,"请求成功"),
    UNKNOWN_ERROR(-1,"未知错误"),
    BAD_REQUEST(400,"客户端请求错误"),
    SERVER_ERROR(500,"服务端错误")
    ;
    private Integer code;
    private String msg;
    private ResultEnum(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
