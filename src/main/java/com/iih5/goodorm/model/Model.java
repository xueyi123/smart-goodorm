package com.iih5.goodorm.model;

import com.alibaba.fastjson.JSON;
import com.iih5.goodorm.dialect.DefaultDialect;
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
    Object[] NULL_PARA_ARRAY = new Object[]{};
    JdbcTemplate jdbcTemplate = null;
    String tableName = null;

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
     * 增加
     * @param attr
     * @param value
     */
    public  void  incr(String attr, Object value){
        if (value instanceof String){
            throw new UnsupportedOperationException("只能使用数字类型");
        }
        set(attr,attr+value);
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

    public  void  replaceById(){

    }
    public  void  replaceBy(){

    }

    public  void  sqlInfo(){

    }
    public  void  replace(){

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
     * @param conditions      比如：conditions="userId=? and name=?"
     * @param conditionValues 比如：new Object[]{1000,'hill'};
     * @return true if delete succeed otherwise false
     */
    public boolean deleteBy(String conditions, Object[] conditionValues) {
        if (conditionValues == null || conditionValues.length == 0) {
            return false;
        }
        String sql = DefaultDialect.getDialect().deleteByCondition(tableName, conditions);
        if (jdbcTemplate.update(sql, conditionValues) < 0) {
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
        String st1 = list.toString();
        String arr = st1.substring(st1.indexOf("[") + 1, st1.indexOf("]"));
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
     * @param conditionValues 比如：new Object[]{1000,'hill'};
     * @return true if delete succeed otherwise false
     */
    public boolean updateBy(String conditions, Object[] conditionValues) {
        if (getModifyFlag().isEmpty()) {
            return false;
        }
        StringBuilder sql = new StringBuilder();
        DefaultDialect.getDialect().forModelUpdate(tableName, conditions, attrs, getModifyFlag(), sql);
        if (jdbcTemplate.update(sql.toString(), conditionValues) < 0) {
            return false;
        } else {
            return true;
        }
    }
    public boolean updateById(long id) {
        return updateBy("id=?", new Object[]{id});
    }

    /**
     * 删除属性值
     *
     * @param attr
     * @return this model
     */
    public M removeAttr(String attr) {
        attrs.remove(attr);
        getModifyFlag().remove(attr);
        return (M) this;
    }

    /**
     * 删除属性值
     *
     * @param attrs
     * @return this model
     */
    public M removeAttr(String... attrs) {
        if (attrs != null)
            for (String a : attrs) {
                this.attrs.remove(a);
                this.getModifyFlag().remove(a);
            }
        return (M) this;
    }

    /**
     * 清空所有的属性值
     *
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

    public M findById(long id) {
        return findBy("id=?",new Object[]{id});
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
     * @param sql
     * @param paras
     * @return
     */
    public List<Map<String, Object>> findMapListBy(String sql, Object[] paras) {
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
        String sql = DefaultDialect.getDialect().forModelFindBy(tableName, columns, conditions);
        StringBuffer cSql=new StringBuffer();
        cSql.append("select count(*) from ( ");
        cSql.append(sql);
        cSql.append(" ) as t");
        long size= findBy(cSql.toString(),paras,Long.class);
        long totalRow=size;
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
        List<T> list = findListBy(ssql.toString(),paras,model);
        return new Page<T>(list, pageNumber, pageSize, totalPage, totalRow);
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
        String sql = DefaultDialect.getDialect().forModelFindBy(tableName, columns, conditions);
        StringBuffer cSql=new StringBuffer();
        cSql.append("select count(*) from ( ");
        cSql.append(sql);
        cSql.append(" ) as t");
        long size= findBasicObject(cSql.toString(),paras,Long.class);
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
        List list = findList(ssql.toString(),paras,isNotAttr);
        return new Page<Map>(list, pageNumber, pageSize, totalPage, totalRow);
        return null;
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
