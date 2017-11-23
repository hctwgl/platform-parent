package fid.platform.grabhandler.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mysql.fabric.xmlrpc.base.Array;

import fid.platform.core.common.pojo.robot.RobotNlpMissionOrgConf;
import fid.platform.grabhandler.component.MasterCrawlerProcess;
import fid.platform.grabhandler.controller.SpiderMissionManage;
import fid.platform.grabhandler.manager.CrawlerManager;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.RedisScheduler;

public class Test {

	public static void main(String[] args){
		ApplicationContext context = new ClassPathXmlApplicationContext("conf/root-context.xml");
		SpiderMissionManage spiderMissionManager = context.getBean("spiderMissionManage",SpiderMissionManage.class);
		MasterCrawlerProcess masterCrawlerProcess = context.getBean("masterCrawlerProcess",MasterCrawlerProcess.class);
		
		Spider spider = spiderMissionManager.startMision(new Long(10), masterCrawlerProcess);
        CrawlerManager.addSpider(new Long(19), new Long(10), spider);
		
	}
	
}
