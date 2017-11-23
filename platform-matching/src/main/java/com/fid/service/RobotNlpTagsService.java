package com.fid.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.fid.domain.RobotNlpTags;


public interface RobotNlpTagsService {
	
	public int insert(RobotNlpTags robotNlpTags);

	public List<RobotNlpTags> queryListByParam(Map<String, Object> param);

	List<RobotNlpTags> queryListByParamFormRedis(RobotNlpTags t, String columnName,
			Collection<Object> columnValues);

	List<RobotNlpTags> queryListAllFormRedis(RobotNlpTags t);

	public Boolean insertDBWithCache(RobotNlpTags t);

	public Boolean deleteByPrimaryKeyDBWithCache(Class<RobotNlpTags> clz, Long id);

	public Boolean updateByPrimaryKeyDBWithCache(RobotNlpTags t);

}
