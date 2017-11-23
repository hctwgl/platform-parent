package fid.platform.master.service.impl;

import fid.platform.core.common.constant.Constant;
import fid.platform.core.common.pojo.robot.RobotNlpMissionCrawlerStatus;
import fid.platform.core.common.pojo.robot.RobotNlpMissionOrgConf;
import fid.platform.database.robot.mapper.RobotNlpMissionCrawlerStatusMapper;
import fid.platform.database.robot.mapper.RobotNlpMissionOrgConfMapper;
import fid.platform.grabhandler.component.MasterCrawlerProcess;
import fid.platform.grabhandler.controller.SpiderMissionManage;
import fid.platform.grabhandler.manager.CrawlerManager;
import fid.platform.grabhandler.service.CrawlerStatusService;
import fid.platform.master.service.CrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by mengtian on 2017/11/16
 */
@Service
public class CrawlerServiceImpl implements CrawlerService {


    private final Logger logger = LoggerFactory.getLogger(CrawlerServiceImpl.class);

    @Resource
    private RobotNlpMissionOrgConfMapper robotNlpMissionOrgConfMapper;

    @Resource
    private RobotNlpMissionCrawlerStatusMapper robotNlpMissionCrawlerStatusMapper;

    @Resource
    private SpiderMissionManage spiderMissionManage;

    @Resource
    private MasterCrawlerProcess masterCrawlerProcess;

    @Resource
    private CrawlerStatusService crawlerStatusService;


    @Override
    public void addCrawler(RobotNlpMissionOrgConf orgConf) throws Exception {
        //爬虫
        orgConf.setStatus(Constant.CRAWLER_NOTSTART);
        robotNlpMissionOrgConfMapper.insert(orgConf);
        //添加爬虫对应状态
        RobotNlpMissionCrawlerStatus crawlerStatus = crawlerStatusService.getCrawlerStatus(orgConf.getMissionId());
        if (crawlerStatus == null) {
            crawlerStatus = new RobotNlpMissionCrawlerStatus();
            crawlerStatus.setCrawlerStatus(Constant.CRAWLER_NOTSTART);
            robotNlpMissionCrawlerStatusMapper.insertRobotNlpMissionCrawlerStatus(crawlerStatus);
        } else {
            crawlerStatus.setCrawlerStatus(Constant.CRAWLER_NOTSTART);
            robotNlpMissionCrawlerStatusMapper.updateRobotNlpMissionCrawlerStatus(crawlerStatus);
        }
    }

    @Override
    public void startCrawler(Long missionId, Long orgConfId) throws Exception {
        Spider spider = spiderMissionManage.startMision(orgConfId, masterCrawlerProcess);
        CrawlerManager.addSpider(missionId, orgConfId, spider);

        //更新任务爬虫总状态为爬取中
        RobotNlpMissionCrawlerStatus crawlerStatus = crawlerStatusService.getCrawlerStatus(missionId);
        crawlerStatus.setCrawlerStatus(Constant.CRAWLER_RUNNING);
        robotNlpMissionCrawlerStatusMapper.updateRobotNlpMissionCrawlerStatus(crawlerStatus);
    }

    @Override
    public void stopCrawler(Long missionId, Long orgConfId) throws Exception {
        CrawlerManager.delSpider(missionId, orgConfId);
        RobotNlpMissionOrgConf robotNlpMissionOrgConf = robotNlpMissionOrgConfMapper.selectByPrimaryKey(orgConfId);
        robotNlpMissionOrgConf.setStatus(Constant.CRAWLER_STOP);
        robotNlpMissionOrgConfMapper.updateByPrimaryKeySelective(robotNlpMissionOrgConf);

        //当所有爬取URL任务都为停止状态时，修改任务爬取总状态为停止状态
        List<RobotNlpMissionOrgConf> missionOrgConfs = robotNlpMissionOrgConfMapper.queryMissionOrgConfs(missionId);

        if (checkMissionOrgConfStatus(missionOrgConfs, Constant.CRAWLER_STOP)) {
            RobotNlpMissionCrawlerStatus crawlerStatus = crawlerStatusService.getCrawlerStatus(missionId);
            crawlerStatus.setCrawlerStatus(Constant.CRAWLER_STOP);
            robotNlpMissionCrawlerStatusMapper.updateRobotNlpMissionCrawlerStatus(crawlerStatus);
        }
    }

    @Override
    public void shutdownCrawler(Long missionId, Long orgConfId) throws Exception {
        CrawlerManager.delSpider(missionId, orgConfId);

        RobotNlpMissionOrgConf robotNlpMissionOrgConf = robotNlpMissionOrgConfMapper.selectByPrimaryKey(orgConfId);
        robotNlpMissionOrgConf.setStatus(Constant.CRAWLER_SHUTDOWN);
        robotNlpMissionOrgConfMapper.updateByPrimaryKeySelective(robotNlpMissionOrgConf);
        //当所有爬取URL任务都为终止状态时，修改任务爬取总状态为终止状态
        List<RobotNlpMissionOrgConf> missionOrgConfs = robotNlpMissionOrgConfMapper.queryMissionOrgConfs(missionId);

        if (checkMissionOrgConfStatus(missionOrgConfs, Constant.CRAWLER_SHUTDOWN)) {
            RobotNlpMissionCrawlerStatus crawlerStatus = crawlerStatusService.getCrawlerStatus(missionId);
            crawlerStatus.setCrawlerStatus(Constant.CRAWLER_SHUTDOWN);
            robotNlpMissionCrawlerStatusMapper.updateRobotNlpMissionCrawlerStatus(crawlerStatus);
        }
    }

    private boolean checkMissionOrgConfStatus(List<RobotNlpMissionOrgConf> missionOrgConfs, Integer status) {
        boolean isInstatus = true;
        for (RobotNlpMissionOrgConf missionOrgConf : missionOrgConfs) {
            if (missionOrgConf.getStatus() != status) {
                isInstatus = false;
                break;
            }
        }
        return isInstatus;
    }
}
