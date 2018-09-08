package com.dmall.web.common.utils;

import com.dmall.common.utils.StringUtil;
import com.dmall.common.enums.ResultEnum;
import com.dmall.web.common.result.ReturnResult;

import java.util.List;

/**
 * @author: yuhang
 * @date: 2018/8/31
 */
public class ResultUtil {

    /**
     * 封装List数据
     */
    public static <T> ReturnResult buildResult(ResultEnum resultEnum, String message, Long count, List<T> data){
        ReturnResult returnResult =new ReturnResult();
        returnResult.setCode(resultEnum.getCode());
        if(StringUtil.isBlank(message)){
            message=resultEnum.getMsg();
        }
        if(count==null && data!=null){
            count=Long.valueOf(data.size());
        }
        returnResult.setMsg(message);
        returnResult.setCount(count);
        returnResult.setData(data);
        return returnResult;
    }

    /**
     * 封装List数据  msg用枚举内的 常用 用于分页
     */
    public static <T> ReturnResult buildResult(ResultEnum resultEnum,Long count,List<T> data){
        return buildResult(resultEnum,null,count,data);
    }

    /**
     * 封装List数据  msg用枚举内的 count取集合长度
     */
    public static <T> ReturnResult buildResult(ResultEnum resultEnum, List<T> data){
       return buildResult(resultEnum,null,null,data);
    }

    /**
     * 封装普通数据
     */
    public static  ReturnResult buildResult(ResultEnum resultEnum,String msg, Object data){
        ReturnResult returnResult =new ReturnResult();
        returnResult.setCode(resultEnum.getCode());
        if(StringUtil.isBlank(msg)){
            msg=resultEnum.getMsg();
        }
        returnResult.setMsg(msg);
        returnResult.setData(data);
        return returnResult;
    }

    /**
     * 封装普通数据 常用
     */
    public static  ReturnResult buildResult(ResultEnum resultEnum, Object data){
        return buildResult(resultEnum,null,data);
    }

    /**
     * 封装 code和msg
     */
    public static <T> ReturnResult buildResult(Integer code, String msg){
        ReturnResult returnResult =new ReturnResult();
        returnResult.setCode(code);
        returnResult.setMsg(msg);
        return returnResult;
    }

    /**
     * 只封装结果
     */
    public static ReturnResult buildResult(ResultEnum resultEnum){
        return buildResult(resultEnum,null,null,null);
    }
}
