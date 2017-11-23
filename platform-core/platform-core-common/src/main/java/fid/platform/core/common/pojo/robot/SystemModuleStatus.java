package fid.platform.core.common.pojo.robot;

import java.util.Date;

/**
 * @Package fid.platform.database.robot.pojo
 * @Author auto generated
 * @Date 2017-11-17 15:25:57
 */
public class SystemModuleStatus {

    /**
     *
     */
    private Integer id;
    /**
     * 所属模块
     */
    private Integer moduleId;

    /**
     * 模块名称
     */
    private String moduleName;
    /**
     * 模块所在机器的host(ip+端口)
     */
    private String machineHost;
    /**
     * 模块状态 0、空闲， 1、使用中，2、下线
     */
    private Integer moduleStatus;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getMachineHost() {
        return machineHost;
    }

    public void setMachineHost(String machineHost) {
        this.machineHost = machineHost;
    }

    public Integer getModuleStatus() {
        return moduleStatus;
    }

    public void setModuleStatus(Integer moduleStatus) {
        this.moduleStatus = moduleStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
