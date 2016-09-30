package com.iih5.goodorm.generator;

import java.util.ArrayList;
import java.util.List;

public class TableMeta {
    public String name;				// 表名
    public List<ColumnMeta> columnMetas = new ArrayList<ColumnMeta>();	// 字段 meta
}
