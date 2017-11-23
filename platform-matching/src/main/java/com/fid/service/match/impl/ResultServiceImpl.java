package com.fid.service.match.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fid.ansj.AnsjLibraryLoader;
import com.fid.common.param.RbsCommonParams;
import com.fid.domain.match.ResultNlpTags;
import com.fid.domain.RobotMatchKeywords;
import com.fid.domain.match.ResultKeyWords;
import com.fid.domain.match.WordsCoor;
import com.fid.domain.vo.TagsForContentVo;
import com.fid.service.match.DataDbToCacheService;
import com.fid.service.match.ResultMatchService;
import com.fid.service.match.ResultService;

@Service("resultService")
public class ResultServiceImpl extends AnsjLibraryLoader implements ResultService {

	private final static Logger logger = LoggerFactory.getLogger(ResultServiceImpl.class);

	// 静态缓存个词
	public static List<String> hasWordList = null;

	@Autowired
	private DataDbToCacheService dataDbToCacheService;

	@Autowired
	private ResultMatchService resultMatchService;

	@Override
	public Map<String, Object> getAllResult(String contentData,Integer dataType) {
		Map<String, Object> result = new HashMap<>();
		result.put("success", false);
		result.put("msg", "匹配失败");

		// 匹配标签结果
		Map<String, Set<String>> drJoMapDeatail = new HashMap<>();
		// 结果标签
		List<ResultNlpTags> resultLastTags = new ArrayList<ResultNlpTags>();

		// 加载个词
		if (hasWordList == null) {
			hasWordList = dataDbToCacheService.getWordsFormCache();
		}
		// logger.error(contentData+"从redis里获取所有词完毕:================="+new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		Set<String> hasWordSet = new LinkedHashSet<>(hasWordList);
		contentData = Jsoup.parse(contentData).text();
		String content = contentData;
		String contentNodeal = content;
		// contentNodeal = DealStringForMatchUtil.filterContent(contentNodeal);
		// 处理特殊字符
		content = content.replaceAll("\\s+|　+", ""); // 英文之间需要空格
		//对内容做分词处理后拼接
//		String str = "";
//		{
//			{
//				List<Term> terms = ToAnalysis.parse(content).recognition(AnsjLibraryLoader.getFilter()).getTerms();
//				System.out.println(terms);
//				for (Term term : terms) {
//					if ("".equals(str)) {
//						str = str + term.getName().toUpperCase();
//					} else {
//						str = str + "," + term.getName().toUpperCase();
//					}
//				}
//			}
//		}
//		content = str;
		content = content.replace(",", "×");
		content = content.replaceAll(RbsCommonParams.REGEX, RbsCommonParams.REGEX_DB);
		// 1.获取标签
		List<WordsCoor> coors = resultMatchService.getRbssCoor(content, contentNodeal, hasWordSet);
		Set<String> set = new HashSet<>();
		for (WordsCoor rbssCoor : coors) {
			set.add(rbssCoor.getName());
		}
		System.out.println(set);
		// 1.1.进行坐标囊括去重复
		// coors = resultMatchService.filterCoors(coors);
		Collections.sort(coors, new WordsCoor());
		// 形成词坐标的键值
		Map<String, List<WordsCoor>> wordCoors = new HashMap<>();
		{
			for (WordsCoor coor : coors) {
				Set<String> rules = coor.getRules();
				for (String rule : rules) {
					if (wordCoors.get(rule) == null) {
						wordCoors.put(rule, new ArrayList<>());
					}
					wordCoors.get(rule).add(coor);
				}
			}
		}
		Set<String> rules = resultMatchService.getRules(coors);
		logger.error("将要涉及到的词:" + rules + "=================" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		List<RobotMatchKeywords> keyWordsList = resultMatchService.getRelationTags(rules,dataType);

		keyWordsList = resultMatchService.getMatchTags(keyWordsList, rules);
		logger.error("获取标签完毕:" + "=================" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		// 2.获取组合
		List<ResultKeyWords> ResultKeyWordsList = resultMatchService.getRbssTags(keyWordsList);
		ResultKeyWordsList = resultMatchService.getParagraphRbssTags(ResultKeyWordsList, wordCoors);
		// 3.获取结论
		resultLastTags = resultMatchService.getRbssConlusionByTags(ResultKeyWordsList);
		// 对结果标签、词组进行归类
		for (ResultNlpTags resultNlpTags : resultLastTags) {
			String tagName = resultNlpTags.getRobotNlpTags().getName();
			Set<String> keywordsStrings = new HashSet<>();  //词组组合
			List<ResultKeyWords> resultKeyWordsList2 = resultNlpTags.getResultKeyWordsList();
			for (ResultKeyWords resultKeyWords : resultKeyWordsList2) {
				keywordsStrings.add(resultKeyWords.getRobotMatchKeywords().getName());
			}
			drJoMapDeatail.put(tagName, keywordsStrings);
		}
		
		List<TagsForContentVo> resultTags = new ArrayList<>();
		for (ResultNlpTags resultNlpTags : resultLastTags) {
			TagsForContentVo tagsForContentVo = new TagsForContentVo();
			tagsForContentVo.setId(resultNlpTags.getRobotNlpTags().getId());
			tagsForContentVo.setName(resultNlpTags.getRobotNlpTags().getName());
			resultTags.add(tagsForContentVo);
		}
		// 4.返回结果封装
		result.put("data", drJoMapDeatail);
		result.put("labelData", resultTags);
		result.put("success", true);
		result.put("msg", "匹配成功");
		logger.error(result.toString());
		return result;
	}

}
