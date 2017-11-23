package com.fid.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fid.domain.vo.StockVo;
import com.fid.util.JsoupUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CrawlerUtil {

	// 个股信息URL
	public final static String stockURL = "http://data.news.21fid.com/fidnews/v1/fid/ggStockListInfo";
	public final static String user = "fidinner";
	public final static String key = "ab54eae187cd5cf4e89fed7a4e62586e";

	/**
	 * 个股url获取
	 */
	public static List<StockVo> getStockListInfo() {
		List<StockVo> resultList = new ArrayList<>();
		try {
			Map<String, String> param = new HashMap<>();
			param.put("user", user);
			param.put("key", key);
			String jsonByUrl = JsoupUtil.getJSONByUrlForGet(stockURL, param);
//			System.out.println(jsonByUrl);
			JSONObject parseObject = JSONObject.fromObject(jsonByUrl);
			if (parseObject != null) {
				JSONArray jsonArray = parseObject.getJSONArray("data");
				StockVo stockVo = null;
				for (int i = 0; i < jsonArray.size(); i++) {
					stockVo = new StockVo();
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String stockCode = jsonObject.getString("code");
					String stockWindCode = jsonObject.getString("windCode");
					stockVo.setStockCode(stockCode);
					stockVo.setStockWindCode(stockWindCode);
					resultList.add(stockVo);
				}
			}
			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		List<StockVo> zyjStock = CrawlerUtil.getStockListInfo();
		System.out.println(zyjStock.get(0).getStockWindCode());
	}
}
