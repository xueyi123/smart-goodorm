package com.iih5.goodorm.model;
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
}
