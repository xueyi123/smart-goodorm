
package com.iih5.goodorm.generator;

import java.util.HashMap;
import java.util.Map;

public class JavaType {

	protected static  Map<String, String> map = new HashMap<String, String>() {{
		put("tinyint","java.lang.Integer");//1字节，范围（-128~127）
		put("smallint","java.lang.Integer");//2字节，范围（-32768~32767）
		put("mediumint","java.lang.Integer");//3字节，范围（-8388608~8388607）
		put("int","java.lang.Integer");//4字节，范围（-2147483648~2147483647）
		put("bigint","java.lang.Long");//8字节，范围（+-9.22*10的18次方）

		put("float","java.lang.Float");//4字节，单精度浮点型，m总个数，d小数位
		put("double","java.lang.Double");//8字节，双精度浮点型，m总个数，d小数位
		put("decimal","java.lang.String");//decimal是存储为字符串的浮点数

		put("char","java.lang.String");//固定长度，最多255个字符
		put("varchar","java.lang.String");//可变长度，最多65535个字符
		put("tinytext","java.lang.String");//可变长度，最多255个字符
		put("text","java.lang.String");//可变长度，最多65535个字符
		put("mediumtext","java.lang.String");//可变长度，最多2的24次方-1个字符
		put("longtext","java.lang.String");//可变长度，最多2的32次方-1个字符

		put("date","java.sql.Date");//3字节，日期，格式：2014-09-18
		put("time","java.sql.Time");//3字节，时间，格式：08:42:30
		put("datetime","java.sql.Timestamp");//8字节，日期时间，格式：2014-09-18 08:42:30
		put("timestamp","java.sql.Timestamp");//4字节，自动存储记录修改的时间
		put("year","java.sql.Date");//1字节，年份
	}};
	public static String getJavaTypeByDataType(String dataType,int typeLen) {
		if(dataType.equals("tinyint")&& typeLen==1) {
			return "java.lang.Boolean";
		}
		if ((dataType.equals("int")||dataType.equals("bigint"))&& typeLen>12){
			return "java.lang.Long";
		}
		return map.get(dataType);
	}

}
