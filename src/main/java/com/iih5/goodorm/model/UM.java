package com.iih5.goodorm.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public  class UM implements Serializable {
    Map<String, Object> attrs = new HashMap<String, Object>();
    public Map<String, Object> getAttrs() {
        return attrs;
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
}
