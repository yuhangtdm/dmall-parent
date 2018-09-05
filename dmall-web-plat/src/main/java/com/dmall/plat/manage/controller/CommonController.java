package com.dmall.plat.manage.controller;

import com.dmall.common.utils.ReflectUtil;
import com.dmall.common.utils.SpringContextUtil;
import com.dmall.common.utils.StringUtil;
import com.dmall.plat.sys.service.DictService;
import com.dmall.sys.entity.Dict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @Autowired
    private DictService dictService;

    public static Logger logger= LoggerFactory.getLogger(CommonController.class);

    @RequestMapping("select")
    @ResponseBody
    public List<Map<String,Object>> select(String dict,String bean,String methodName){
        List<Map<String,Object>> result=new ArrayList<>();
        if(StringUtil.isNotBlank(dict)){
            List<Dict> dicts = dictService.queryDictByType(dict);
            for (Dict dict1 : dicts) {
                Map<String, Object> beanToMap = ReflectUtil.beanToMap(dict1);
                result.add(beanToMap);
            }
            return result;

        }
        if(StringUtil.isNotBlank(bean)){
            Object obj = SpringContextUtil.getBean(bean);
            if(obj!=null){
                try {
                    if(StringUtil.isBlank(methodName)){
                        methodName="list";
                    }
                    Method method = obj.getClass().getDeclaredMethod(methodName);
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
            }else {
                logger.error("spring工厂内没有该bean:{}",bean);
            }



        }
        return result;
    }
}
