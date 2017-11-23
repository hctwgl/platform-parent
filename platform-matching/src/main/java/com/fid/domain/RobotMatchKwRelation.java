package com.fid.domain;

public class RobotMatchKwRelation {
    private Long id;

    private Long kId;

    private Long wId;

    private Integer missionid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getkId() {
        return kId;
    }

    public void setkId(Long kId) {
        this.kId = kId;
    }

    public Long getwId() {
        return wId;
    }

    public void setwId(Long wId) {
        this.wId = wId;
    }

    public Integer getMissionid() {
        return missionid;
    }

    public void setMissionid(Integer missionid) {
        this.missionid = missionid;
    }

	@Override
	public String toString() {
		return "RobotMatchKwRelation [id=" + id + ", kId=" + kId + ", wId=" + wId + ", missionid=" + missionid + "]";
	}
    
}