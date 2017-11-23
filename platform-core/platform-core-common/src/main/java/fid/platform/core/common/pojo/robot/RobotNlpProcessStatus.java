package fid.platform.core.common.pojo.robot;

import java.util.Date;

public class RobotNlpProcessStatus {
    private Long id;

    private Integer processtype;

    private Integer processstatus;

    private Date createtime;

    private Long missionid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProcesstype() {
        return processtype;
    }

    public void setProcesstype(Integer processtype) {
        this.processtype = processtype;
    }

    public Integer getProcessstatus() {
        return processstatus;
    }

    public void setProcessstatus(Integer processstatus) {
        this.processstatus = processstatus;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Long getMissionid() {
        return missionid;
    }

    public void setMissionid(Long missionid) {
        this.missionid = missionid;
    }
}