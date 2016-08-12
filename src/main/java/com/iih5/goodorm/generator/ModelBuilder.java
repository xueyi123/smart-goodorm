package com.iih5.goodorm.generator;

import com.iih5.goodorm.kit.StringKit;

public class ModelBuilder {

    private StringBuffer builder=null;
    private StringBuffer packageBuilder;
    private StringBuffer importBuilder;
    private StringBuffer classBuilder;
    private StringBuffer serialBuilder;
    private StringBuffer columnBuilder;
    private StringBuffer setMethodBuilder;
    private StringBuffer getMethodBuilder;

    public ModelBuilder(){
        builder = new StringBuffer();
        packageBuilder = new StringBuffer();
        classBuilder   = new StringBuffer();
        importBuilder  = new StringBuffer();
        serialBuilder  = new StringBuffer();
        columnBuilder  = new StringBuffer();
        setMethodBuilder=new StringBuffer();
        getMethodBuilder=new StringBuffer();
    }
    private void  join(){
        builder.append(packageBuilder);
        builder.append("\n\n");
        builder.append(importBuilder);
        builder.append("\n");
        builder.append(classBuilder);
        builder.append("\n");
        builder.append(serialBuilder);
        builder.append("\n");
        builder.append(columnBuilder);
        builder.append("\n");
        builder.append(setMethodBuilder);
        builder.append("\n");
        builder.append(getMethodBuilder);
        builder.append("\n");
        builder.append("}\n");
    }
    private ModelBuilder createPackage(String pack){
        packageBuilder.append("package "+pack+";");
        return (this);
    }
    private ModelBuilder createImport(String imp){
        importBuilder.append("import "+imp+";\n");
        return (this);
    }
    private ModelBuilder createClass(String clas){
        classBuilder.append("public class "+clas+" extends Model<"+clas+">{");
        return (this);
    }
    private ModelBuilder createSerialVersion(){
        serialBuilder.append("    private static final long serialVersionUID = 1L;");
        return (this);
    }
    private ModelBuilder createColumn(String type, String column, String comment){
        columnBuilder.append("    //"+comment+"\n");
        columnBuilder.append("    public "+type+" "+column+";\n");
        return (this);
    }
    private ModelBuilder createSetMethod(String type, String column){
        setMethodBuilder.append("    public void set");
        setMethodBuilder.append(StringKit.firstCharToUpperCase(column)+"("+type+" "+column+") { \n");
        setMethodBuilder.append("        this."+column+" = "+column+"; \n");
        setMethodBuilder.append("    }\n");
        return (this);
    }
    private ModelBuilder createGetMethod(String type, String column){
        setMethodBuilder.append("    public "+type+" "+"get");
        setMethodBuilder.append(StringKit.firstCharToUpperCase(column)+"() { \n");
        setMethodBuilder.append("        return "+column+";\n");
        setMethodBuilder.append("    }\n");
        return (this);
    }
    public String  doBuild(TableMeta tableMeta,String packageName){
        createPackage(packageName);
        createImport("com.iih5.goodorm.model.Model");
        createClass(StringKit.toModelNameByTable(tableMeta.name)+"Model");
        createSerialVersion();
        for (ColumnMeta columnMeta:tableMeta.columnMetas) {
            String javaType= JavaType.getJavaTypeByDataType(columnMeta.dataType,columnMeta.typeLen);
            if (javaType==null){
                throw new NullPointerException("找不到 "+columnMeta.dataType+"对应的JavaType");
            }
            if (JavaKeyword.contains(columnMeta.name)){
                throw new IllegalArgumentException("非法参数名:"+columnMeta.name);
            }
            createColumn(javaType,columnMeta.name,columnMeta.comment);
            createSetMethod(javaType,columnMeta.name);
            createGetMethod(javaType,columnMeta.name);
        }
        join();
        return  builder.toString();
    }

}
