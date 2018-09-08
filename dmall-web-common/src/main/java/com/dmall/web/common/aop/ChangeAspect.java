package com.dmall.web.common.aop;

import com.dmall.common.annotation.ChangeColumn;
import com.dmall.common.utils.JsonUtil;
import com.dmall.common.utils.ReflectUtil;
import com.dmall.common.utils.SpringContextUtil;
import com.dmall.common.utils.StringUtil;
import com.dmall.web.common.result.ReturnResult;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: yuhang
 * @date: 2018/9/2
 */
@Aspect
@Component
public class ChangeAspect {

    @Autowired
    private RedisTemplate redisTemplate;
    /*
     * 定义一个切入点
     */
    @Pointcut("@annotation(com.dmall.common.annotation.TransBean)")
    public void ip() {
    }

    @AfterReturning(returning = "ret", pointcut = "ip()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        ReturnResult result= (ReturnResult) ret;
        if(result.getData()!=null){
            List data = result.getData();
            doHandle(data);

        }
    }

    private void doHandle(List data) {
        try {
            /**
             * 遍历数据
             */
            for (Object datum : data) {
                Field[] declaredFields = datum.getClass().getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    declaredField.setAccessible(true);
                    ChangeColumn annotation = declaredField.getAnnotation(ChangeColumn.class);

                    if(annotation!=null){
                        String value = annotation.value();//要转换的字段
                        Field changeField = datum.getClass().getDeclaredField(value);
                        if(changeField!=null){
                            changeField.setAccessible(true);
                            Object hValue="";
                            if(!annotation.dictType().equals("")){
                                //数据字典
                            }else if(!annotation.beanName().equals("")){
                                Class cacheClass=annotation.cacheClass();
                                String display=annotation.display();
                                String className=cacheClass.getName();
                                Object key=declaredField.get(datum);
                                String objJson= (String) redisTemplate.opsForValue().get(className+":"+key);
                                hValue=ReflectUtil.getValue(JsonUtil.toBean(objJson,cacheClass),display);
                                if(StringUtil.isEmptyObj(hValue)){
                                    //数据库查询
                                    Object bean = SpringContextUtil.getBean(annotation.beanName());//service
                                    Method method = bean.getClass().getDeclaredMethod(annotation.methodName());
                                    //查询到的对象
                                    Object invoke=method.invoke(bean,key);
                                    if(invoke!=null){
                                        redisTemplate.opsForValue().set(className+":"+key,JsonUtil.toJson(invoke));
                                        hValue=ReflectUtil.getValue(invoke,annotation.display());
                                    }
                                }
                            }
                            changeField.set(datum,hValue);
                        }
                    }
                }
            }
        }catch (Exception e){

        }

    }


}
