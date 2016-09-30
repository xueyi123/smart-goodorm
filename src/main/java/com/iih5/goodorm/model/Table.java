/**
 * ---------------------------------------------------------------------------
 * 类名称   ：Table
 * 类描述   ：
 * 创建人   ： xue.yi
 * 创建时间： 2016/8/12 10:50
 * 版权拥有：银信网银科技
 * ---------------------------------------------------------------------------
 */
package com.iih5.goodorm.model;

public class Table extends ModelOperator<Table> {

    public Table(){};
    public Table(DBExecutor dbExecutor, String table){
       super(dbExecutor,table);
    }
}
