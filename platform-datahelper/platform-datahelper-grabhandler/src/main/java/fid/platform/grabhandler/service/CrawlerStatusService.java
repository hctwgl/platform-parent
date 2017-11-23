package fid.platform.grabhandler.service;

import fid.platform.core.common.pojo.robot.RobotNlpMissionCrawlerStatus;

/**
 * Created by mengtian on 2017/11/15
 */
public interface CrawlerStatusService {

    RobotNlpMissionCrawlerStatus getCrawlerStatus(Long missionId) throws Exception;

    void addCrawlerStatus(RobotNlpMissionCrawlerStatus crawlerStatus) throws Exception;

    void updateCrawlerStatus(RobotNlpMissionCrawlerStatus crawlerStatus) throws Exception;
}
