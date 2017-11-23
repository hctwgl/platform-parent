package com.fid.domain.vo;

import java.io.Serializable;

public class SearchVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2267603287479104479L;
	
	private String parentTagName;
	
	private String childTagName;
	
	private String keyWordName;
	
	private Long keywordId;
	
	private Long childTagId;
	
	private Long parentTagId;

	public String getParentTagName() {
		return parentTagName;
	}

	public void setParentTagName(String parentTagName) {
		this.parentTagName = parentTagName;
	}

	public String getChildTagName() {
		return childTagName;
	}

	public void setChildTagName(String childTagName) {
		this.childTagName = childTagName;
	}

	public String getKeyWordName() {
		return keyWordName;
	}

	public void setKeyWordName(String keyWordName) {
		this.keyWordName = keyWordName;
	}

	public Long getKeywordId() {
		return keywordId;
	}

	public void setKeywordId(Long keywordId) {
		this.keywordId = keywordId;
	}

	public Long getChildTagId() {
		return childTagId;
	}

	public void setChildTagId(Long childTagId) {
		this.childTagId = childTagId;
	}

	public Long getParentTagId() {
		return parentTagId;
	}

	public void setParentTagId(Long parentTagId) {
		this.parentTagId = parentTagId;
	}

	@Override
	public String toString() {
		return "SearchVo [parentTagName=" + parentTagName + ", childTagName=" + childTagName + ", keyWordName="
				+ keyWordName + ", keywordId=" + keywordId + ", childTagId=" + childTagId + ", parentTagId="
				+ parentTagId + "]";
	}


}
