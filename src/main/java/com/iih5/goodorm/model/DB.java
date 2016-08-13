package com.iih5.goodorm.model;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

public class DB {
    private static DBExecutor defaultExecutor = null;
    static {
        defaultExecutor = DBExecutor.use();
    }
    public static DBExecutor use(String dataSource){
        return DBExecutor.use(dataSource);
    }
    public static DBExecutor use(String dataSource,String prefix){
        return DBExecutor.use(dataSource,prefix);
    }
    public static JdbcTemplate getJdbcTemplate(){
        return  defaultExecutor.getJdbcTemplate();
    }
    public  static  String getTablePrefix(){
        return defaultExecutor.getTablePrefix();
    }
    public static M M(String table){
        return defaultExecutor.M(table);
    }

    public static  <T> List<T> queryList(String sql, Object[] paras, final Class<T> classType) {
       return defaultExecutor.queryList(sql,paras,classType);
    }
    /**
     * 获取Map格式列表(不包含attrs包裹属性)
     *
     * @param sql
     * @param paras
     * @return
     */
    public static List<Map<String, Object>> queryMapList(String sql, Object[] paras) {
        return defaultExecutor.queryMapList(sql, paras);
    }
    /**
     * 查找Model对象
     *
     * @param sql
     * @param paras
     * @param classType
     * @param <T>
     * @return
     * @
     */
    public static  <T> T query(String sql, Object[] paras, final Class<T> classType) {
       return defaultExecutor.query(sql, paras, classType);
    }
    /**
     * 更新数据对象（update or insert,delete）
     *
     * @param sql
     * @param paras
     * @return
     * @throws DataAccessException
     */
    public static  int execute(String sql, Object[] paras) throws DataAccessException {
        return defaultExecutor.execute(sql, paras);
    }
    /**
     * 批量更新数据对象（update or insert,delete
     *
     * @param sql
     * @param batchArgs
     * @return
     */
    public static int[] batchExecute(String sql, List<Object[]> batchArgs) {
        return defaultExecutor.batchExecute(sql, batchArgs);
    }
    /**
     * 多表分页查询
     *
     * @param pageNumber
     * @param pageSize
     * @param sql
     * @param paras
     * @param <T>
     * @return
     */
    public static <T> Page<T> paginate( int pageNumber, int pageSize, String sql, Object[] paras,final Class<T> model) {
       return defaultExecutor.paginate(model,pageNumber,pageSize,sql,paras);
    }
    /**
     * 多表分页查询
     *
     * @param pageNumber
     * @param pageSize
     * @param sql
     * @param paras
     * @return
     */
    public  static Page<Map> paginateMap(int pageNumber, int pageSize, String sql, Object[] paras) {
       return defaultExecutor.paginateMap(pageNumber, pageSize, sql, paras);
    }
}
