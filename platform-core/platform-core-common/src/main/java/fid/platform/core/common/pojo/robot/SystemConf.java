package fid.platform.core.common.pojo.robot;

import java.util.Date;

/**
 * @Title: TODO (用一句话描述该文件做什么)
 * @Package fid.platform.core.common.pojo.robot
 * @Description: TODO (用一句话描述该文件做什么)
 * @Author auto generated
 * @Date 2017-11-01 16:13:45
 * @Version V1.0
 * Update Logs:
 */
public class SystemConf {

    /**
     *
     */
    private Integer id;
    /**
     * 所属模块id
     */
    private Integer moduleId;
    /**
     * 配置key
     */
    private String confKey;
    /**
     * 配置说明
     */
    private String confDesc;
    /**
     * 配置值
     */
    private String confValue;
    /**
     * 页面是否可见 0、是，1、否
     */
    private Integer isShow;

    private Date createTime;

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

    public String getConfKey() {
        return confKey;
    }

    public void setConfKey(String confKey) {
        this.confKey = confKey;
    }

    public String getConfDesc() {
        return confDesc;
    }

    public void setConfDesc(String confDesc) {
        this.confDesc = confDesc;
    }

    public String getConfValue() {
        return confValue;
    }

    public void setConfValue(String confValue) {
        this.confValue = confValue;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
