package com.fid.domain.tags;

import java.io.Serializable;

public class ContentTags implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 9110137862571553760L;

	private Long id;

    private Long tagid;

    private String tagname;

    private Long msgid;

    private Integer msgtype;
    
    private String message;
    
    private Long pid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTagid() {
        return tagid;
    }

    public void setTagid(Long tagid) {
        this.tagid = tagid;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname == null ? null : tagname.trim();
    }

    public Long getMsgid() {
        return msgid;
    }

    public void setMsgid(Long msgid) {
        this.msgid = msgid;
    }

    public Integer getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(Integer msgtype) {
        this.msgtype = msgtype;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	@Override
	public String toString() {
		return "ContentTags [id=" + id + ", tagid=" + tagid + ", tagname=" + tagname + ", msgid=" + msgid + ", msgtype=" + msgtype + ", message=" + message
				+ ", pid=" + pid + "]";
	}
    
}