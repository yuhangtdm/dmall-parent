package com.dmall.web.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dmall.common.utils.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author:yuhang
 * @Date:2019/3/29
 */
public class BeanUtil extends BeanUtils {

    /**
     *  将源实体类转化为目标实体类
     *  拷贝属性方法
     */
    public static <T> T transform(Class<T> clazz,Object src){
        if (src==null){
            return null;
        }
        T instance = null;
        try {
            instance = clazz.newInstance();
            copyProperties(instance,src,getNullPropertiesNames(src));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return instance;
    }

    /**
     * 属性的拷贝 只拷贝src中非null的属性
     */
    public static Object transform(Object src,Object dest){
        if (src == null || dest ==null){
            return null;
        }
        copyProperties(src,dest,getNullPropertiesNames(src));
        return dest;
    }

    /**
     *  属性的拷贝 只拷贝dest非null的属性
     * @param src
     * @param dest
     * @return
     */
    public static Object transform2(Object src,Object dest){
        if (src == null || dest ==null){
            return null;
        }
        copyProperties(src,dest,getNotNullPropertiesNames(dest));
        return dest;
    }



    /**
     * 将源数组转化为目标数组
     */
    public static <T> List<T> batchTransform(final Class<T> clazz, List<?> srcList) {
        if (CollectionUtils.isEmpty(srcList)) {
            return Collections.emptyList();
        }

        List<T> result = new ArrayList<>(srcList.size());
        for (Object srcObject : srcList) {
            result.add(transform(clazz, srcObject));
        }
        return result;
    }

    /**
     * 用于将一个列表转换为列表中的对象的某个属性映射到对象的map
     */
    public static <K,V> Map<K,V> listToMapByKey(String fieldName,List<?> list){
        Map<K,V> map = new HashMap<>();
        if (CollectionUtils.isEmpty(list)){
            return map;
        }
        Class<?> aClass = list.get(0).getClass();
        Field field = deepFindField(fieldName,aClass);
        if (field==null){
            throw new IllegalArgumentException("error param in fieldName:"+fieldName);
        }
        for (Object obj : list) {
                field.setAccessible(true);
            Object o = null;
            try {
                o = field.get(obj);
                map.put((K)o,(V)obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 聚合转化 将list根据指定属性转化为 属性->属性对应集合 的map
     */
    public static <K,V> Map<K,List<V>> aggListToMapByKey(String fieldName,List<?> list){
        Map<K,List<V>> map = new HashMap<>();
        if (CollectionUtils.isEmpty(list)){
            return map;
        }
        Class<?> aClass = list.get(0).getClass();
        Field field = deepFindField(fieldName,aClass);
        if (field==null){
            throw new IllegalArgumentException("error param in fieldName:"+fieldName);
        }
        for (Object obj : list) {
            field.setAccessible(true);
            Object o = null;
            try {
                o = field.get(obj);
                map.computeIfAbsent((K)o,k->new ArrayList<>());
                map.get((K)o).add((V) obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 将JSONArray中的对象按照某个key分组
     */
    public static <K> Map<K,JSONArray> jsonArrayToMap(String key, JSONArray array){
        Map<K,JSONArray> map = new HashMap<>();
        if (StringUtil.isBlank(key) || array==null || array.isEmpty()){
            return map;
        }
        for (int i=0,len=array.size();i<len;i++){
            JSONObject object = array.getJSONObject(i);
            K ke = (K) object.get(key);
            map.computeIfAbsent(ke, k->new JSONArray());
            map.get(ke).add(object);
        }
        return map;
    }

    /**
     * 获取一个对象的属性
     */
    public static Object getProperty(Object obj,String fieldName){
        if (obj==null || StringUtil.isBlank(fieldName)){
            return null;
        }
        Field field = deepFindField(fieldName, obj.getClass());
        if (field==null){
            throw new IllegalArgumentException("error param in fieldName:"+fieldName);
        }
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置某个对象的某个属性
     */
    public static void setProperty(Object obj, String fieldName, Object value) {
        try {
            Field field = deepFindField(fieldName,obj.getClass());
            if (field==null){
                throw new IllegalArgumentException("error param in fieldName:"+fieldName);
            }
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定类的指定属性
     */
    public static Field deepFindField(String fieldName,Class<?> aClass) {
        if (StringUtil.isBlank(fieldName) || aClass == null){
            return null;
        }
        Field field = null;
        while (!aClass.getName().equals(Object.class.getName())){
            try {
                field = aClass.getDeclaredField(fieldName);
                if (field!=null){
                    return field;
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                aClass = aClass.getSuperclass();
            }
        }
        return field;
    }


    /** 获取实体属性为null的字段数组 **/
    private static String[] getNullPropertiesNames(Object src) {
        BeanWrapper bw = new BeanWrapperImpl(src);
        PropertyDescriptor[] pds = bw.getPropertyDescriptors();
        Set<String> emptyNames= new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            String name = pd.getName();
            Object propertyValue = bw.getPropertyValue(name);
            if (propertyValue==null){
                emptyNames.add(name);
            }
        }
        String[] results = new String[emptyNames.size()];
        return emptyNames.toArray(results);
    }

    /** 获取实体属性非null的字段数组 **/
    private static String[] getNotNullPropertiesNames(Object dest) {
        BeanWrapper bw = new BeanWrapperImpl(dest);
        PropertyDescriptor[] pds = bw.getPropertyDescriptors();
        Set<String> emptyNames= new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            String name = pd.getName();
            Object propertyValue = bw.getPropertyValue(name);
            if (propertyValue!=null){
                emptyNames.add(name);
            }
        }
        String[] results = new String[emptyNames.size()];
        return emptyNames.toArray(results);
    }

}
