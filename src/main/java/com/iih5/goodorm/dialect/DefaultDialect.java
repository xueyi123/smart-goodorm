package com.iih5.goodorm.dialect;
public class DefaultDialect {
    static Dialect dialect= new MysqlDialect();
    public static Dialect getDialect(){
        return  dialect;
    }

}
