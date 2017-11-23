package com.fid.domain.match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fid.common.param.RbsCommonParams;
import com.fid.domain.RobotMatchKeywords;


public class ResultKeyWords {
	
	private RobotMatchKeywords robotMatchKeywords;
	//标签词名
	private Map<String,List<WordsCoor>> coorSet;	
	private List<ResultPhrase> phrases;
	private Set<String> tagsSet;
	private String tagsString;
	private String  result;
//	private Set<Long> picIds;
	
	public RobotMatchKeywords getRobotMatchKeywords() {
		return robotMatchKeywords;
	}

	public void setRobotMatchKeywords(RobotMatchKeywords robotMatchKeywords) {
		this.robotMatchKeywords = robotMatchKeywords;
	}
	
	public List<ResultPhrase> getPhrases() {
		return phrases;
	}

	public void setPhrases(List<ResultPhrase> phrases) {
		this.phrases = phrases;
	}
	public Map<String, List<WordsCoor>> getCoorSet() {
		if(coorSet == null)return coorSet;
		//进行第二次排序
		Map<String, List<WordsCoor>> result = new LinkedHashMap<>();
		String name = this.getRobotMatchKeywords().getName();
		String[] tagsWords = name.split(RbsCommonParams.WORD_SPLIT);
		for (String tagsWord : tagsWords) {
			if(coorSet == null)continue;
			Set<String> set = new HashSet<>();
			List<WordsCoor>  list =  this.coorSet.get(tagsWord);
			List<WordsCoor>  newList =  new ArrayList<>();
			{
				for (WordsCoor rbssCoor : list) {
					String key = rbssCoor.getBeginIndex()+"-"+rbssCoor.getEndIndex();
					if(!set.contains(key)){
						newList.add(rbssCoor);
						set.add(key);
					}
				}
			}
			result.put(tagsWord, newList);
		}
		if(coorSet == null)return coorSet;
		return coorSet;
	}
	public void setCoorSet(Map<String, List<WordsCoor>> coorSet) {
		this.coorSet = coorSet;
	}
	
	public Set<String> getTagsSet() {
		if(phrases == null || phrases.size() ==0)return Collections.emptySet();
		Set<String> tagSet = new HashSet<>();
		for (ResultPhrase phrase : phrases) {
			tagSet.add(phrase.getWordsString());
		}
		return this.tagsSet;
	}
	public void setTagsSet(Set<String> tagsSet) {
		this.tagsSet = tagsSet;
	}
	public String getTagsString() {
		Set<String>  tagsSet =  this.getTagsSet();
		String result = String.join(RbsCommonParams.TAG_SPLIT, tagsSet);
		this.tagsString = result;
		return tagsString;
	}
	public void setTagsString(String tagsString) {
		this.tagsString = tagsString;
	}
	
	
	public String getResult() {
		if(this.phrases != null && this.phrases.size() != 0){
			Collections.sort(this.phrases,new ResultPhrase());
			if(this.phrases.size() > 0 )this.phrases = this.phrases.subList(0, 1);
			Set<String> set = new HashSet<>();
			for (ResultPhrase rbssPhrase : phrases) {
				set.add(rbssPhrase.getResult());
			}
			this.result = String.join(RbsCommonParams.TAG_SPLIT, set);
		} else {
			this.result= "";
		}
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ResultKeyWords [robotMatchKeywords=" + robotMatchKeywords + ", coorSet=" + coorSet + ", phrases="
				+ phrases + ", tagsSet=" + tagsSet + ", tagsString=" + tagsString + ", result=" + result + "]";
	}
	
}
