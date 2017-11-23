package com.fid.service.match.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fid.annotation.MethodTimer;
import com.fid.domain.RobotMatchWords;
import com.fid.service.RobotMatchWordsService;
import com.fid.service.match.DataDbToCacheService;
import com.fid.util.ReadPropertyUtil;
import com.fid.util.RedisAPI;
import com.fid.util.StringUtil;

@Service
public class DataDbToCacheServiceImpl implements DataDbToCacheService {

	public static final String MATCH_WORDS_NAME = "MATCH_WORDS_NAME";

	public static final String HQ_STOCK_CODE = "HQ_STOCK_CODE";

	@Autowired
	private RobotMatchWordsService robotMatchWordsService;

	@PostConstruct
	@Override
	public boolean wordsToCache() {
		if (!"true".equals(ReadPropertyUtil.getPropertyByName("redis.dataload")))
			return false;
		Map<String, Object> param = new HashMap<>();
		// 加载个词缓存
		{
			boolean isExist = RedisAPI.exist(MATCH_WORDS_NAME);
//			if (!isExist) {
				List<RobotMatchWords> wordsModel = robotMatchWordsService.queryListByParam(param);
				for (RobotMatchWords rbsWords : wordsModel) {
					String words = rbsWords.getName();
					if (StringUtil.isBlank(words)) continue;
					RedisAPI.set(MATCH_WORDS_NAME, words);
				}
//			}
		}
		return true;
	}

	public Set<String> getMapWordsFormCache() {
		Set<String> setWords = RedisAPI.getSet(MATCH_WORDS_NAME);
		return setWords;
	}

	public List<String> getWordsFormCache() {
		List<String> keys = new ArrayList<>();
		Set<String> setWords = this.getMapWordsFormCache();
		if (setWords != null) {
			keys.addAll(setWords);
		}
		return keys;
	}
	
	public static void main(String[] args) {
		String words = "发 发的说法是,发顺丰  盛大发售1,大师傅";
		for (String word : words.split("\\s+|,")) {
			System.out.println(word);
		}
	}


}
