package com.fid.service.match;

import java.util.Map;

public interface ResultService {
	/**
	 * 得到匹配的结果
	 * @param contentData
	 * @return
	 */
	public Map<String,Object> getAllResult(String contentData,Integer dataType);

}
