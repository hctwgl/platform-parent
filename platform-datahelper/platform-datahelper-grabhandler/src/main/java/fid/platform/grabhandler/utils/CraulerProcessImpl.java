package fid.platform.grabhandler.utils;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import fid.platform.grabhandler.controller.SpiderMissionManage;
import us.codecraft.webmagic.Spider;

@Service
public class CraulerProcessImpl implements CrawlerProcess{
	Spider spider;
	@Resource
	private SpiderMissionManage manage;
	
	@Override
	public void startOn(Long missionId) {
		// TODO Auto-generated method stub
		
		spider = manage.startMision(missionId,this);
		System.out.println("spider start  "+spider);
//		spider.start();
	}

	@Override
	public void complete(Long missionId) {
		// TODO Auto-generated method stub
		System.out.println("spider stop  "+spider);
		spider.stop();
	}

}
