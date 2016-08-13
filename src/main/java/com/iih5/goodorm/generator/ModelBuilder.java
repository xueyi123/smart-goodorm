package com.iih5.goodorm.generator;

public class ModelBuilder {

    private StringBuffer builder=null;
    private StringBuffer packageBuilder;
    private StringBuffer importBuilder;
    private StringBuffer classBuilder;
    private StringBuffer columnBuilder;

    public ModelBuilder(){
        builder = new StringBuffer();
        packageBuilder = new StringBuffer();
        classBuilder   = new StringBuffer();
        importBuilder  = new StringBuffer();
        columnBuilder  = new StringBuffer();
    }
    private void  join(){
        builder.append(packageBuilder);
        builder.append("\n\n");
        builder.append(importBuilder);
        builder.append("\n");
        builder.append(classBuilder);
        builder.append("\n");
        builder.append(columnBuilder);
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
        classBuilder.append("public class "+clas+"{");
        return (this);
    }
    private ModelBuilder createColumn(String column, String comment){
        columnBuilder.append("    /**"+comment+"*/\n");
        columnBuilder.append("    public static final String "+column+"=\""+column+"\";\n");
        return (this);
    }
    private ModelBuilder createTABLE(String table){
        columnBuilder.append("    public static final String TABLE=\""+table+"\";\n");
        return (this);
    }
    public String  doBuild(TableMeta tableMeta,String packageName){
        createPackage(packageName);
        createClass(tableMeta.name);
        createTABLE(tableMeta.name);
        for (ColumnMeta columnMeta:tableMeta.columnMetas) {
            createColumn(columnMeta.name,columnMeta.comment);
        }
        join();
        return  builder.toString();
    }

}
