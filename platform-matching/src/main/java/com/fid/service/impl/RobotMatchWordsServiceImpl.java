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
import com.fid.domain.RobotMatchKwRelation;
import com.fid.domain.RobotMatchWords;
import com.fid.mapper.RobotMatchWordsMapper;
import com.fid.service.RobotMatchWordsService;
import com.fid.util.RedisAPI;
import com.fid.util.RedisEntityUtil;

@Service("robotMatchWordsService")
public class RobotMatchWordsServiceImpl implements RobotMatchWordsService {
	
	@Resource(name = "robotMatchWordsMapper")
	private RobotMatchWordsMapper robotMatchWordsMapper;


	@Override
	public List<RobotMatchWords> queryListByParam(Map<String, Object> param) {
		return robotMatchWordsMapper.queryListByParam(param);
	}

	@Override
	public List<RobotMatchWords> queryListByParamFormRedis(RobotMatchWords t, String columnName,
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
		List<RobotMatchWords> list  = RedisEntityUtil.getObjectByColumn(t, columnName, str);
		return list;
	}

	@Override
	public List<RobotMatchWords> queryListAllFormRedis(RobotMatchWords t) {
		List<RobotMatchWords>  resultList = new ArrayList<>();
		Collection<String> list =  RedisAPI.getMap(t.getClass().getSimpleName()).values();
		for (String jsonString : list) {
//			t = (RobotMatchWords) gson.fromJson(jsonString, t.getClass());
			t = (RobotMatchWords)JSON.parseObject(jsonString, t.getClass());
			resultList.add(t);
		}
		return resultList;
	}

	@Override
	public Boolean insertDBWithCache(RobotMatchWords t) {
		boolean success = false;
		int insert = robotMatchWordsMapper.insert(t);
		if (insert > 0) {
			 success = true;
		}
		RedisEntityUtil.insertEntityToRedis(t);
		return success;
	}

	@Override
	public Boolean deleteByPrimaryKeyDBWithCache(Class<RobotMatchWords> clz, Long id) {
		boolean success = false;
		int deleteByPrimaryKey = robotMatchWordsMapper.deleteByPrimaryKey(id);
		if (deleteByPrimaryKey > 0) {
			 success = true;
		}
		RedisEntityUtil.deleteEntityFromRedisById(clz, id);
		return success;
	}

	@Override
	public Boolean updateByPrimaryKeyDBWithCache(RobotMatchWords t) {
		robotMatchWordsMapper.updateByPrimaryKey(t);
		Long id = RedisEntityUtil.getObjectIdLong(t);
		RedisEntityUtil.deleteEntityFromRedisById(t.getClass(), id);
		RedisEntityUtil.insertEntityToRedis(t);
		return true;
	}

	@Override
	public int insertBatch(List<RobotMatchWords> ts) {
		return robotMatchWordsMapper.insertBatch(ts);
	}

	@Override
	public int insert(RobotMatchWords tMatchWords) {
		return robotMatchWordsMapper.insert(tMatchWords);
	}

}
