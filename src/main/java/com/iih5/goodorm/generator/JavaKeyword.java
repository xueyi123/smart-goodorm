

package com.iih5.goodorm.generator;

import java.util.HashSet;
import java.util.Set;

/**
 * JavaKeyword.JAVA敏感词列表
 */
public class JavaKeyword {
	
	private static  String[] keywordArray = {
		"abstract",
		"assert",
		"boolean",
		"break",
		"byte",
		"case",
		"catch",
		"char",
		"class",
		"const",
		"continue",
		"default",
		"do",
		"double",
		"else",
		"enum",
		"extends",
		"final",
		"finally",
		"float",
		"for",
		"goto",
		"if",
		"implements",
		"import",
		"instanceof",
		"int",
		"interface",
		"long",
		"native",
		"new",
		"package",
		"private",
		"protected",
		"public",
		"return",
		"strictfp",
		"short",
		"static",
		"super",
		"switch",
		"synchronized",
		"this",
		"throw",
		"throws",
		"transient",
		"try",
		"void",
		"volatile",
		"while"
	};
	
	private static  Set<String> set = initKeyword();
	private static Set<String> initKeyword() {
		HashSet<String> ret = new HashSet<String>();
		for (String keyword : keywordArray) {
			ret.add(keyword);
		}
		return ret;
	}
	
	public static boolean contains(String str) {
		return set.contains(str);
	}

}






