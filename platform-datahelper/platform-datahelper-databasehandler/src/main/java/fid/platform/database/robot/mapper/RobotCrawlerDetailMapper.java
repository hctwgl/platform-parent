package fid.platform.database.robot.mapper;

import fid.platform.core.common.pojo.robot.vo.RobotCrawlerDetail;

/**
 * Created by mengtian on 2017/11/17
 */
public interface RobotCrawlerDetailMapper {
    RobotCrawlerDetail queryCrawlerDetail(Long missionId) throws Exception;
}
