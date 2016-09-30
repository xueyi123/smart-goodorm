package com.iih5.goodorm.model;

import jdk.jfr.events.ErrorThrownEvent;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Map;

public class BaseUtils {
    /**
     * 判断是否为基本对象
     * @param model
     * @param <T>
     * @return
     */
    public static  <T> boolean isBaseObject( Class<T> model){
        String name = model.getName();
        if (name.equals("java.lang.String") ||
                name.equals("java.lang.Integer") ||
                name.equals("java.lang.Double") ||
                name.equals("java.lang.Float") ||
                name.equals("java.lang.Long") ||
                name.equals("java.lang.Short") ||
                name.equals("java.lang.Boolean") ||
                name.equals("java.lang.Byte")||
                name.equals("int")||
                name.equals("double")||
                name.equals("float")||
                name.equals("long")||
                name.equals("short")||
                name.equals("boolean")||
                name.equals("byte")
                ) {
            return true;
        }
        return false;
    }

    /**
     * map对象转换为bean
     * @param map
     * @param bean
     */
    public static void mapToBean(Map<String, Object> map, Object bean) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    Method setter = property.getWriteMethod();
                    setter.invoke(bean, value);
                }
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException("map对象转换为bean时出错："+e.getMessage());
        }
    }

    /**
     * bean对象转换为map
     * @param bean
     * @return
     */
    public static Map<String, Object> beanToMap(Object bean) {
        if(bean == null){return null;}
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (!key.equals("class")) {
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(bean);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException("bean对象转换为map时出错："+e.getMessage());
        }
        return map;
    }
}
