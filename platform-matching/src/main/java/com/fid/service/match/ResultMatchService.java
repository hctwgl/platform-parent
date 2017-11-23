package com.fid.service.match;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fid.domain.match.ResultNlpTags;
import com.fid.domain.RobotMatchKeywords;
import com.fid.domain.match.ResultKeyWords;
import com.fid.domain.match.WordsCoor;

public interface ResultMatchService {

	// 1.获取坐标信息
	public List<WordsCoor> getRbssCoor(String content, String contentNodeal, Set<String> hasWordList);

	// 2.获取所有规则
	public Set<String> getRules(List<WordsCoor> coors);

	// 3.获取所有的关键词组
	public List<RobotMatchKeywords> getRelationTags(Set<String> rule,Integer dataType);

	// 4.筛选出全符合的关键词组 每个规则都存在
	public List<RobotMatchKeywords> getMatchTags(List<RobotMatchKeywords> rbsTags, Set<String> allRules);

	// 5.获取每个标签匹配内容 对tags进行处理,获取每个标签在规则中的坐标数
	public List<ResultKeyWords> getRbssTags(List<RobotMatchKeywords> rbsTags);

	// 6.获取标签可能
	public List<ResultKeyWords> getParagraphRbssTags(List<ResultKeyWords> rbssTagsList,
			Map<String, List<WordsCoor>> wordCoors);

	// 7.筛选:只保留有序的短语
	public List<ResultKeyWords> filterOrderParagraphRbssTags(List<ResultKeyWords> rbssTagsList);

	// 8.获取结论
	public List<ResultNlpTags> getRbssConlusionByTags(List<ResultKeyWords> rbssTagsList);

	// 9.对结论中的数据进行判断，拼接上父节点跟关联标签内容
	public List<ResultNlpTags> filterConclusionTags(List<ResultNlpTags> conclusionList);

	// 10.对结论中的数据进行判断，去噪:去掉只有个股路径，但不包含个股信息的标签
//	public List<ResultChildTags> filterStocksDriver(List<ResultChildTags> rbssConclusionListResult,
//			String contentNodeal);

	// 11.获取json内容
	public List<Map<String, Object>> getConclusionResult(List<ResultNlpTags> rbssConclusionList,
			Map<String,Set<String>> aMap,Map<String,Set<String>> rMap,List<Map<String, Object>> map);

}
