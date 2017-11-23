package com.fid.util;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @ClassName JsoupUtil
 * @Description 请求url获取html的doc
 * @author hudaqiang
 * @Date 2017年5月8日 下午1:49:33
 * @version 1.0.0
 */
public class JsoupUtil {
	public static Document getHtmlDoc(String url) throws IOException{
		Document doc = Jsoup.connect(url)
							.header("User-Agent",
									"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
							.ignoreContentType(true)
							.get();
		return doc;
	}
	
	public static String getJSONByUrlForGet(String url, Map<String,String> param){
		String result = null;
		try {
			result = Jsoup.connect(url).data(param)
					.ignoreContentType(true)
					.timeout(3000)
					.maxBodySize(0)
					.get().text();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
