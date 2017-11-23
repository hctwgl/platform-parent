package com.fid.domain;

public class RobotMatchWords {
    private Long id;

    private String name;

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

    public Integer getMissionid() {
        return missionid;
    }

    public void setMissionid(Integer missionid) {
        this.missionid = missionid;
    }

	@Override
	public String toString() {
		return "RobotMatchWords [id=" + id + ", name=" + name + ", missionid=" + missionid + "]";
	}
    
}