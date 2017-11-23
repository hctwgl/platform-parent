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
import com.fid.mapper.RobotNlpTagsMapper;
import com.fid.service.RobotNlpTagsService;
import com.fid.util.RedisAPI;
import com.fid.util.RedisEntityUtil;

@Service("robotNlpTagsService")
public class RobotNlpTagsServiceImpl implements RobotNlpTagsService {
	
	@Resource(name = "robotNlpTagsMapper")
	private RobotNlpTagsMapper robotNlpTagsMapper;

	@Override
	public List<RobotNlpTags> queryListByParam(Map<String, Object> param) {
		return robotNlpTagsMapper.queryListByParam(param);
	}

	@Override
	public List<RobotNlpTags> queryListByParamFormRedis(RobotNlpTags t, String columnName,
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
		List<RobotNlpTags> list  = RedisEntityUtil.getObjectByColumn(t, columnName, str);
		return list;
	}

	@Override
	public List<RobotNlpTags> queryListAllFormRedis(RobotNlpTags t) {
		List<RobotNlpTags>  resultList = new ArrayList<>();
		Collection<String> list =  RedisAPI.getMap(t.getClass().getSimpleName()).values();
		for (String jsonString : list) {
//			t = (RobotNlpTags) gson.fromJson(jsonString, t.getClass());
			t = (RobotNlpTags)JSON.parseObject(jsonString, t.getClass());
			resultList.add(t);
		}
		return resultList;
	}

	@Override
	public Boolean insertDBWithCache(RobotNlpTags t) {
		boolean success = false;
		int insert = robotNlpTagsMapper.insert(t);
		if (insert > 0) {
			 success = true;
		}
		RedisEntityUtil.insertEntityToRedis(t);
		return success;
	}

	@Override
	public Boolean deleteByPrimaryKeyDBWithCache(Class<RobotNlpTags> clz, Long id) {
		boolean success = false;
		int deleteByPrimaryKey = robotNlpTagsMapper.deleteByPrimaryKey(id);
		if (deleteByPrimaryKey > 0) {
			 success = true;
		}
		RedisEntityUtil.deleteEntityFromRedisById(clz, id);
		return success;
	}

	@Override
	public Boolean updateByPrimaryKeyDBWithCache(RobotNlpTags t) {
		robotNlpTagsMapper.updateByPrimaryKey(t);
		Long id = RedisEntityUtil.getObjectIdLong(t);
		RedisEntityUtil.deleteEntityFromRedisById(t.getClass(), id);
		RedisEntityUtil.insertEntityToRedis(t);
		return true;
	}

	@Override
	public int insert(RobotNlpTags robotNlpTags) {
		return robotNlpTagsMapper.insert(robotNlpTags);
	}

}
