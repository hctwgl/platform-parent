package com.fid.util;

/**
 * redis帮助类
 * @description:
 * @author ZhangZhenxing
 * @email: 1245812397@qq.com
 */
public class RedisDBHelper {
	/**
	 * 获取最大maxid
	 * @param tableName
	 * @return
	 */
	public synchronized  long selectMaxId(String tableName){
		long i =5000000;
		String idstr = tableName+"_auto_increment_id";
		if(RedisAPI.get(idstr) != null) {
			i = Long.valueOf(RedisAPI.get(idstr).toString());
			i++;
			RedisAPI.set(idstr, String.valueOf(i)) ;
		}
		return i;
	}
}
