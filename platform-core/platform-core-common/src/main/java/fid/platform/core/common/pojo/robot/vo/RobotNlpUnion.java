package fid.platform.core.common.pojo.robot.vo;

import fid.platform.core.common.pojo.robot.RobotNlpMissionOrgConf;

import java.util.Date;
import java.util.List;

/**
 * Created by mengtian on 2017/11/15
 * <p>
 * 任务各项数据集合
 * 针对页面展示
 */
public class RobotNlpUnion {
    //任务
    private Long id;
    private String name;
    private String description;
    private Integer status;
    private Date createTime;
    //爬虫配置
//    private List<RobotNlpMissionOrgConf> missionOrgConfs;

    //爬取状态
    private Integer crawlerStatus;

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
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCrawlerStatus() {
        return crawlerStatus;
    }

    public void setCrawlerStatus(Integer crawlerStatus) {
        this.crawlerStatus = crawlerStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
