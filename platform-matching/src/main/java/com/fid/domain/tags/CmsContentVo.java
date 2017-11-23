package com.fid.domain.tags;

import java.io.Serializable;
import java.util.Date;

public class CmsContentVo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7318920277353522216L;

	private Long msgid;

    private Date createtime;

    private Integer clazz;

    private String classname;

    private String title;

    private String source;

    private String content;

    public Long getMsgid() {
        return msgid;
    }

    public void setMsgid(Long msgid) {
        this.msgid = msgid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getClazz() {
		return clazz;
	}

	public void setClazz(Integer clazz) {
		this.clazz = clazz;
	}

	public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname == null ? null : classname.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

	@Override
	public String toString() {
		return "CmsContentVo [msgid=" + msgid + ", createtime=" + createtime + ", clazz=" + clazz + ", classname=" + classname + ", title=" + title
				+ ", source=" + source + ", content=" + content + "]";
	}

    
}