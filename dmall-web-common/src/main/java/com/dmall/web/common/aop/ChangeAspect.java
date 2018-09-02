package com.dmall.web.common.aop;

import com.dmall.common.annotation.ChangeColumn;
import com.dmall.web.common.result.ReturnResult;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author: yuhang
 * @date: 2018/9/2
 */
@Aspect
@Component
public class ChangeAspect {
    /*
     * 定义一个切入点
     */
    @Pointcut("@annotation(com.dmall.common.annotation.TransBean)")
    public void ip() {
    }

    @AfterReturning(returning = "ret", pointcut = "ip()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        System.out.println("方法处理前: " + ret);
        ReturnResult result= (ReturnResult) ret;
        if(result.getData()!=null){
            List data = result.getData();
            for (Object datum : data) {
                Field[] declaredFields = datum.getClass().getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    ChangeColumn annotation = declaredField.getAnnotation(ChangeColumn.class);
                    if(annotation!=null){
                        String value = annotation.value();
                        Field changeField = datum.getClass().getDeclaredField(value);
                        if(changeField!=null){
                            changeField.setAccessible(true);
                            changeField.set(datum,"测试产品类型");
                            changeField.setAccessible(false);
                        }
                    }
                }
            }
        }
        System.out.println("方法处理后: " + ret);
    }

}
