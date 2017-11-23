package com.fid.domain.match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fid.common.param.RbsCommonParams;


public class ResultPhrase implements Comparator<ResultPhrase>{
	private List<WordsCoor> coors;
	private List<String> words;
	private String wordsString;
	//词之间的距离
    private Integer distance;
	
	private String result;

	public List<WordsCoor> getCoors() {
		return coors;
	}

	public void setCoors(List<WordsCoor> coors) {
		this.coors = coors;
	}

	public List<String> getWords() {
		List<String> wordList = new ArrayList<>();
		if(coors == null || coors.size() == 0) return Collections.emptyList(); 
		for (WordsCoor rbssCoor : coors) {
			String name = rbssCoor.getName();
			wordList.add(name);
		}
		this.words = wordList;
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}

	public String getWordsString() {
		this.words = this.getWords();
		if(this.words != null){
			String result = String.join(RbsCommonParams.WORD_SPLIT, this.words);
			wordsString = result;
		}
		return wordsString;
	}

	public void setWordsString(String wordsString) {
		this.wordsString = wordsString;
	}
	
	
	
	public String getResult() {
		if(this.coors != null && this.coors.size() != 0 ){
			List<String> coorNameList= new ArrayList<>();
			for (WordsCoor rbssCoor : coors) {
				coorNameList.add(rbssCoor.getName());
			}
			this.result = String.join(RbsCommonParams.WORD_SPLIT, coorNameList);
		}
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public Integer getDistance() {
		if(distance != null )return this.distance;
		if(coors == null) return this.distance;
		int end = 0;
		int sum = 0;
		for (WordsCoor coor : coors) {
			if(end == 0)  end = coor.getBeginIndex();
			sum += coor.getBeginIndex() - end;
			end = coor.getEndIndex();
		}
		this.distance = sum;
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	
	@Override
	public int compare(ResultPhrase o1, ResultPhrase o2) {
//		if(o1.getDistance() < 0 || o2.getDistance() < 0 || o1.getDistance() == o2.getDistance()) return 0;
		return o2.getDistance() - o1.getDistance();
	}

	@Override
	public String toString() {
		return "RbssPhrase [coors=" + coors + ", words=" + words + ", wordsString=" + wordsString + ", distance="
				+ distance + ", result=" + result + "]";
	}
	
}

