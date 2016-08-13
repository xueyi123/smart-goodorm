package com.iih5.goodorm.model;

import com.alibaba.fastjson.JSON;
import com.iih5.goodorm.dialect.DefaultDialect;
import com.iih5.goodorm.dialect.MysqlDialect;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public abstract class Model<M extends Model> implements Serializable {

    Map<String, Object> attrs = new HashMap<String, Object>();
    Set<String> modifyFlag = new HashSet<String>();
    JdbcTemplate jdbcTemplate = null;
    String tableName = null;

    public Model(){}

    public Model(DBExecutor dbExecutor,String table) {
        this.tableName = table;
        this.jdbcTemplate = dbExecutor.getJdbcTemplate();
    }

    public Map<String, Object> getAttrs() {
        return attrs;
    }
    private Set<String> getModifyFlag() {
        return modifyFlag;
    }
    /**
     * Set attribute to model.
     *
     * @param attr  the attribute name of the model
     * @param value the value of the attribute
     * @return this model
     */
    public M set(String attr, Object value) {
        attrs.put(attr, value);
        getModifyFlag().add(attr);    // Add modify flag, update() need this flag.
        return (M) this;
    }
    /**
     * 增加减
     * @param attr
     * @param value
     */
    public  M  incr(String attr, Object value){
        if (value instanceof String){
            throw new UnsupportedOperationException("只能使用数字类型");
        }
        String number = String.valueOf(value);
        if (number.substring(0,1).equals("-")){
            set(attr,attr+number);
        }else {
            set(attr,attr+"+"+number);
        }

        return (M)this;
    }
    /**
     * 乘
     * @param attr
     * @param value
     */
    public  M  mult(String attr, Object value){
        if (value instanceof String){
            throw new UnsupportedOperationException("只能使用数字类型");
        }
        String number = String.valueOf(value);
        set(attr,attr+"*"+number);

        return (M)this;
    }
    /**
     * 除
     * @param attr
     * @param value
     */
    public  M  minus(String attr, Object value){
        if (value instanceof String){
            throw new UnsupportedOperationException("只能使用数字类型");
        }
        String number = String.valueOf(value);
        set(attr,attr+"/"+number);

        return (M)this;
    }
    /**
     * @param attr
     * @param <T>
     * @return
     */
    public <T> T get(String attr) {
        return (T) (attrs.get(attr));
    }
    /**
     * @param attr
     * @return
     */
    public String getStr(String attr) {
        return (String) attrs.get(attr);
    }
    /**
     * @param attr
     * @return
     */
    public Integer getInt(String attr) {
        return (Integer) attrs.get(attr);
    }
    /**
     * @param attr
     * @return
     */
    public Long getLong(String attr) {
        return (Long) attrs.get(attr);
    }
    /**
     * @param attr
     * @return
     */
    public java.math.BigInteger getBigInteger(String attr) {
        return (java.math.BigInteger) attrs.get(attr);
    }
    /**
     * @param attr
     * @return
     */
    public Date getDate(String attr) {
        return (Date) attrs.get(attr);
    }
    /**
     * @param attr
     * @return
     */
    public java.sql.Time getTime(String attr) {
        return (java.sql.Time) attrs.get(attr);
    }
    /**
     * @param attr
     * @return
     */
    public java.sql.Timestamp getTimestamp(String attr) {
        return (java.sql.Timestamp) attrs.get(attr);
    }
    /**
     * @param attr
     * @return
     */
    public Double getDouble(String attr) {
        return (Double) attrs.get(attr);
    }
    /**
     * @param attr
     * @return
     */
    public Float getFloat(String attr) {
        return (Float) attrs.get(attr);
    }
    /**
     * @param attr
     * @return
     */
    public Boolean getBoolean(String attr) {
        return (Boolean) attrs.get(attr);
    }
    /**
     * @param attr
     * @return
     */
    public java.math.BigDecimal getBigDecimal(String attr) {
        return (java.math.BigDecimal) attrs.get(attr);
    }
    /**
     * @param attr
     * @return
     */
    public byte[] getBytes(String attr) {
        return (byte[]) attrs.get(attr);
    }
    /**
     * @param attr
     * @return
     */
    public Number getNumber(String attr) {
        return (Number) attrs.get(attr);
    }
    /**
     * 添加保存到数据库
     * @return 返回保存状态
     */
    public boolean save() {
        try {
            StringBuilder sql = new StringBuilder();
            List<Object> paras = new ArrayList<Object>();
            DefaultDialect.getDialect().forModelSave(tableName, attrs, sql, paras);
            if (jdbcTemplate.update(sql.toString(), paras.toArray()) < 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 根据条件删除数据
     *
     * @param conditions 比如：conditions="userId=? and name=?"
     * @param params 比如：new Object[]{1000,'hill'};
     * @return true if delete succeed otherwise false
     */
    public boolean deleteBy(String conditions, Object[] params) {
        if (params == null || params.length == 0) {
            return false;
        }
        String sql = DefaultDialect.getDialect().deleteByCondition(tableName, conditions);
        if (jdbcTemplate.update(sql, params) < 0) {
            return false;
        }
        return true;
    }
    public boolean deleteById(long id) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ");
        sql.append(tableName);
        sql.append(" where id= ");
        sql.append(id);
        if (jdbcTemplate.update(sql.toString()) < 0) {
            return false;
        }
        return true;
    }
    public boolean deleteByIds(List list) {
        String str = list.toString();
        return deleteByIds(str);
    }
    public boolean deleteByIds(Long... ids) {
        String str = JSON.toJSONString(ids) ;
        return deleteByIds(str);
    }
    public boolean deleteByIds(Integer... ids) {
        String str = JSON.toJSONString(ids) ;
        return deleteByIds(str);
    }
     boolean deleteByIds(String str) {
        String arr = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ");
        sql.append(tableName);
        sql.append(" where id in ");
        sql.append("(");
        sql.append(arr);
        sql.append(")");
        if (jdbcTemplate.update(sql.toString()) < 0) {
            return false;
        }
        return true;
    }

    /**
     * 根据条件修改数据
     *
     * @param conditions      比如：conditions="userId=? and name=?"
     * @param params 比如：new Object[]{1000,'hill'};
     * @return true if delete succeed otherwise false
     */
    public boolean updateBy(String conditions, Object[] params) {
        if (getModifyFlag().isEmpty()) {
            return false;
        }
        StringBuilder sql = new StringBuilder();
        DefaultDialect.getDialect().forModelUpdate(tableName, conditions, attrs, getModifyFlag(), sql);
        if (jdbcTemplate.update(sql.toString(), params) < 0) {
            return false;
        } else {
            return true;
        }
    }
    public boolean updateById(long id) {
        return updateBy("id=?", new Object[]{id});
    }
    /**
     * 替换
     * @param id
     * @return
     */
    public  boolean  replaceById(long id){
        boolean rt = true;
        if (findById(id) == null){
            rt = save();
        }else {
            rt = updateById(id);
        }
        return rt;
    }
    /**
     * 替换
     * @param condition
     * @param paras
     * @return
     */
    public  boolean  replaceBy(String condition, Object[] paras){
        boolean rt = true;
        if (findBy(condition,paras) == null){
            rt = save();
        }else {
            rt = updateBy(condition, paras);
        }
        return rt;
    }
    /**
     * 保存并返回自增长ID
     * @return
     */
    public Long saveAndReturnId(){
        if (save()){
            String sql="SELECT LAST_INSERT_ID();";
            return  DB.query(sql,new Object[]{},Long.class);
        }
        return null;
    }
    /**
     * 清空所有的属性值
     * @return
     */
    public M clear() {
        attrs.clear();
        getModifyFlag().clear();
        return (M) this;
    }
    /**
     * @param columns  字段名称，比如 columns="id,name,age"
     * @param conditions  conditions 查询条件，比如 conditions="user_id=? and age=?"
     * @param paras  查询条件对应的参数
     * @return 返回Model对象
     * @
     */
    public M findBy(String columns, String conditions, Object[] paras) {
        List<M> result = findListBy(columns, conditions, paras);
        return result.size() > 0 ? result.get(0) : null;
    }
    /**
     * @param conditions     conditions 查询条件，比如 conditions="user_id=? and age=?"
     * @param paras 查询条件对应的参数
     * @return 返回Model对象 1
     * @
     */
    public M findBy(String conditions, Object[] paras) {
        List<M> result = findListBy(conditions, paras);
        return result.size() > 0 ? result.get(0) : null;
    }
    /**
     * 根据ID查找
     * @param id
     * @return
     */
    public M findById(long id) {
        return findBy("id=?",new Object[]{id});
    }
    /**
     * 获取基本类型数值
     * @param columns 表字段
     * @param conditions 条件
     * @param paras 条件参数
     * @param classType 基本类型
     * @param <T>
     * @return
     */
    public <T> T findBy(String columns,String conditions, Object[] paras, final Class<T> classType) {
        if (!BaseUtils.isBaseObject(classType)){
          throw new UnsupportedOperationException(classType.getName()+"非法类型，只允许基本数值类型（包含String）");
        }
        String sql = DefaultDialect.getDialect().forModelFindBy(tableName, columns, conditions);
        return jdbcTemplate.queryForObject(sql, paras, classType);
    }
    /**
     * 查找Model对象列表
     *
     * @param columns        字段名称，比如 columns="id,name,age"
     * @param conditions     查询条件，比如 conditions="user_id=? and age=?"
     * @param conditionParas 查询条件对应的参数
     * @param <T>
     * @return 返回Model对象列表
     * @
     */
    public <T> List<T> findListBy(String columns, String conditions, Object[] conditionParas) {
        String sql = DefaultDialect.getDialect().forModelFindBy(tableName, columns, conditions);
        final Set<String> columnMeta = new HashSet<String>();
        return jdbcTemplate.query(sql, conditionParas, new RowMapper<T>() {
            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                try {
                    if (columnMeta.size() == 0) {
                        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                            String column = rs.getMetaData().getColumnLabel(i + 1);
                            columnMeta.add(column);
                        }
                    }
                    Model<?> mModel = getUsefulClass().newInstance();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    Map<String, Object> attrs = mModel.getAttrs();
                    for (int i = 1; i <= columnCount; i++) {
                        Object value = rs.getObject(i);
                        if (value != null) {
                            attrs.put(rsmd.getColumnLabel(i), value);
                        }
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
     * 查找Model对象列表
     *
     * @param conditions     查询条件，比如 conditions="user_id=? and age=?"
     * @param params 查询条件对应的参数
     * @return 返回Model对象列表
     * @
     */
    public List<M> findListBy(String conditions, Object[] params) {
        return findListBy("*", conditions, params);
    }
    /**
     * 获取Map格式列表(不包含attrs包裹属性)
     *
     * @param condition
     * @param paras
     * @return
     */
    public List<Map<String, Object>> findMapListBy(String condition, Object[] paras) {
        String sql = DefaultDialect.getDialect().forModelFindBy(tableName,"*",condition);
        return jdbcTemplate.queryForList(sql, paras);
    }
    /**
     * 分页查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每一页的大小
     * @param columns    字段名称，比如 columns="id,name,age"
     * @param conditions 查询条件，比如 conditions="user_id=? and age=?"
     * @param paras      查询参数
     * @return 返回对象列表
     * @
     */
    public Page<M> paginate(int pageNumber, int pageSize, String columns, String conditions, Object[] paras) {
        if (pageNumber<=0){
            pageNumber=1;
        }
        String sql = DefaultDialect.getDialect().forModelFindBy(tableName, columns, conditions);
        StringBuffer cSql=new StringBuffer();
        cSql.append("select count(*) from ( ");
        cSql.append(sql);
        cSql.append(" ) as t");
        Long size= DB.query(cSql.toString(),paras,Long.class);
        long totalRow=size;
        if (totalRow == 0) {
            return new Page<M>(new ArrayList<M>(0), pageNumber, pageSize, 0, 0);
        }
        long totalPage = (totalRow / pageSize);
        if (totalRow % pageSize != 0) {
            totalPage++;
        }
        if (pageNumber > totalPage) {
            return new Page<M>(new ArrayList<M>(0), pageNumber, pageSize, totalPage, totalRow);
        }

        long offset = pageSize * (pageNumber - 1);
        StringBuilder ssql = new StringBuilder();
        ssql.append(sql).append(" ");
        ssql.append(" limit ").append(offset).append(", ").append(pageSize);
        List list = DB.queryList(ssql.toString(),paras,this.getClass());
        return new Page<M>(list, pageNumber, pageSize, totalPage, totalRow);
    }
    /**
     * 分页查询
     *
     * @param pageNumber 第几页
     * @param pageSize   每一页的大小
     * @param columns    字段名称，比如 columns="id,name,age"
     * @param conditions 查询条件，比如 conditions="user_id=? and age=?"
     * @param paras      查询参数
     * @return 返回对象列表
     * @
     */
    public Page<Map> paginateMap(int pageNumber, int pageSize, String columns, String conditions, Object[] paras) {
        if (pageNumber<=0){
            pageNumber=1;
        }
        String sql = DefaultDialect.getDialect().forModelFindBy(tableName, columns, conditions);
        StringBuffer cSql=new StringBuffer();
        cSql.append("select count(*) from ( ");
        cSql.append(sql);
        cSql.append(" ) as t");
        Long size= DB.query(cSql.toString(),paras,Long.class);
        long totalRow=size;
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
        List list = DB.queryMapList(ssql.toString(), paras);
        return new Page<Map>(list, pageNumber, pageSize, totalPage, totalRow);
    }
    /**
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (!(o instanceof Model))
            return false;
        if (getUsefulClass() != ((Model) o).getUsefulClass())
            return false;
        if (o == this)
            return true;
        return this.attrs.equals(((Model) o).attrs);
    }
    /**
     * @return
     */
    public int hashCode() {
        return (attrs == null ? 0 : attrs.hashCode()) ^ (getModifyFlag() == null ? 0 : getModifyFlag().hashCode());
    }
    /**
     * 转换为json字符串
     *
     * @return json str
     */
    public String toString() {
        return JSON.toJSONString(this.getAttrs());
    }
    /**
     * @return
     */
    private Class<? extends Model> getUsefulClass() {
        Class c = getClass();
        return c.getName().indexOf("EnhancerByCGLIB") == -1 ? c : c.getSuperclass();
    }
}
