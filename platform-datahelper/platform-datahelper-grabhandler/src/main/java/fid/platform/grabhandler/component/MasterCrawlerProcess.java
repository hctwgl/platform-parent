package fid.platform.grabhandler.component;

import fid.platform.core.common.constant.Constant;
import fid.platform.core.common.pojo.robot.RobotNlpMissionCrawlerStatus;
import fid.platform.core.common.pojo.robot.RobotNlpMissionOrgConf;
import fid.platform.database.robot.mapper.RobotNlpMissionOrgConfMapper;
import fid.platform.grabhandler.service.CrawlerStatusService;
import fid.platform.grabhandler.utils.CrawlerProcess;
import fid.platform.grabhandler.manager.CrawlerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by mengtian on 2017/11/15
 */
@Component
public class MasterCrawlerProcess implements CrawlerProcess {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private CrawlerStatusService crawlerStatusService;

    @Resource
    private RobotNlpMissionOrgConfMapper robotNlpMissionOrgConfMapper;

    @Override
    public void startOn(Long orgConfId) {
        try {

            RobotNlpMissionOrgConf orgConf = robotNlpMissionOrgConfMapper.selectByPrimaryKey(orgConfId);
            orgConf.setStatus(Constant.CRAWLER_RUNNING);
            robotNlpMissionOrgConfMapper.updateByPrimaryKeySelective(orgConf);

            RobotNlpMissionCrawlerStatus crawlerStatus = crawlerStatusService.getCrawlerStatus(orgConf.getMissionId());
            if (crawlerStatus != null) {
                crawlerStatus.setCrawlerStatus(Constant.CRAWLER_RUNNING);
                crawlerStatusService.updateCrawlerStatus(crawlerStatus);
            } else {
                crawlerStatus = new RobotNlpMissionCrawlerStatus();
                crawlerStatus.setMissionId(orgConf.getMissionId());
                crawlerStatus.setCrawlerStatus(Constant.CRAWLER_RUNNING);
                crawlerStatusService.addCrawlerStatus(crawlerStatus);
            }
        } catch (Exception e) {
            logger.error("新增爬虫状态出错", e);
        }
    }

    @Override
    public void complete(Long orgConfId) {
        try {

            RobotNlpMissionOrgConf orgConf = robotNlpMissionOrgConfMapper.selectByPrimaryKey(orgConfId);
            orgConf.setStatus(Constant.CRAWLER_COMPLETE);
            robotNlpMissionOrgConfMapper.updateByPrimaryKeySelective(orgConf);

            //如果有爬取任务还在爬取爬取状态下，则任务任务还未完成，其他状态（停止、终止）都认为时任务已完成
            List<RobotNlpMissionOrgConf> missionOrgConfs = robotNlpMissionOrgConfMapper.queryMissionOrgConfs(orgConf.getMissionId());
            boolean complete = true;
            for (RobotNlpMissionOrgConf missionOrgConf : missionOrgConfs) {
                if (missionOrgConf.getStatus() == Constant.CRAWLER_RUNNING) {
                    complete = false;
                    break;
                }
            }
            if (complete) {
                RobotNlpMissionCrawlerStatus crawlerStatus = crawlerStatusService.getCrawlerStatus(orgConf.getMissionId());
                crawlerStatus.setCrawlerStatus(Constant.CRAWLER_COMPLETE);
                crawlerStatusService.updateCrawlerStatus(crawlerStatus);
                //将manager中的缓存删除
                CrawlerManager.delSpider(orgConf.getMissionId());
            } else {
                CrawlerManager.delSpider(orgConf.getMissionId(), orgConfId);
            }
        } catch (Exception e) {
            logger.error("爬取完毕更新状态出错", e);
        }
    }
}
