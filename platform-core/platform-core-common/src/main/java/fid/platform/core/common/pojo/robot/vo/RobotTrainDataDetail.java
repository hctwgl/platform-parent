package fid.platform.core.common.pojo.robot.vo;

import java.util.Date;

/**
 * Created by mengtian on 2017/11/21
 */
public class RobotTrainDataDetail {
    private Long missionId;
    private Integer processStatus;
    private Date createTime;

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
