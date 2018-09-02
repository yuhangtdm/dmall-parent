package com.dmall.common.exception;

import com.dmall.common.enums.ResultEnum;

/**
 * 自定义异常
 * @author: yuhang
 * @date: 2018/9/1
 */
public class BusinessException extends RuntimeException {
     private Integer errorCode;
     private String msg;

     public BusinessException(){
         super();
         this.errorCode=ResultEnum.UNKNOWN_ERROR.getCode();
         this.msg=ResultEnum.UNKNOWN_ERROR.getMsg();
     }

     public BusinessException(Integer code,String msg){
         super();
         this.errorCode=code;
         this.msg=msg;
     }

    public BusinessException(ResultEnum resultEnum,String msg){
        super();
        this.errorCode=resultEnum.getCode();
        this.msg=msg;
    }

    public BusinessException(ResultEnum resultEnum){
        super();
        this.errorCode=resultEnum.getCode();
        this.msg=resultEnum.getMsg();
    }

    public BusinessException(String message, Throwable cause){
        super(message,cause);
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getMsg() {
        return msg;
    }
}
