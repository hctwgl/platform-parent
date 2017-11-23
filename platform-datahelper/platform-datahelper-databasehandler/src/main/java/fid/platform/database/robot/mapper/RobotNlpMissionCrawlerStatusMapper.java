package fid.platform.database.robot.mapper;

import fid.platform.core.common.pojo.robot.RobotNlpMission;
import fid.platform.core.common.pojo.robot.RobotNlpMissionCrawlerStatus;

import java.util.List;

/**
 * @Package fid.platform.database.robot.mapper
 * @Author auto generated
 * @Date 2017-11-15 11:46:42
 */
public interface RobotNlpMissionCrawlerStatusMapper {
    void insertRobotNlpMissionCrawlerStatus(RobotNlpMissionCrawlerStatus robotNlpMissionCrawlerStatus) throws Exception;

    void updateRobotNlpMissionCrawlerStatus(RobotNlpMissionCrawlerStatus robotNlpMissionCrawlerStatus) throws Exception;

    void deleteRobotNlpMissionCrawlerStatus(Long id) throws Exception;

    List<RobotNlpMissionCrawlerStatus> queryRobotNlpMissionCrawlerStatus(RobotNlpMissionCrawlerStatus robotNlpMissionCrawlerStatus) throws Exception;

    Integer queryRobotNlpMissionCrawlerStatusCount(RobotNlpMissionCrawlerStatus robotNlpMissionCrawlerStatus) throws Exception;

    RobotNlpMissionCrawlerStatus findRobotNlpMissionCrawlerStatus(Long id) throws Exception;
}