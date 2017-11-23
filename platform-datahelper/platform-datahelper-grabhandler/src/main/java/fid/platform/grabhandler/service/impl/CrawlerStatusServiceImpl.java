package fid.platform.grabhandler.service.impl;

import fid.platform.core.common.pojo.robot.RobotNlpMissionCrawlerStatus;
import fid.platform.database.robot.mapper.RobotNlpMissionCrawlerStatusMapper;
import fid.platform.grabhandler.service.CrawlerStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by mengtian on 2017/11/15
 */
@Service
public class CrawlerStatusServiceImpl implements CrawlerStatusService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private RobotNlpMissionCrawlerStatusMapper robotNlpMissionCrawlerStatusMapper;

    @Override
    public RobotNlpMissionCrawlerStatus getCrawlerStatus(Long missionId) throws Exception {
        try {
            RobotNlpMissionCrawlerStatus crawlerStatus = new RobotNlpMissionCrawlerStatus();
            crawlerStatus.setMissionId(missionId);
            List<RobotNlpMissionCrawlerStatus> crawlerStatuses =
                    robotNlpMissionCrawlerStatusMapper.queryRobotNlpMissionCrawlerStatus(crawlerStatus);
            return crawlerStatuses == null ? null : crawlerStatuses.get(0);
        } catch (Exception e) {
            logger.error("查询出错", e);
            throw new Exception("查询出错", e);
        }
    }

    @Override
    public void addCrawlerStatus(RobotNlpMissionCrawlerStatus crawlerStatus) throws Exception {
        try {
            robotNlpMissionCrawlerStatusMapper.insertRobotNlpMissionCrawlerStatus(crawlerStatus);
        } catch (Exception e) {
            logger.error("新增爬虫状态出错", e);
            throw new Exception("新增爬虫状态出错", e);
        }
    }

    @Override
    public void updateCrawlerStatus(RobotNlpMissionCrawlerStatus crawlerStatus) throws Exception {
        try {
            robotNlpMissionCrawlerStatusMapper.updateRobotNlpMissionCrawlerStatus(crawlerStatus);
        } catch (Exception e) {
            logger.error("更新爬取状态出错", e);
            throw new Exception("更新爬取状态出错", e);
        }
    }
}
