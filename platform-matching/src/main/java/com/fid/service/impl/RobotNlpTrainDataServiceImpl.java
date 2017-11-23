package com.fid.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.fid.domain.RobotNlpTags;
import com.fid.domain.RobotNlpTrainData;
import com.fid.mapper.RobotNlpTrainDataMapper;
import com.fid.service.RobotNlpTrainDataService;
import com.fid.util.RedisAPI;
import com.fid.util.RedisEntityUtil;

@Service("robotNlpTrainDataService")
public class RobotNlpTrainDataServiceImpl implements RobotNlpTrainDataService {

	@Resource(name = "robotNlpTrainDataMapper")
	private RobotNlpTrainDataMapper robotNlpTrainDataMapper;

	@Override
	public List<RobotNlpTrainData> queryListByParam(Map<String, Object> param) {
		return robotNlpTrainDataMapper.queryListByParam(param);
	}

	@Override
	public List<RobotNlpTrainData> queryListByParamFormRedis(RobotNlpTrainData t, String columnName,
			Collection<Object> columnValues) {
		if(columnValues == null) {
			return null;
		}
		columnValues.remove(null);
		Set<Object> set = new HashSet<>(columnValues);
		set.remove(null);
		String[] str = new String[set.size()];
		int i = 0;
		for (Object obj : set) {
			str[i++] = obj.toString();
		}
		List<RobotNlpTrainData> list  = RedisEntityUtil.getObjectByColumn(t, columnName, str);
		return list;
	}

	@Override
	public List<RobotNlpTrainData> queryListAllFormRedis(RobotNlpTrainData t) {
		List<RobotNlpTrainData>  resultList = new ArrayList<>();
		Collection<String> list =  RedisAPI.getMap(t.getClass().getSimpleName()).values();
		for (String jsonString : list) {
//			t = (RobotNlpTrainData) gson.fromJson(jsonString, t.getClass());
			t = (RobotNlpTrainData)JSON.parseObject(jsonString, t.getClass());
			resultList.add(t);
		}
		return resultList;
	}

	@Override
	public Boolean insertDBWithCache(RobotNlpTrainData t) {
		boolean success = false;
		int insert = robotNlpTrainDataMapper.insert(t);
		if (insert > 0) {
			 success = true;
		}
		RedisEntityUtil.insertEntityToRedis(t);
		return success;
	}

	@Override
	public Boolean deleteByPrimaryKeyDBWithCache(Class<RobotNlpTrainData> clz, Long id) {
		boolean success = false;
		int deleteByPrimaryKey = robotNlpTrainDataMapper.deleteByPrimaryKey(id);
		if (deleteByPrimaryKey > 0) {
			 success = true;
		}
		RedisEntityUtil.deleteEntityFromRedisById(clz, id);
		return success;
	}

	@Override
	public Boolean updateByPrimaryKeyDBWithCache(RobotNlpTrainData t) {
		robotNlpTrainDataMapper.updateByPrimaryKey(t);
		Long id = RedisEntityUtil.getObjectIdLong(t);
		RedisEntityUtil.deleteEntityFromRedisById(t.getClass(), id);
		RedisEntityUtil.insertEntityToRedis(t);
		return true;
	}

	@Override
	public int addBatch(List<RobotNlpTrainData> list) {
		return robotNlpTrainDataMapper.addBatch(list);
	}
}
