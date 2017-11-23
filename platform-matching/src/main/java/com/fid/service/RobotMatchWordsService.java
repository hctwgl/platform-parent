package com.fid.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.fid.domain.RobotMatchWords;

public interface RobotMatchWordsService {
	
	public int insertBatch(List<RobotMatchWords> ts);
	
	public int insert(RobotMatchWords tMatchWords);
	
	public List<RobotMatchWords>  queryListByParam(Map<String,Object> param);
	
	List<RobotMatchWords> queryListByParamFormRedis(RobotMatchWords t, String columnName, Collection<Object> columnValues);
	
	List<RobotMatchWords> queryListAllFormRedis(RobotMatchWords t);
	
	public Boolean insertDBWithCache(RobotMatchWords t);
	
	public Boolean deleteByPrimaryKeyDBWithCache(Class<RobotMatchWords> clz,Long id);
	
	public Boolean updateByPrimaryKeyDBWithCache(RobotMatchWords t);

}
