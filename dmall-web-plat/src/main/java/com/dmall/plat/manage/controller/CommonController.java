package com.dmall.plat.manage.controller;

import com.dmall.common.utils.ReflectUtil;
import com.dmall.common.utils.SpringContextUtil;
import com.dmall.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:yuhang
 * @Date:2018/9/3
 */
@Controller
@RequestMapping("common")
public class CommonController {

    public static Logger logger= LoggerFactory.getLogger(CommonController.class);

    @RequestMapping("select")
    public List<Map<String,Object>> select(String dict,String bean){
        List<Map<String,Object>> result=new ArrayList<>();
        if(StringUtil.isNotBlank(dict)){

        }
        if(StringUtil.isNotBlank(bean)){
            Object obj = SpringContextUtil.getBean(bean);
            try {
                Method method = obj.getClass().getDeclaredMethod("list");
                Object invoke = method.invoke(obj);
                List data= (List) invoke;
                for (Object datum : data) {
                    Map<String, Object> beanToMap = ReflectUtil.beanToMap(datum);
                    result.add(beanToMap);
                }
            } catch (Exception e) {
                logger.error("反射遇到异常{}",e.getMessage());
                return result;
            }


        }
        return result;
    }
}
