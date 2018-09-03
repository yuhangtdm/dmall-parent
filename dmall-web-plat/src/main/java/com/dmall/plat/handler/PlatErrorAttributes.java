package com.dmall.plat.handler;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 遇到异常返回的json数据
 * @author: yuhang
 * @date: 2018/9/1
 */
@Component
public class PlatErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
//        Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
        Map<String,Object> errorAttributes=new LinkedHashMap<>();
        if(requestAttributes.getAttribute("data",RequestAttributes.SCOPE_REQUEST)!=null){
            errorAttributes.putAll((Map<? extends String, ?>) requestAttributes.getAttribute("data",RequestAttributes.SCOPE_REQUEST));
        }else{
            errorAttributes=super.getErrorAttributes(requestAttributes,includeStackTrace);
        }
        return errorAttributes;
    }
}
