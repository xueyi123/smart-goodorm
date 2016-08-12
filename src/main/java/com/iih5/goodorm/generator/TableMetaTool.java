package com.iih5.goodorm.generator;
import com.iih5.goodorm.model.DB;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<MetaModel> list = DB.use(dataSource).findList(sql,new Object[]{dbName}, MetaModel.class);
        for (MetaModel gModel:list) {
            sets.add(gModel.getStr("TABLE_NAME"));
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
        List<MetaModel> list = DB.findList(sql,new Object[]{dbName}, MetaModel.class);
        for (MetaModel gModel:list) {
            sets.add(gModel.getStr("TABLE_NAME"));
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
    private static TableMeta toTableMeta(String tableName, List<MetaModel> list){
        TableMeta tableMeta=new TableMeta();
        tableMeta.name=tableName;
        for (MetaModel model:list) {
            if (tableName.equals(model.getStr("TABLE_NAME"))){
                ColumnMeta columnMeta= new ColumnMeta();
                columnMeta.dataType=model.getStr("DATA_TYPE");
                columnMeta.name=  model.getStr("COLUMN_NAME");
                columnMeta.comment=model.getStr("COLUMN_COMMENT");
                String tmp= model.getStr("COLUMN_TYPE");
                if (tmp.indexOf("(")<0||tmp.indexOf(")")<0){
                    columnMeta.typeLen=0;
                }else if (tmp.contains("unsigned")&& tmp.contains("int")){
                   columnMeta.typeLen=14;
                } else {
                    String typeLen= tmp.substring(tmp.indexOf("(")+1,tmp.indexOf(")"));
                    if (!typeLen.contains(",")){
                        columnMeta.typeLen=Integer.valueOf(typeLen);
                    }
                }
                tableMeta.columnMetas.add(columnMeta);
            }
        }
        return tableMeta;
    }
}
