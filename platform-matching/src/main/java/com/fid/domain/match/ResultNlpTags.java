package com.fid.domain.match;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fid.domain.RobotMatchKeywords;
import com.fid.domain.RobotNlpTags;

public class ResultNlpTags {

	private RobotNlpTags robotNlpTags;
	private List<RobotMatchKeywords> keyWordsList;
	private List<ResultKeyWords> resultKeyWordsList;
	// private List<Long> picIdList;
	private Set<String> tagsResult;


	public List<RobotMatchKeywords> getKeyWordsList() {
		List<ResultKeyWords> resultKeyWordsList = this.getResultKeyWordsList();
		List<RobotMatchKeywords> result = new ArrayList<>();
		if (resultKeyWordsList != null) {
			for (ResultKeyWords resultKeyWords : resultKeyWordsList) {
				result.add(resultKeyWords.getRobotMatchKeywords());
			}
			this.keyWordsList = result;
		}
		return keyWordsList;
	}

	public void setKeyWordsList(List<RobotMatchKeywords> keyWordsList) {
		this.keyWordsList = keyWordsList;
	}

	public List<ResultKeyWords> getResultKeyWordsList() {
		return resultKeyWordsList;
	}

	public void setResultKeyWordsList(List<ResultKeyWords> resultKeyWordsList) {
		this.resultKeyWordsList = resultKeyWordsList;
	}

	public Set<String> getTagsResult() {
		return tagsResult;
	}

	public void setTagsResult(Set<String> tagsResult) {
		this.tagsResult = tagsResult;
	}

	public RobotNlpTags getRobotNlpTags() {
		return robotNlpTags;
	}

	public void setRobotNlpTags(RobotNlpTags robotNlpTags) {
		this.robotNlpTags = robotNlpTags;
	}

	@Override
	public String toString() {
		return "ResultNlpTags [robotNlpTags=" + robotNlpTags + ", keyWordsList=" + keyWordsList
				+ ", resultKeyWordsList=" + resultKeyWordsList + ", tagsResult=" + tagsResult + "]";
	}

}
