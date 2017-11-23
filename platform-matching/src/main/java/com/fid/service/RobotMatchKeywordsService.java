package com.fid.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.fid.domain.RobotMatchKeywords;

public interface RobotMatchKeywordsService {
	
    public List<RobotMatchKeywords>  queryListByParam(Map<String,Object> param);
	
	List<RobotMatchKeywords> queryListByParamFormRedis(RobotMatchKeywords t, String columnName, Collection<Object> columnValues);
	
	List<RobotMatchKeywords> queryListAllFormRedis(RobotMatchKeywords t);
	
	public Boolean insertDBWithCache(RobotMatchKeywords t);
	
	public Boolean deleteByPrimaryKeyDBWithCache(Class<RobotMatchKeywords> clz,Long id);
	
	public Boolean updateByPrimaryKeyDBWithCache(RobotMatchKeywords t);
	
	public int insert(RobotMatchKeywords t);

}
