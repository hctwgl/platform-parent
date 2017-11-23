package com.fid.domain;

public class RobotNlpTags {
    private Long id;

    private String name;

    private Long pid;

    private Long missionid;

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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getMissionid() {
        return missionid;
    }

    public void setMissionid(Long missionid) {
        this.missionid = missionid;
    }

	@Override
	public String toString() {
		return "RobotNlpTags [id=" + id + ", name=" + name + ", pid=" + pid + ", missionid=" + missionid + "]";
	}
    
}