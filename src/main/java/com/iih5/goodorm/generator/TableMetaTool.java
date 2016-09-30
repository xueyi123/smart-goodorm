package com.iih5.goodorm.generator;


import com.iih5.goodorm.model.DB;

import java.sql.*;
import java.util.*;

public class TableMetaTool {
    static String SQL = "SELECT TABLE_NAME,COLUMN_NAME,COLUMN_COMMENT FROM information_schema.columns WHERE table_schema=?";
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
        List<Map<String,Object>> list = DB.use(dataSource).queryMapList(SQL,new Object[]{dbName});
        for (Map<String,Object> m:list) {sets.add((String) m.get("TABLE_NAME"));}
        for (String name:sets) {tableList.add(toTableMeta(name,list));}
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
        List<Map<String,Object>> list = DB.queryMapList(SQL,new Object[]{dbName});
        for (Map<String,Object> gModel:list) {
            sets.add((String) gModel.get("TABLE_NAME"));
        }
        for (String name:sets) {
            System.out.println(""+name);
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
    private static TableMeta toTableMeta(String tableName, List<Map<String,Object>> list)  {
        Connection connection = null;
        Statement stm = null;
        ResultSet rs = null;
        try {
            Map<String,String> javaType = new HashMap<String, String>();
            String sql="select * from "+tableName+" where 1=2 ";
            connection = DB.getJdbcTemplate().getDataSource().getConnection();
            stm =  connection.createStatement();
            rs = stm.executeQuery(sql);
            ResultSetMetaData rmd = rs.getMetaData();
            for (int i=1; i<= rmd.getColumnCount(); i++) {
                javaType.put(rmd.getColumnLabel(i),rmd.getColumnClassName(i));
            }
            TableMeta tableMeta = new TableMeta();
            tableMeta.name = tableName;
            for (Map<String,Object> model:list) {
                if (tableName.equals(model.get("TABLE_NAME"))){
                    ColumnMeta columnMeta= new ColumnMeta();
                    columnMeta.name=(String)model.get("COLUMN_NAME");
                    columnMeta.comment=(String) model.get("COLUMN_COMMENT");
                    columnMeta.dataType=javaType.get(columnMeta.name);
                    tableMeta.columnMetas.add(columnMeta);
                }
            }
            return tableMeta;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                connection.close();
                rs.close();
                stm.close();
            } catch (SQLException e) {
            }
        }
        return null;
    }
}
