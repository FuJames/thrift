package com.fqz.thrift.util;

/**
 * @author qianzhong.fu
 *
 */
public class StringUtil {
	public static boolean isNullOrEmpty(String str){
		if(str == null || str.isEmpty())
			return true;
		return false;
	}
	public static String getStringAfterLastSlash(String str){
		int index = str.lastIndexOf("/");
		if(index != -1)
			return str.substring(index + 1);
		return str;
	}
	public static String elimateAllLeftSlash(String string){
		return elimateFromString("/", string);
	}
	public static String elimateAllPercentLetter(String string){
		return elimateFromString("%", string);
	}
	private static String elimateFromString(String elimatedStr,String string){
		return string.replaceAll(elimatedStr, "");
	}
}
