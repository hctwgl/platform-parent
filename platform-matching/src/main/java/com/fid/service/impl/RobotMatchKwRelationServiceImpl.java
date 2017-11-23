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
import com.fid.domain.RobotMatchKeywords;
import com.fid.domain.RobotMatchKwRelation;
import com.fid.mapper.RobotMatchKeywordsMapper;
import com.fid.mapper.RobotMatchKwRelationMapper;
import com.fid.service.RobotMatchKwRelationService;
import com.fid.util.RedisAPI;
import com.fid.util.RedisEntityUtil;

@Service("robotMatchKwRelationService")
public class RobotMatchKwRelationServiceImpl implements RobotMatchKwRelationService {
	
	@Resource(name = "robotMatchKwRelationMapper")
	private RobotMatchKwRelationMapper robotMatchKwRelationMapper;


	@Override
	public List<RobotMatchKwRelation> queryListByParam(Map<String, Object> param) {
		return robotMatchKwRelationMapper.queryListByParam(param);
	}

	@Override
	public List<RobotMatchKwRelation> queryListByParamFormRedis(RobotMatchKwRelation t, String columnName,
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
		List<RobotMatchKwRelation> list  = RedisEntityUtil.getObjectByColumn(t, columnName, str);
		return list;
	}

	@Override
	public List<RobotMatchKwRelation> queryListAllFormRedis(RobotMatchKwRelation t) {
		List<RobotMatchKwRelation>  resultList = new ArrayList<>();
		Collection<String> list =  RedisAPI.getMap(t.getClass().getSimpleName()).values();
		for (String jsonString : list) {
//			t = (RobotMatchKwRelation) gson.fromJson(jsonString, t.getClass());
			t = (RobotMatchKwRelation)JSON.parseObject(jsonString, t.getClass());
			resultList.add(t);
		}
		return resultList;
	}

	@Override
	public Boolean insertDBWithCache(RobotMatchKwRelation t) {
		boolean success = false;
		int insert = robotMatchKwRelationMapper.insert(t);
		if (insert > 0) {
			 success = true;
		}
		RedisEntityUtil.insertEntityToRedis(t);
		return success;
	}

	@Override
	public Boolean deleteByPrimaryKeyDBWithCache(Class<RobotMatchKwRelation> clz, Long id) {
		boolean success = false;
		int deleteByPrimaryKey = robotMatchKwRelationMapper.deleteByPrimaryKey(id);
		if (deleteByPrimaryKey > 0) {
			 success = true;
		}
		RedisEntityUtil.deleteEntityFromRedisById(clz, id);
		return success;
	}

	@Override
	public Boolean updateByPrimaryKeyDBWithCache(RobotMatchKwRelation t) {
		robotMatchKwRelationMapper.updateByPrimaryKey(t);
		Long id = RedisEntityUtil.getObjectIdLong(t);
		RedisEntityUtil.deleteEntityFromRedisById(t.getClass(), id);
		RedisEntityUtil.insertEntityToRedis(t);
		return true;
	}

	@Override
	public int insert(RobotMatchKwRelation t) {
		int insert = robotMatchKwRelationMapper.insert(t);
		return insert;
	}


}
