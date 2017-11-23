package com.fid.controller.tags;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fid.service.RobotNlpTagsService;

@Controller
@RequestMapping("/fid/rTags")
public class RobotNlpTagsController {

	@Autowired
	private RobotNlpTagsService robotNlpTagsService;

	/**
	 * 查询标签
	 */
	@RequestMapping("/queryRTags")
	@ResponseBody
	public Map<String, Object> addRelatedTags(@RequestParam(required = true) Long tid,
			@RequestParam(required = true) String tagName) {

		Map<String, Object> result = new HashMap<>();
		result.put("success", false);
		Boolean isSuccess = false;
		return result;
	}

}
