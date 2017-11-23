package com.fid.controller.matching;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fid.common.data.DataToRedis;
import com.fid.common.data.DataToRedisOther;
import com.fid.service.match.ResultService;

@Controller
@RequestMapping("/fid")
public class MatchTagsResultController {

	@Autowired
	private ResultService resultService;
	
	@Autowired
	private DataToRedisOther dataToRedisOther;

	/**
	 * 规则: 1.处理特殊字符
	 * 
	 * @param content
	 * @return
	 */
	@RequestMapping(value = "/getMatchingTags")
	@ResponseBody
	public synchronized Map<String, Object> getMatchingTags(@RequestParam(required = false) String content,
			@RequestParam(required = false) Integer dataType) {
		try {
			Map<String, Object> result = resultService.getAllResult(content,dataType);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}

	/**
	 * 规则: 1.导数据
	 * 
	 * @param content
	 * @return
	 */
	@RequestMapping(value = "/insertRedis")
	@ResponseBody
	public Map<String, Object> insertRedis() {
		dataToRedisOther.insert();
		Map<String, Object> result = new HashMap<>();
		result.put("msg", "操作成功!");
		result.put("status", "true");
		return result;
	}

}
