package com.fid.domain.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class CmbStockReport implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private String windCode;

    private String code;

    private String stockCnName;

    private String title;

    private Date newsTime;

    private Date publishTime;

    private String content;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWindCode() {
        return windCode;
    }

    public void setWindCode(String windCode) {
        this.windCode = windCode == null ? null : windCode.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getStockCnName() {
        return stockCnName;
    }

    public void setStockCnName(String stockCnName) {
        this.stockCnName = stockCnName == null ? null : stockCnName.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Date getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(Date newsTime) {
        this.newsTime = newsTime;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

	@Override
	public String toString() {
		return "CmbStockReport [id=" + id + ", windCode=" + windCode + ", code=" + code + ", stockCnName=" + stockCnName
				+ ", title=" + title + ", newsTime=" + newsTime + ", publishTime=" + publishTime + ", content="
				+ content + "]";
	}
     
}