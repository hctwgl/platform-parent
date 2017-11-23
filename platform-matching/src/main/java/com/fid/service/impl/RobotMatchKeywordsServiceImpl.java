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
import com.fid.mapper.RobotMatchKeywordsMapper;
import com.fid.service.RobotMatchKeywordsService;
import com.fid.util.RedisAPI;
import com.fid.util.RedisEntityUtil;
@Service("robotMatchKeywordsService")
public class RobotMatchKeywordsServiceImpl implements RobotMatchKeywordsService {
	
	@Resource(name = "robotMatchKeywordsMapper")
	private RobotMatchKeywordsMapper robotMatchKeywordsMapper;

	@Override
	public List<RobotMatchKeywords> queryListByParam(Map<String, Object> param) {
		return robotMatchKeywordsMapper.queryListByParam(param);
	}

	@Override
	public List<RobotMatchKeywords> queryListByParamFormRedis(RobotMatchKeywords t, String columnName,
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
		List<RobotMatchKeywords> list  = RedisEntityUtil.getObjectByColumn(t, columnName, str);
		return list;
	}

	@Override
	public List<RobotMatchKeywords> queryListAllFormRedis(RobotMatchKeywords t) {
		List<RobotMatchKeywords>  resultList = new ArrayList<>();
		Collection<String> list =  RedisAPI.getMap(t.getClass().getSimpleName()).values();
		for (String jsonString : list) {
//			t = (RobotMatchKeywords) gson.fromJson(jsonString, t.getClass());
			t = (RobotMatchKeywords)JSON.parseObject(jsonString, t.getClass());
			resultList.add(t);
		}
		return resultList;
	}

	@Override
	public Boolean insertDBWithCache(RobotMatchKeywords t) {
		boolean success = false;
		int insert = robotMatchKeywordsMapper.insert(t);
		if (insert > 0) {
			 success = true;
		}
		RedisEntityUtil.insertEntityToRedis(t);
		return success;
	}

	@Override
	public Boolean deleteByPrimaryKeyDBWithCache(Class<RobotMatchKeywords> clz, Long id) {
		boolean success = false;
		int deleteByPrimaryKey = robotMatchKeywordsMapper.deleteByPrimaryKey(id);
		if (deleteByPrimaryKey > 0) {
			 success = true;
		}
		RedisEntityUtil.deleteEntityFromRedisById(clz, id);
		return success;
	}

	@Override
	public Boolean updateByPrimaryKeyDBWithCache(RobotMatchKeywords t) {
		robotMatchKeywordsMapper.updateByPrimaryKey(t);
		Long id = RedisEntityUtil.getObjectIdLong(t);
		RedisEntityUtil.deleteEntityFromRedisById(t.getClass(), id);
		RedisEntityUtil.insertEntityToRedis(t);
		return true;
	}

	@Override
	public int insert(RobotMatchKeywords t) {
		int insert = robotMatchKeywordsMapper.insert(t);
		return insert;
	}

}
