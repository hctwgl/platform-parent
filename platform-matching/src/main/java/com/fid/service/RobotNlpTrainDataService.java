package com.fid.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.fid.domain.RobotNlpTrainData;

public interface RobotNlpTrainDataService {
	
	public List<RobotNlpTrainData> queryListByParam(Map<String, Object> param);

	List<RobotNlpTrainData> queryListByParamFormRedis(RobotNlpTrainData t, String columnName,
			Collection<Object> columnValues);

	List<RobotNlpTrainData> queryListAllFormRedis(RobotNlpTrainData t);

	public Boolean insertDBWithCache(RobotNlpTrainData t);

	public Boolean deleteByPrimaryKeyDBWithCache(Class<RobotNlpTrainData> clz, Long id);

	public Boolean updateByPrimaryKeyDBWithCache(RobotNlpTrainData t);
	
	public int addBatch(List<RobotNlpTrainData> list);


}
