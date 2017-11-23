package com.fid.domain;

public class RobotNlpTrainData {
    private Long id;

    private Long tid;

    private Long missionid;

    private String txt;
    
    private String tName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getMissionid() {
        return missionid;
    }

    public void setMissionid(Long missionid) {
        this.missionid = missionid;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt == null ? null : txt.trim();
    }

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}

	@Override
	public String toString() {
		return "RobotNlpTrainData [id=" + id + ", tid=" + tid + ", missionid=" + missionid + ", txt=" + txt + ", tName="
				+ tName + "]";
	}
    
}