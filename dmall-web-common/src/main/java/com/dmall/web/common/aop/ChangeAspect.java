package com.dmall.web.common.aop;

import com.dmall.common.annotation.ChangeColumn;
import com.dmall.common.utils.JsonUtil;
import com.dmall.common.utils.ReflectUtil;
import com.dmall.common.utils.SpringContextUtil;
import com.dmall.common.utils.StringUtil;
import com.dmall.plat.sys.service.DictService;
import com.dmall.sys.entity.Dict;
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

    @Autowired
    private DictService dictService;
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
            List data = (List) result.getData();
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
                    Object key=declaredField.get(datum);
                    ChangeColumn annotation = declaredField.getAnnotation(ChangeColumn.class);
                    if(annotation!=null){
                        String value = annotation.value();//转换的字段
                        Field changeField = datum.getClass().getDeclaredField(value);
                        if(changeField!=null){
                            changeField.setAccessible(true);
                            if(!annotation.dictType().equals("")){
                                //数据字典
                                List<Dict> dicts = dictService.queryDictByType(annotation.dictType());
                                for (Dict dict : dicts) {
                                    if(dict.getDictCode().equals(String.valueOf(key))){
                                        changeField.set(datum,dict.getDictValue());
                                        break;
                                    }
                                }
                            }else if(!annotation.beanName().equals("")){
                               /* Class cacheClass=annotation.cacheClass();//缓存类
                                String display=annotation.display();//要展示的字段
                                String className=cacheClass.getName();//类名称
                                Object key=declaredField.get(datum);//key
                                String objJson= (String) redisTemplate.opsForValue().get(className+":"+key);
                                hValue=ReflectUtil.getValue(JsonUtil.toBean(objJson,cacheClass),display);*/
                                //数据库查询
                                Object bean = SpringContextUtil.getBean(annotation.beanName());//service
                                Method method = bean.getClass().getDeclaredMethod(annotation.methodName());
                                //查询到的值
                                Object invoke=method.invoke(bean,key);
                                changeField.set(datum,invoke);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){

        }

    }


}
