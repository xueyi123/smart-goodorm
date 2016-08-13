package com.iih5.goodorm.generator;
import com.iih5.goodorm.model.DB;

import java.util.*;

public class TableMetaTool {
    /**
     * 从db获取库表和字段信息
     * @param dataSource
     * @param dbName
     * @return
     * @throws Exception
     */
    public static List<TableMeta> findTableMetaList(String dataSource, String dbName) throws Exception{
        List<TableMeta> tableList = new ArrayList<TableMeta>();
        Set<String> sets= new HashSet<String>();
        String sql="select TABLE_NAME,DATA_TYPE,COLUMN_TYPE,COLUMN_NAME,COLUMN_COMMENT from information_schema.columns where table_schema=? ";
        List<Map<String,Object>> list = DB.use(dataSource).queryMapList(sql,new Object[]{dbName});
        for (Map<String,Object> gModel:list) {
            sets.add((String) gModel.get("TABLE_NAME"));
        }
        for (String name:sets) {
            tableList.add(toTableMeta(name,list));
        }
        return tableList;
    }
    /**
     * 从db获取库表和字段信息
     * @param dbName
     * @return
     * @throws Exception
     */
    public static List<TableMeta> findTableMetaList(String dbName) throws Exception{
        List<TableMeta> tableList = new ArrayList<TableMeta>();
        Set<String> sets= new HashSet<String>();
        String sql="select TABLE_NAME,DATA_TYPE,COLUMN_TYPE,COLUMN_NAME,COLUMN_COMMENT from information_schema.columns where table_schema=? ";
        List<Map<String,Object>> list = DB.queryMapList(sql,new Object[]{dbName});
        for (Map<String,Object> gModel:list) {
            sets.add((String) gModel.get("TABLE_NAME"));
        }
        for (String name:sets) {
            tableList.add(toTableMeta(name,list));
        }
        return tableList;
    }
    /**
     * 组合TableMeta
     * @param tableName
     * @param list
     * @return
     */
    private static TableMeta toTableMeta(String tableName, List<Map<String,Object>> list){
        TableMeta tableMeta=new TableMeta();
        tableMeta.name=tableName;
        for (Map<String,Object> model:list) {
            if (tableName.equals((String) model.get("TABLE_NAME"))){
                ColumnMeta columnMeta= new ColumnMeta();
                columnMeta.name=  (String) model.get("COLUMN_NAME");
                columnMeta.comment=(String) model.get("COLUMN_COMMENT");
                tableMeta.columnMetas.add(columnMeta);
            }
        }
        return tableMeta;
    }
}
