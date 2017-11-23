package com.fid.domain.match;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;


public class WordsCoor implements Comparator<WordsCoor> {
	private int beginIndex;
	private int endIndex;
	private String name;
	private String rule;

	// 优化
	private Set<String> rules = new HashSet<>();

	public int getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}
	
	public Set<String> getRules() {
		Set<String> rules = new HashSet<>();
		if(this.rule != null) rules.add(rule);
		return rules;
	}
	
	public void setRules(Set<String> rules) {
		this.rules = rules;
	}

	@Override
	public int compare(WordsCoor o1, WordsCoor o2) {
		return o1.getBeginIndex() - o2.getBeginIndex();
	}

	@Override
	public String toString() {
		return "WordsCoor [beginIndex=" + beginIndex + ", endIndex=" + endIndex + ", name=" + name + ", rule=" + rule
				+ ", rules=" + rules + "]";
	}

}
