package com.fid.domain;

public class RobotMatchKeywords {
    private Long id;

    private String name;

    private Long tId;

    private Integer missionid;

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
        this.name = name == null ? null : name.trim();
    }

    public Long gettId() {
        return tId;
    }

    public void settId(Long tId) {
        this.tId = tId;
    }

    public Integer getMissionid() {
        return missionid;
    }

    public void setMissionid(Integer missionid) {
        this.missionid = missionid;
    }

	@Override
	public String toString() {
		return "RobotMatchKeywords [id=" + id + ", name=" + name + ", tId=" + tId + ", missionid=" + missionid + "]";
	}
    
}