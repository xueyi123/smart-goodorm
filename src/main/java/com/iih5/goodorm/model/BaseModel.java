/**
 * ---------------------------------------------------------------------------
 * 类名称   ：BaseModel
 * 类描述   ：
 * 创建人   ： xue.yi
 * 创建时间： 2016/9/30 17:01
 * 版权拥有：星电商科技
 * ---------------------------------------------------------------------------
 */
package com.iih5.goodorm.model;

public class BaseModel {
    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
