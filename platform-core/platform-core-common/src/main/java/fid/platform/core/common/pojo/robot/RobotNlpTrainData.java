package fid.platform.core.common.pojo.robot;

public class RobotNlpTrainData {
    private Long id;

    private Long tid;

    private Long missionid;

    private String txt;

    private String tname;

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

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = txt == null ? null : tname.trim();
    }
}