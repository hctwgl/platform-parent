package com.fid.domain.vo;

public class TagsForContentVo {
	
	private Long id;  //标签id

    private String name;
    
    private Long pid;  //上一级标签id

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	@Override
	public String toString() {
		return "ChildTagsForContentVo [id=" + id + ", name=" + name + ", pid=" + pid + "]";
	}
    
}
