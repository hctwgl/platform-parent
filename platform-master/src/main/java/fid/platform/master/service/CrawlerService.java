package fid.platform.master.service;

import fid.platform.core.common.pojo.robot.RobotNlpMissionOrgConf;

/**
 * Created by mengtian on 2017/11/16
 */
public interface CrawlerService {

    /**
     * 添加任务对应爬取任务
     *
     * @param orgConf
     * @throws Exception
     */
    void addCrawler(RobotNlpMissionOrgConf orgConf) throws Exception;

    void startCrawler(Long missionId, Long orgConfId) throws Exception;

    void stopCrawler(Long missionId, Long orgConfId) throws Exception;

    void shutdownCrawler(Long missionId, Long orgConfId) throws Exception;
}
