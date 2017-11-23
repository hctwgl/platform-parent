package fid.platform.core.common.pojo.robot.vo;

import java.util.List;

/**
 * Created by mengtian on 2017/11/17
 */
public class RobotCrawlerDetail {

    private Long id;
    private Long missionId;
    private Integer crawlerStatus;

    private List<RobotCrawlerPage> crawlerPages;

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

    public List<RobotCrawlerPage> getCrawlerPages() {
        return crawlerPages;
    }

    public void setCrawlerPages(List<RobotCrawlerPage> crawlerPages) {
        this.crawlerPages = crawlerPages;
    }
}
