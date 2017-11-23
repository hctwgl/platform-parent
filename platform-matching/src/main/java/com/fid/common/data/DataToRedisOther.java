package com.fid.common.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fid.annotation.MethodTimer;
import com.fid.domain.RobotMatchKeywords;
import com.fid.domain.RobotMatchKwRelation;
import com.fid.domain.RobotMatchWords;
import com.fid.domain.RobotNlpTags;
import com.fid.service.RobotMatchKeywordsService;
import com.fid.service.RobotMatchKwRelationService;
import com.fid.service.RobotMatchWordsService;
import com.fid.service.RobotNlpTagsService;
import com.fid.util.RedisEntityUtil;

/**
 * 插入数据到Redis中
 * 
 * @description:
 * 
 */
@Component
public class DataToRedisOther {
	private static final Logger logger = LoggerFactory.getLogger(DataToRedisOther.class);

	@Autowired
	private RobotNlpTagsService robotNlpTagsService;

	@Autowired
	private RobotMatchKeywordsService robotMatchKeywordsService;

	@Autowired
	private RobotMatchKwRelationService robotMatchKwRelationService;

	@Autowired
	private RobotMatchWordsService robotMatchWordsService;

	public List<RobotMatchWords> getAllRbsWords() {
		List<RobotMatchWords> words = new ArrayList<>();
		Map<String, Object> param = new HashMap<>();
		words = robotMatchWordsService.queryListByParam(param);
		return words;
	}

	public List<RobotMatchKeywords> getAllRbsKeyWords() {
		List<RobotMatchKeywords> list = new ArrayList<>();
		Map<String, Object> param = new HashMap<>();
		list = robotMatchKeywordsService.queryListByParam(param);
		return list;
	}

	public List<RobotMatchKwRelation> getAllRbsKwRelations() {
		List<RobotMatchKwRelation> list = new ArrayList<>();
		Map<String, Object> param = new HashMap<>();
		list = robotMatchKwRelationService.queryListByParam(param);
		return list;
	}

	public List<RobotNlpTags> getAllRbsConclusionTags() {
		List<RobotNlpTags> list = new ArrayList<>();
		Map<String, Object> param = new HashMap<>();
		list = robotNlpTagsService.queryListByParam(param);
		return list;
	}

	@MethodTimer
	public void insert() {
		long beginTime = new Date().getTime();
		{
			List<RobotMatchWords> list = getAllRbsWords();
			RedisEntityUtil.insertEntityToRedisListAdmin(list);
			System.out.println("============1=========" + list.size());
		}
		{
			List<RobotMatchKeywords> list = getAllRbsKeyWords();
			RedisEntityUtil.insertEntityToRedisListAdmin(list);
			System.out.println("============2=========" + list.size());
		}
		{
			List<RobotMatchKwRelation> list = getAllRbsKwRelations();
			RedisEntityUtil.insertEntityToRedisListAdmin(list);
			System.out.println("============3=========" + list.size());
		}
		{
			List<RobotNlpTags> list = getAllRbsConclusionTags();
			RedisEntityUtil.insertEntityToRedisListAdmin(list);
			System.out.println("============4=========" + list.size());
		}

		long endTime = new Date().getTime();
		System.out.println("总共耗时秒数为:" + (endTime - beginTime) / 1000 + "秒");
	}

}
