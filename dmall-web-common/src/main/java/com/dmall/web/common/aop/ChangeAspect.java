package com.dmall.web.common.aop;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理表格数据的切面类
 * 对于数据库里存了key,显示时展示value
 * 数据字典时 指定type 用于value的赋值
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

    /**
     * 处理集合数据
     */
    /*private void doHandle(List data) {
        try {
            for (Object datum : data) {
                Field[] declaredFields = datum.getClass().getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    declaredField.setAccessible(true);
                    Object key=declaredField.get(datum);
                    ChangeColumn annotation = declaredField.getAnnotation(ChangeColumn.class);
                    if(annotation!=null){
                        //转换的字段
                        String value = annotation.value();
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
                                Class cacheClass=annotation.cacheClass();//缓存类
                                String display=annotation.display();//要展示的字段
                                String className=cacheClass.getName();//类名称
                                String objJson= (String) redisTemplate.opsForValue().get(className+":"+key);
                                Object hValue=ReflectUtil.getValue(JsonUtil.toBean(objJson,cacheClass),display);
                                if(StringUtil.isEmptyObj(hValue)){
                                    Object bean = SpringContextUtil.getBean(annotation.beanName());//service
                                    Method method = bean.getClass().getDeclaredMethod(annotation.methodName());
                                    Object invoke=method.invoke(bean,key);
                                    hValue=ReflectUtil.getValue(invoke,display);
                                    redisTemplate.opsForValue().set(className+":"+key,JsonUtil.toJson(invoke));
                                }
                                changeField.set(datum,hValue);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){

        }

    }*/


    private void doHandle(List data) {
        try {
            Map<String,List> map=new HashMap<>();
            if(StringUtil.isNotEmptyObj(data)){
                Object obj = data.get(0);
                Field[] declaredFields = obj.getClass().getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    ChangeColumn changeColumn = declaredField.getAnnotation(ChangeColumn.class);
                    if(changeColumn!=null){
                        if(StringUtil.isNotEmptyObj(changeColumn.dictType())){
                            List<Dict> dicts = dictService.queryDictByType(changeColumn.dictType());
                            map.put(declaredField.getName(),dicts);
                        }else if(StringUtil.isNotEmptyObj(changeColumn.beanName())){
                            Object bean=SpringContextUtil.getBean(changeColumn.beanName());
                            Method method = bean.getClass().getMethod(changeColumn.methodName(), Wrapper.class);
                            Object invoke = method.invoke(obj, new EntityWrapper());
                            map.put(declaredField.getName(),(List)invoke);
                        }
                    }
                }
            }
            for (Object datum : data) {
                Field[] declaredFields = datum.getClass().getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    declaredField.setAccessible(true);
                    ChangeColumn changeColumn = declaredField.getAnnotation(ChangeColumn.class);
                    if(changeColumn!=null){
                        Object key=declaredField.get(datum);
                        String value = changeColumn.value();
                        Field changeField = datum.getClass().getDeclaredField(value);
                        changeField.setAccessible(true);
                        List list = map.get(declaredField.getName());
                        for (Object o : list) {
                            Field keyField = o.getClass().getDeclaredField(changeColumn.key());
                            Field displayValue = o.getClass().getDeclaredField(changeColumn.display());
                            if(key.equals(keyField.get(o))){
                                changeField.set(o,displayValue);
                                break;
                            }
                        }
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
