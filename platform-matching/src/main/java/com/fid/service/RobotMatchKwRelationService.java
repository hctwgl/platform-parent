package com.fid.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.fid.domain.RobotMatchKwRelation;

public interface RobotMatchKwRelationService {
	
//    public int insertBatch(List<RobotMatchKwRelation> ts);
	
	public List<RobotMatchKwRelation>  queryListByParam(Map<String,Object> param);
	
	List<RobotMatchKwRelation> queryListByParamFormRedis(RobotMatchKwRelation t, String columnName, Collection<Object> columnValues);
	
	List<RobotMatchKwRelation> queryListAllFormRedis(RobotMatchKwRelation t);
	
	public Boolean insertDBWithCache(RobotMatchKwRelation t);
	
	public Boolean deleteByPrimaryKeyDBWithCache(Class<RobotMatchKwRelation> clz,Long id);
	
	public Boolean updateByPrimaryKeyDBWithCache(RobotMatchKwRelation t);
	
	public int insert(RobotMatchKwRelation t);


}
