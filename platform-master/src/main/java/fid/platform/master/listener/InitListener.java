package fid.platform.master.listener;

import fid.platform.core.common.constant.Constant;
import fid.platform.core.common.pojo.robot.RobotNlpMissionCrawlerStatus;
import fid.platform.core.common.pojo.robot.RobotNlpMissionOrgConf;
import fid.platform.database.robot.mapper.RobotNlpMissionCrawlerStatusMapper;
import fid.platform.database.robot.mapper.RobotNlpMissionOrgConfMapper;
import fid.platform.grabhandler.component.MasterCrawlerProcess;
import fid.platform.grabhandler.controller.SpiderMissionManage;
import fid.platform.grabhandler.manager.CrawlerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import us.codecraft.webmagic.Spider;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

/**
 * Created by mengtian on 2017/11/14
 */
public class InitListener implements ServletContextListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ApplicationContext context = WebApplicationContextUtils
                .getRequiredWebApplicationContext(servletContextEvent.getServletContext());
        //重启所有未完成的爬虫
        MasterCrawlerProcess crawlerProcess = context.getBean(MasterCrawlerProcess.class);
        RobotNlpMissionCrawlerStatusMapper crawlerStatusMapper =
                context.getBean(RobotNlpMissionCrawlerStatusMapper.class);
        SpiderMissionManage spiderMissionManage = context.getBean(SpiderMissionManage.class);

        RobotNlpMissionOrgConfMapper robotNlpMissionOrgConfMapper = context.getBean(RobotNlpMissionOrgConfMapper.class);

        RobotNlpMissionCrawlerStatus crawlerStatus = new RobotNlpMissionCrawlerStatus();
        crawlerStatus.setCrawlerStatus(Constant.CRAWLER_RUNNING);

        try {
            List<RobotNlpMissionCrawlerStatus> crawlerStatuses =
                    crawlerStatusMapper.queryRobotNlpMissionCrawlerStatus(crawlerStatus);
            for (RobotNlpMissionCrawlerStatus missionCrawlerStatus : crawlerStatuses) {
                List<RobotNlpMissionOrgConf> missionOrgConfs = robotNlpMissionOrgConfMapper
                        .queryMissionOrgConfs(missionCrawlerStatus.getMissionId());

                for (RobotNlpMissionOrgConf missionOrgConf : missionOrgConfs) {
                    //调用爬虫启动接口,传递crawlerProcess
                    Spider spider = spiderMissionManage.startMision(missionOrgConf.getId(), crawlerProcess);
                    //将启动成功的爬虫放入manager管理
                    CrawlerManager.addSpider(missionCrawlerStatus.getMissionId(), missionOrgConf.getId(), spider);
                }
            }
        } catch (Exception e) {
            logger.error("初始化项目爬虫出错", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
//        ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext());
    }
}
