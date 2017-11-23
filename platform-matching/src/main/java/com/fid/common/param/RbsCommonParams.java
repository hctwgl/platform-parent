package com.fid.common.param;

/**
 * 公共参数类
 * @description:
 * @author ZhangZhenxing
 * @email: 1245812397@qq.com
 */
public class RbsCommonParams {
	//普通
	public final static Integer CON_TYPE_1  = 1;
	//必选 身份因子
	public final static Integer CON_TYPE_2  = 2;
	//驱动因子
	public final static Integer CON_TYPE_3  = 3;
	
	public final static String REGEX = "([\\d]+)([\\.||\\×]?)([\\d]*)";
	
	public final static String REGEX_DB = REGEX.replace("\\", "\\\\");
	
	public final static String WORD_SPLIT = ",";
	
	public final static String TAG_SPLIT = "#";
	
	public static void main(String[] args) {
		System.out.println(REGEX_DB);
		System.out.println(REGEX);
		System.out.println("-".replace("-", "\\-"));
	}
}
