package com.dmall.common.enums;

/**
 * @author: yuhang
 * @date: 2018/10/13
 */
public enum MediaEnum {
   // 0-图片集 1-主图 2-视频
    IMAGE(0,"图片集"),
    MAIN_IMAGE(1,"主图"),
    VIDIO(400,"视频"),
    ;
    private Integer code;
    private String msg;
    private MediaEnum(Integer code,String msg){
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
