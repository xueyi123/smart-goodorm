package com.iih5.goodorm.dialect;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Dialect {

    /**
     * 构建查找sql
     * @param tableName
     * @param columns 要查询的字段
     * @param conditions ，执行条件 比如：conditions="userId=? and name=?"
     * @return  sql
     */
    public String forModelFindBy(String tableName, String columns,String conditions);

    /**
     * 构建删除sql
     * @param tableName
     * @param conditions 执行条件 比如：conditions="userId=? and name=?"
     * @return sql
     */
    public String deleteByCondition(String tableName, String conditions);
    /**
     * 构建保存sql
     * @param tableName
     * @param attrs
     * @param sql
     * @param paras
     */
    public void forModelSave(String tableName, Map<String, Object> attrs, StringBuilder sql, List<Object> paras);

    /**
     * 构建更改sql
     * @param tableName
     * @param conditions 执行条件 比如：conditions="userId=? and name=?"
     * @param attrs
     * @param modifyFlag
     * @param sql
     */
    public void forModelUpdate(String tableName, String conditions, Map<String, Object> attrs, Set<String> modifyFlag, StringBuilder sql);

}






