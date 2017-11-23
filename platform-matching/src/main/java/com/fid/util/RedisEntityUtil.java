package com.fid.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 * 将实体反转成reids数据
 * @description:
 * @author ZhangZhenxing
 * @email: 1245812397@qq.com
 */
public class RedisEntityUtil {
	private static transient final Logger logger = LoggerFactory.getLogger(RedisEntityUtil.class);
	/**
	 * 插入集合到redis
	 * @param list
	 */
	public synchronized static  <T> void insertEntityToRedisList(List<T> list){
		if(list == null  || list.size() == 0) return;
		Class<? extends Object> clz = list.get(0).getClass();
		Map<String,Map<String,String>> result = new HashMap<>();
		try {
			for(Field field : clz.getDeclaredFields()){
				if(field.getName().equals("serialVersionUID"))continue;
				String filedNameKey = getMapKey(clz.getSimpleName(),field.getName());
				Map<String,Set<String>> columnMap = new HashMap<>();
				field.setAccessible(true);
				for (T object : list) {
					String columValue;
					columValue = field.get(object)+"";
					if(columnMap.get(columValue) == null){
						columnMap.put(columValue, new HashSet<>());
					}
					columnMap.get(columValue).add(getObjectIdString(object));
				}
				
				//转string
				Map<String,String> columnStrMap = new HashMap<>();
				for(Entry<String, Set<String>> entry: columnMap.entrySet()){
					String value = RedisAPI.getMapValue(filedNameKey, entry.getKey());
					if(!StringUtil.isBlank(value)){
						for (String v : value.split(",")) {
							entry.getValue().add(v);
						}
					}
					columnStrMap.put(entry.getKey(),String.join(",", entry.getValue()));
				}
				result.put(filedNameKey, columnStrMap);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,String> idEntity = new HashMap<>();
		for (T object : list) {
			idEntity.put(getObjectIdString(object), JSON.toJSONString(object));
		}
		result.put(clz.getSimpleName(), idEntity);
		//插入缓存中
		for (Entry<String, Map<String, String>> entry : result.entrySet()) {
			RedisAPI.setMap(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * 插入集合到redis
	 * @param list
	 */
	public synchronized static  <T> void insertEntityToRedisListAdmin(List<T> list){
		if(list == null  || list.size() == 0) return;
		Class<? extends Object> clz = list.get(0).getClass();
		Map<String,Map<String,String>> result = new HashMap<>();
		try {
			for(Field field : clz.getDeclaredFields()){
				if(field.getName().equals("serialVersionUID"))continue;
				String filedNameKey = getMapKey(clz.getSimpleName(),field.getName());
				Map<String,Set<String>> columnMap = new HashMap<>();
				field.setAccessible(true);
				for (T object : list) {
					String columValue;
					columValue = field.get(object)+"";
					if(columnMap.get(columValue) == null){
						columnMap.put(columValue, new HashSet<>());
					}
					columnMap.get(columValue).add(getObjectIdString(object));
				}
				
				//转string
				Map<String,String> columnStrMap = new HashMap<>();
				for(Entry<String, Set<String>> entry: columnMap.entrySet()){
					columnStrMap.put(entry.getKey(),String.join(",", entry.getValue()));
				}
				result.put(filedNameKey, columnStrMap);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,String> idEntity = new HashMap<>();
		for (T object : list) {
			idEntity.put(getObjectIdString(object), JSON.toJSONString(object));
		}
		result.put(clz.getSimpleName(), idEntity);
		//插入缓存中
		for (Entry<String, Map<String, String>> entry : result.entrySet()) {
			RedisAPI.setMap(entry.getKey(), entry.getValue());
		}
	}
	
	public  static  <T> void insertEntityToRedisList(List<T> list,int pageSize){
		int countPage = list.size() / pageSize;
    	for (int i = 0; i < countPage; i++) {
    		System.out.println("插入:"+i*pageSize+"===="+ (i+1)*pageSize+"===="+new SimpleDateFormat("HH:mm:ss.SSS").format(new Date()));
    		insertEntityToRedisList(list.subList(i*pageSize, (i+1)*pageSize));
		}
    	int others =  list.size() % pageSize;
    	if(others > 0 ){
    		insertEntityToRedisList(list.subList(countPage*pageSize, list.size()));
    	}
	}
	
	/**
	 * 从redis删除信息
	 * @param clz
	 * @param id
	 * @return
	 */
	public static Boolean deleteEntityFromRedisById(Class<?> clz, Long id){
		if(id == null) return false;
		String objectStr = RedisAPI.getMapValue(clz.getSimpleName(), id.toString());	
		if(objectStr == null) return false;
		JSONObject  jsonObject = JSON.parseObject(objectStr);
		for(String key :jsonObject.keySet()){
			String value = jsonObject.getString(key);
			if(key == null || value == null) continue;
 			String mapKey = getMapKey(clz.getSimpleName(),key.toString());
 			String mapValue = RedisAPI.getMapValue(mapKey, value.toString());
 			if(mapValue != null){
 				Set<String> setValue = new LinkedHashSet<>(Arrays.asList(mapValue.split(",")));
 				setValue.remove(id.toString());
 				if(setValue.size() != 0){
 					RedisAPI.setMapValue(mapKey, value.toString(), String.join(",", setValue));
 				} else {
 					RedisAPI.deleteFromMap(mapKey, value.toString());
 				}
 			}
 		}
 		RedisAPI.deleteFromMap(clz.getSimpleName(), id.toString());
		return true;
	}
	
	/**
	 * 获取redis键
	 * @param classSimpleName
	 * @param key
	 * @return
	 */
	public static String getMapKey(Object classSimpleName,Object key){
		return classSimpleName+"_"+key;
	}
	
	public static Long getObjectIdLong(Object object){
		Long value = null;
        String getter = "getId";    
        Method method;
		try {
			method = object.getClass().getMethod(getter, new Class[] {});
			value = (Long) method.invoke(object, new Object[] {});    
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		return value;
	}
	/**
	 * 根据id获取信息
	 * @param object
	 * @return
	 */
	public static String getObjectIdString(Object object){
		return getObjectIdLong(object)+""; 
	}
	
	 
       
	/**
	 * 规则： 实体名称+ _字段名 ，根据字段名去找id，id去找实体
	 * @param entityName
	 * @param object
	 */
	public  static  void insertEntityToRedis(Object object){
		JSONObject jsonObject = (JSONObject) JSONObject.toJSON(object);
		for(String key : jsonObject.keySet()){
			String objectClassName = getMapKey(object.getClass().getSimpleName(),key);
			String value = RedisAPI.getMapValue(objectClassName, ""+jsonObject.get(key)); 
			if(value == null){
				RedisAPI.setMapValue(objectClassName, ""+jsonObject.get(key),jsonObject.get("id").toString());
			} else{
				Set<String> ids = new HashSet<>();
				CollectionUtils.addAll(ids, value.split(","));  
				if(!ids.contains(jsonObject.get("id"))){
					ids.add(jsonObject.get("id").toString());
					RedisAPI.setMapValue(objectClassName, ""+jsonObject.get(key),String.join(",", ids));
				}
			}
		}
		RedisAPI.setMapValue(object.getClass().getSimpleName(), jsonObject.get("id").toString(),jsonObject.toString());
	}
	
	/**
	 * 获取JSONObject对象
	 * @param clz
	 * @param columnName
	 * @param columnValue
	 * @return
	 */
	public static List<JSONObject> getJSONObjectByColumn(Class<?> clz ,String columnName, String columnValue){
		
		String ids = RedisAPI.getMapValue(getMapKey(clz.getSimpleName(),columnName), columnValue);
		if(ids == null || ids.trim().equals(""))return null;
		List<JSONObject> list = new ArrayList<>();
		for(String id :ids.split(",")){
			String objectStr =  RedisAPI.getMapValue(clz.getSimpleName(), id);
			if(objectStr == null || objectStr.trim().equals(""))return null;
			JSONObject jsonObject = JSONObject.parseObject(objectStr);
			list.add(jsonObject);
		}
		return list;
	}
	
	/**
	 * 获取Object对象
	 * @param clz
	 * @param columnName
	 * @param columnValue
	 * @return
	 */
	public static <T> List<T>  getObjectByColumn(T t ,String columnName, String... columnValue){
		Class<? extends Object> clz = t.getClass();
		List<String> idsList = RedisAPI.getMapValues(getMapKey(clz.getSimpleName(),columnName), columnValue);
		if(idsList == null) return new ArrayList<>();
		Set<String> idsSet  = new LinkedHashSet<>(idsList);
		idsSet.remove(null);
		String ids = String.join(",", idsSet);
		if(ids == null || ids.trim().equals(""))return Collections.emptyList();
		List<T> list = new ArrayList<>();
		List<String> objectStrs =  RedisAPI.getMapValues(clz.getSimpleName(), ids.split(","));
		if(objectStrs == null) return Collections.emptyList();
		for(String objectStr :objectStrs){
			if(objectStr == null || objectStr.trim().equals(""))continue;
//			JSONObject jsonObject = JSONObject.fromObject(objectStr);
			try {
//				list.add(JsonWithObjectUtil.fromJsonToJava(jsonObject, clz));
//				list.add((T)jsonObject.toBean(jsonObject, clz));
				list.add((T) JSON.parseObject(objectStr, clz));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * 获取Object对象
	 * @param clz
	 * @param columnName
	 * @param columnValue
	 * @return
	 */
	/*public static List<JSONObject> getJSONObjectByColumns(Class clz ,String columnName, String columnValue){
		String ids = RedisAPI.getMapValue(getMapKey(clz.getSimpleName(),columnName), columnValue);
		if(ids == null || ids.trim().equals(""))return null;
		List<JSONObject> list = new ArrayList<>();
		List<String> objectStrs =  RedisAPI.getMapValues(clz.getSimpleName(), ids.split(","));
		for(String objectStr :objectStrs){
			if(objectStr == null || objectStr.trim().equals(""))return null;
			JSONObject jsonObject = JSONObject.fromObject(objectStr);
			try {
				list.add((T) JSON.parseObject(objectStr, clz));
				list.add(jsonObject);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}*/
	
	/**
	 * 获取想要的字段数据
	 * @param clz
	 * @param columnName
	 * @param columnValue
	 * @param wantColumnName
	 * @return
	 */
	public static List<String> getWantColumnByColumn(Class<?> clz ,String columnName, String columnValue,String wantColumnName){
		List<String> wantColumns = new ArrayList<>();
		List<JSONObject> jsonObjects = getJSONObjectByColumn(clz,columnName,columnValue);
		for (JSONObject jsonObject : jsonObjects) {
			String wantColumn = jsonObject.getString(wantColumnName);
			wantColumns.add(wantColumn);
		}
		return wantColumns;
	}
	
	
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
//		System.out.println(RedisAPI.getMap(RbsWords.class.getSimpleName()+"_name"));
//		System.out.println(RedisAPI.getMap(RbsWords.class.getSimpleName()));
//		System.out.println(RedisAPI.getMap(RbsWords.class.getSimpleName()+"_id"));
//		select * from rbs_conclusion_tags where id = 205862;
//		deleteEntityFromRedisById(RbsStructpic.class, 4273L);
//		Arrays.asList("1,2,3").size();
//		System.out.println(Arrays.asList("1,2,3").size());
//		deleteEntityFromRedisById
//		deleteEntityFromRedisById(clz, id)
//		System.out.println(RedisAPI.getMap(RbsConclusionTags.class.getSimpleName()));
//		System.out.println(getObjectByColumn(new RbsConclusionTags(),"id",new String[]{"112764"}));
//		System.out.println(getObjectByColumn(new RbsConclusion(),"id",new String[]{"225"}));
//		System.out.println(getObjectByColumn(new RbsPicTags(),"tagsId",new String[]{"20447"}));
//		System.out.println(getObjectByColumn(new RbsTags(),"id",new String[]{"20447"}));
	}
}
