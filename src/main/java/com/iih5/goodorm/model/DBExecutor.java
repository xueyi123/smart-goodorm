package com.iih5.goodorm.model;

import com.iih5.goodorm.kit.SpringKit;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class DBExecutor {
    private static Map<String, DBExecutor> map = new HashMap<String, DBExecutor>();
    public JdbcTemplate jdbc = null;
    public String tablePrefix = "t_";
    public String dataSource = null;

    /**
     * 选择使用数据库（默认选中第一个）
     *
     * @param dataSource 在spring.xml里配置的jdbc dataSource beanId
     * @return 返回DbExecutor.
     */
    public static DBExecutor use(String dataSource) {
        DBExecutor executor = map.get(dataSource);
        if (executor == null) {
            executor = new DBExecutor();
            executor.jdbc = SpringKit.getJdbcTemplateByDataSource(dataSource);
            executor.dataSource = dataSource;
            map.put(dataSource, executor);
        }
        return executor;
    }

    public static DBExecutor use(String dataSource, String prefix) {
        DBExecutor executor = map.get(dataSource);
        if (executor == null) {
            executor = new DBExecutor();
            executor.jdbc = SpringKit.getJdbcTemplateByDataSource(dataSource);
            executor.dataSource = dataSource;
            executor.tablePrefix = prefix;
            map.put(dataSource, executor);
        }
        return executor;
    }

    public static DBExecutor use() {
        String[] dbs = SpringKit.getApplicationContext().getBeanNamesForType(DataSource.class);
        return use(dbs[0]);
    }
    /**
     * 创建表操作
     * @param table
     * @return
     */
    public M M(String table){
        DBExecutor executor = map.get(dataSource);
        M tb = new M(executor,table);
        return tb;
    }
    /**
     * 返回JdbcTemplate
     *
     * @param
     * @return
     */
    public JdbcTemplate getJdbcTemplate() {
        DBExecutor executor = map.get(dataSource);
        if (executor != null) {
            return executor.jdbc;
        }
        return null;
    }

    /**
     * 获取当前表前缀
     * @return
     */
    public String getTablePrefix(){
        return  tablePrefix;
    }
    /**
     * 查找Model对象列表
     *
     * @param sql
     * @param paras
     * @param classType
     * @param <T>
     * @return
     * @
     */
    public <T> List<T> queryList(String sql, Object[] paras, final Class<T> classType) {
        if (BaseUtils.isBaseObject(classType)){
            return jdbc.queryForList(sql, paras, classType);
        }
        final Set<String> columnMeta = new HashSet<String>();
        return jdbc.query(sql, paras, new RowMapper<T>() {
            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                try {
                    if (columnMeta.size() == 0) {
                        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                            String column = rs.getMetaData().getColumnLabel(i + 1);
                            columnMeta.add(column);
                        }
                    }
                    Model mModel = (Model) classType.newInstance();
                    ResultSetMetaData rad = rs.getMetaData();
                    int columnCount = rad.getColumnCount();
                    Map<String, Object> attrs = mModel.getAttrs();
                    for (int i = 1; i <= columnCount; i++) {
                        Object value = rs.getObject(i);
                        attrs.put(rad.getColumnName(i), value);
                    }
                    return (T) mModel;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
    /**
     * 获取Map格式列表(不包含attrs包裹属性)
     *
     * @param sql
     * @param paras
     * @return
     */
    public List<Map<String, Object>> queryMapList(String sql, Object[] paras) {
        return jdbc.queryForList(sql, paras);
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
    public <T> T query(String sql, Object[] paras, final Class<T> classType) {
        if (BaseUtils.isBaseObject(classType)){
            return jdbc.queryForObject(sql, paras, classType);
        }else {
            List<T> result = queryList(sql, paras, classType);
            return result.size() > 0 ? result.get(0) : null;
        }
    }
    /**
     * 更新数据对象（update or insert,delete）
     *
     * @param sql
     * @param paras
     * @return
     * @throws DataAccessException
     */
    public int execute(String sql, Object[] paras) throws DataAccessException {
        return jdbc.update(sql, paras);
    }
    /**
     * 批量更新数据对象（update or insert,delete
     *
     * @param sql
     * @param batchArgs
     * @return
     */
    public int[] batchExecute(String sql, List<Object[]> batchArgs) {
        return jdbc.batchUpdate(sql, batchArgs);
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
    public <T> Page<T> paginate(int pageNumber, int pageSize, String sql, Object[] paras,final Class<T> model) {
        long size = query(sql, paras, Long.class);
        long totalRow = size;
        if (totalRow == 0) {
            return new Page<T>(new ArrayList<T>(0), pageNumber, pageSize, 0, 0);
        }
        long totalPage = (totalRow / pageSize);
        if (totalRow % pageSize != 0) {
            totalPage++;
        }
        if (pageNumber > totalPage) {
            return new Page<T>(new ArrayList<T>(0), pageNumber, pageSize, totalPage, totalRow);
        }

        long offset = pageSize * (pageNumber - 1);
        StringBuilder ssql = new StringBuilder();
        ssql.append(sql).append(" ");
        ssql.append(" limit ").append(offset).append(", ").append(pageSize);
        List<T> list = queryList(ssql.toString(), paras, model);

        return new Page<T>(list, pageNumber, pageSize, totalPage, totalRow);
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
    public Page<Map> paginateMap(int pageNumber, int pageSize, String sql, Object[] paras) {
        long size = query(sql, paras, Long.class);
        long totalRow = size;
        if (totalRow == 0) {
            return new Page<Map>(new ArrayList<Map>(0), pageNumber, pageSize, 0, 0);
        }
        long totalPage = (totalRow / pageSize);
        if (totalRow % pageSize != 0) {
            totalPage++;
        }
        if (pageNumber > totalPage) {
            return new Page<Map>(new ArrayList<Map>(0), pageNumber, pageSize, totalPage, totalRow);
        }

        long offset = pageSize * (pageNumber - 1);
        StringBuilder ssql = new StringBuilder();
        ssql.append(sql).append(" ");
        ssql.append(" limit ").append(offset).append(", ").append(pageSize);
        List list = queryMapList(ssql.toString(), paras);
        return new Page<Map>(list, pageNumber, pageSize, totalPage, totalRow);
    }

}








