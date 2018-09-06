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

    public static <T> ReturnResult buildResult(ResultEnum resultEnum, String message, Long count, List<T> data){
        ReturnResult returnResult =new ReturnResult();
        returnResult.setCode(resultEnum.getCode());
        if(StringUtil.isBlank(message)){
            message=resultEnum.getMsg();
        }
        if(count==null){
            count=Long.valueOf(data.size());
        }
        returnResult.setMsg(message);
        returnResult.setCount(count);
        returnResult.setData(data);
        return returnResult;
    }


    public static <T> ReturnResult buildResult(ResultEnum resultEnum,Long count,List<T> data){
        return buildResult(resultEnum,null,count,data);
    }

    public static <T> ReturnResult buildResult(ResultEnum resultEnum, List<T> data){
       return buildResult(resultEnum,null,null,data);
    }

    public static ReturnResult buildResult(ResultEnum resultEnum){
        return buildResult(resultEnum,null,null,null);
    }
}
