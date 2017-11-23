package fid.platform.core.common.pojo.robot;


public class RobotNlpMissionCrawlerStatus {

    /**
     *
     */
    private Long id;
    /**
     * 任务id
     */
    private Long missionId;
    /**
     * 爬取状态 0、未开始，1、爬取中，2、爬取完成，3、停止爬取，4、终止爬取任务
     */
    private Integer crawlerStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public Integer getCrawlerStatus() {
        return crawlerStatus;
    }

    public void setCrawlerStatus(Integer crawlerStatus) {
        this.crawlerStatus = crawlerStatus;
    }
}
