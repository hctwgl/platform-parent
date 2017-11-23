package com.fid.service.match.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.css.ElementCSSInlineStyle;

import com.alibaba.fastjson.JSONObject;
import com.fid.common.param.RbsCommonParams;
import com.fid.domain.match.ResultPhrase;
import com.fid.domain.match.ResultNlpTags;
import com.fid.domain.RobotMatchKeywords;
import com.fid.domain.RobotMatchKwRelation;
import com.fid.domain.RobotMatchWords;
import com.fid.domain.RobotNlpTags;
import com.fid.domain.match.ResultKeyWords;
import com.fid.domain.match.WordsCoor;
import com.fid.service.RobotMatchKeywordsService;
import com.fid.service.RobotMatchKwRelationService;
import com.fid.service.RobotMatchWordsService;
import com.fid.service.RobotNlpTagsService;
import com.fid.service.match.ResultMatchService;
import com.fid.util.StringUtil;

@Service
public class ResultMatchServiceImpl implements ResultMatchService {
	
	@Autowired
	private RobotMatchWordsService robotMatchWordsService;
	
	@Autowired
	private RobotMatchKeywordsService robotMatchKeywordsService;
	
	@Autowired
	private RobotMatchKwRelationService robotMatchKwRelationService;
	
	@Autowired
	private RobotNlpTagsService robotNlpTagsService;


	private List<WordsCoor> coors;

	@Override
	public List<WordsCoor> getRbssCoor(String content, String contentNodeal, Set<String> hasWordList) {
		List<WordsCoor> coors = new ArrayList<>();
		if (content.contains("-")) {
			content = content.replace("-", "\\-");
		}
		if (content.contains("+")) {
			content = content.replace("+", "\\+");
		}
		for (String rule : hasWordList) {
			if (rule.equals("+") || StringUtil.isBlank(rule))
				continue;
			if (rule.contains(RbsCommonParams.REGEX)) {
				if (!content.contains(rule))
					continue;
				try {
					Matcher m = Pattern.compile(rule).matcher(contentNodeal);
					long beginTime = new Date().getTime();
					while (m.find() && new Date().getTime() - beginTime < 100) {
						WordsCoor rbssCoor = new WordsCoor();
						rbssCoor.setBeginIndex(m.start());
						rbssCoor.setEndIndex(m.end());
						rbssCoor.setName(m.group());
						rbssCoor.setRule(rule);
						coors.add(rbssCoor);
					}
				} catch (Exception e) {
					System.out.println("正则表达式匹配错误，表达式为:  " + rule);
					e.printStackTrace();
				}
			} else {
				if (!contentNodeal.contains(rule))
					continue;
				int beginIndex = 0;
				long beginTime = new Date().getTime();
				while ((beginIndex = contentNodeal.indexOf(rule, beginIndex)) != -1 && new Date().getTime() - beginTime < 2000) {
					WordsCoor rbssCoor = new WordsCoor();
					rbssCoor.setBeginIndex(beginIndex);
					rbssCoor.setEndIndex(beginIndex + rule.length());
					rbssCoor.setName(rule);
					rbssCoor.setRule(rule);
					beginIndex = rbssCoor.getEndIndex() + 1;
					coors.add(rbssCoor);
				}
			}
		}
		this.coors = coors;
		return coors;
	}

	@Override
	public Set<String> getRules(List<WordsCoor> coors) {
		Set<String> result = new LinkedHashSet<>();
		if (coors == null || coors.size() == 0)
			return result;
		for (WordsCoor coor : coors) {
			if (coor.getRule() == null)
				continue;
			result.add(coor.getRule());
		}
		return result;
	}

	@Override
	public List<RobotMatchKeywords> getRelationTags(Set<String> rule,Integer dataType) {
		Set<String> rules = new HashSet<>(rule);
		{
			// 移除rules包含数字的数据
			Iterator<String> it = rules.iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (key.contains(RbsCommonParams.REGEX_DB)) {
					it.remove();
				}
			}

		}
		// logger.error("开始获取词:================="+new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		List<RobotMatchKeywords> result = new ArrayList<>();
		if (rules == null || rules.size() == 0)
			return result;
		List<RobotMatchWords> wordsModel = robotMatchWordsService.queryListByParamFormRedis(new RobotMatchWords(), "name", new HashSet<>(rules));
		Set<Long> wordsId = new HashSet<Long>();
		//数据类型过滤
		if (dataType != null) {
			for (RobotMatchWords rbsWord : wordsModel) {
				Integer missionid = rbsWord.getMissionid();
				if (missionid == dataType) {
					wordsId.add(rbsWord.getId());
				}
			}
		}else {
			return result;
		}
		// logger.error("开始获取词组:================="+new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		List<RobotMatchKwRelation> kWordsModel = robotMatchKwRelationService.queryListByParamFormRedis(new RobotMatchKwRelation(), "wId", new HashSet<>(wordsId));
		Set<Long> kwId = new HashSet<Long>();
		for (RobotMatchKwRelation kWords : kWordsModel) {
			kwId.add(kWords.getkId());
		}
		// logger.error("开始获取词组组合:================="+new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		List<RobotMatchKeywords> tagsModel = robotMatchKeywordsService.queryListByParamFormRedis(new RobotMatchKeywords(), "id", new HashSet<>(kwId));
		result = tagsModel;
		// logger.error("获取完毕:================="+new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		return result;
	}

	@Override
	public List<RobotMatchKeywords> getMatchTags(List<RobotMatchKeywords> rbsTags, Set<String> allRules) {
		if (rbsTags == null || rbsTags.size() == 0)
			return Collections.emptyList();
		
		List<RobotMatchKeywords> result = new ArrayList<>();
		for (RobotMatchKeywords rbsTag : rbsTags) {
			if (rbsTag.getName() == null)
				continue;
			List<String> rulesSet = Arrays.asList(rbsTag.getName().split(RbsCommonParams.WORD_SPLIT));
			if (allRules.containsAll(rulesSet)) {
				result.add(rbsTag);
			}
		}
		return result;
	}

	@Override
	public List<ResultKeyWords> getRbssTags(List<RobotMatchKeywords> rbsTags) {
		if (rbsTags == null || rbsTags.size() == 0)
			return Collections.emptyList();
		List<ResultKeyWords> result = new ArrayList<>();
		for (RobotMatchKeywords rbsTag : rbsTags) {
			ResultKeyWords rbssTags = new ResultKeyWords();
			rbssTags.setRobotMatchKeywords(rbsTag);
			result.add(rbssTags);
		}
		return result;
	}

	@Override
	public List<ResultKeyWords> getParagraphRbssTags(List<ResultKeyWords> rbssTagsList, Map<String, List<WordsCoor>> wordCoors) {
		List<ResultKeyWords> result = new ArrayList<>();
		if (rbssTagsList == null || rbssTagsList.size() == 0)
			return result;
		for (ResultKeyWords rbssTags : rbssTagsList) {
			String[] tagsWords = rbssTags.getRobotMatchKeywords().getName().split(RbsCommonParams.WORD_SPLIT);
			List<List<WordsCoor>> coors = new ArrayList<>();
			for (String rule : tagsWords) {
				List<WordsCoor> rbssCoors = wordCoors.get(rule);
				coors = this.getRbssCoorListList(rbssCoors, coors);
				if (coors.size() == 0) {
					coors.clear();
					break;
				}
			}
			List<ResultPhrase> phraseList = new ArrayList<>();
			for (List<WordsCoor> coorList : coors) {
				ResultPhrase phrase = new ResultPhrase();
				phrase.setCoors(coorList);
				phraseList.add(phrase);
			}

			if (phraseList.size() == 0 || phraseList == null)
				continue;
			rbssTags.setPhrases(phraseList);
			result.add(rbssTags);
		}
		
		return result;
	}

	@Override
	public List<ResultKeyWords> filterOrderParagraphRbssTags(List<ResultKeyWords> rbssTagsList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ResultNlpTags> getRbssConlusionByTags(List<ResultKeyWords> rbssTagsList) {
		List<ResultNlpTags> result = new ArrayList<>();
		if (rbssTagsList == null)
			return result;
		//tagsId跟词组Map
		Map<Long, List<ResultKeyWords>> tagIdKeywordsMap = new HashMap<>();
		Set<Long> tagsIdsSet = new HashSet<>();
		for (ResultKeyWords tags : rbssTagsList) {
			if (tags.getRobotMatchKeywords() == null)
				continue;
			Long gettId = tags.getRobotMatchKeywords().gettId();
			tagsIdsSet.add(gettId);
			if (tagIdKeywordsMap.get(gettId) == null) {
				tagIdKeywordsMap.put(gettId, new ArrayList<>());
			}
			tagIdKeywordsMap.get(gettId).add(tags);
		}
		// 词组标签关系得到所有标签
		List<RobotNlpTags> modelTags = robotNlpTagsService.queryListByParamFormRedis(new RobotNlpTags(), "id", new HashSet<>(tagsIdsSet));
		// 标签id,和类
		Map<Long, RobotNlpTags> idTagsMap = new HashMap<>();
		//一级标签
		List<RobotNlpTags> robotNlpTagsList = new ArrayList<>(); 
		
		//筛选出所有非一级标签
		Set<Long> idSet = new HashSet<>();
		for (RobotNlpTags tags : modelTags) {
			idTagsMap.put(tags.getId(), tags);
			Long pid = tags.getPid();
			if (pid != null && !"".equals(pid)) {
				idSet.add(tags.getId());
			}else {
				robotNlpTagsList.add(tags);
			}
		}
		//一级二级标签同时存在、只取二级标签
		Iterator<RobotNlpTags> iterator = robotNlpTagsList.iterator();
	    while (iterator.hasNext()) {
			RobotNlpTags robotNlpTags = iterator.next();
			Long id = robotNlpTags.getId();
			for (Long childId : idSet) {
				RobotNlpTags robotNlpTags2 = idTagsMap.get(childId);
				if (robotNlpTags2.getPid().equals(id)) {
					iterator.remove();
					break;
				}
			}
		}
	    //结果标签
	    if (robotNlpTagsList.size() > 0) {
	    	ResultNlpTags resultNlpTags = null;
	    	for (RobotNlpTags robotNlpTags : robotNlpTagsList) {
	    		resultNlpTags = new ResultNlpTags();
	    		resultNlpTags.setRobotNlpTags(robotNlpTags);
	    		resultNlpTags.setResultKeyWordsList(tagIdKeywordsMap.get(robotNlpTags.getId()));
	    		result.add(resultNlpTags);
			}
	    }
	    if (idSet.size() > 0) {
	    	ResultNlpTags resultNlpTags = null;
	    	for (Long childId : idSet) {
				RobotNlpTags robotNlpTags = idTagsMap.get(childId);
				resultNlpTags = new ResultNlpTags();
				resultNlpTags.setRobotNlpTags(robotNlpTags);
				resultNlpTags.setResultKeyWordsList(tagIdKeywordsMap.get(robotNlpTags.getId()));
				result.add(resultNlpTags);
			}
	    }
		return result;
	}
	

	@Override
	public List<ResultNlpTags> filterConclusionTags(List<ResultNlpTags> resultChildTagsList) {
		// 返回结果
		List<ResultNlpTags> result = new ArrayList<>();
		
		return result;
	}

	@Override
	public List<Map<String, Object>> getConclusionResult(List<ResultNlpTags> resultChildTagsList, Map<String, Set<String>> tagsString,
			Map<String, Set<String>> rTagsString, List<Map<String, Object>> result) {
		if (result == null)
			result = new ArrayList<>();
		
		System.out.println(JSONObject.toJSONString(result));
		return result;
	}

	/**
	 *
	 * @param list
	 * @return
	 */
	public List<List<WordsCoor>> getRbssCoorListList(List<WordsCoor> list, List<List<WordsCoor>> result) {
		List<List<WordsCoor>> resultDeal = new ArrayList<>();
		if (list == null || list.size() == 0) {
			if (result == null || result.size() == 0) {
				return resultDeal;
			}
			return result;
		}
		if (result == null || result.size() == 0) {
			result = new ArrayList<>();
			for (WordsCoor coor : list) {
				List<WordsCoor> listOne = new ArrayList<>();
				listOne.add(coor);
				resultDeal.add(listOne);
			}
		} else {
			for (List<WordsCoor> resultOneList : result) {
				for (WordsCoor coor : list) {
					List<WordsCoor> moreList = new ArrayList<>(resultOneList);
					if (coor.getBeginIndex() <= moreList.get(moreList.size() - 1).getBeginIndex())
						continue;
					moreList.add(coor);
					resultDeal.add(moreList);
				}
			}
		}

		return resultDeal;
	}

}
