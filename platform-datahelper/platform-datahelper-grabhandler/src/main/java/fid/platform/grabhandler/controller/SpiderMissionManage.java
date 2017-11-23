package fid.platform.grabhandler.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.sun.tools.javac.main.Main;

import fid.platform.core.common.pojo.robot.RobotNlpMissionOrgConf;
import fid.platform.grabhandler.service.RobotNlpMissionOrgConfService;
import fid.platform.grabhandler.service.RobotNlpOrgDataService;
import fid.platform.grabhandler.utils.ContextUtil;
import fid.platform.grabhandler.utils.CrawlerProcess;
import fid.platform.grabhandler.utils.FidPageProcessor;
import fid.platform.grabhandler.utils.FidPipeline;
import fid.platform.grabhandler.utils.SpikeFileCacheQueueScheduler;
import fid.platform.grabhandler.utils.SystemConf;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.RedisScheduler;

/**
 * 爬虫任务管理
 * @author yangfeng
 *
 */
@Service("spiderMissionManage")
public class SpiderMissionManage {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private RobotNlpMissionOrgConfService robotNlpMissionOrgConfService;
	@Resource
    private RobotNlpOrgDataService robotNlpOrgDataService;
	/**
	 * 开始任务
	 * @param missionId
	 */
	public Spider startMision(Long orgConfId,CrawlerProcess crawlerProcess){
		/**
		 * 执行逻辑：
		 * 1、根据任务id拿到任务配置参数；
		 * 2、配置pageprocessor
		 * 3、启动任务
		 */
		try {
//			RobotNlpMissionOrgConf conf = robotNlpMissionOrgConfService.getRobotNlpMissionOrgConfByMissionId(missionId);
			RobotNlpMissionOrgConf conf = robotNlpMissionOrgConfService.getRobotNlpMissionOrgConfById(orgConfId);
			/**
			 * 只需要一个分页的模板
			 */
	    	FidPageProcessor fidPageProcessornew = new FidPageProcessor();
	    	fidPageProcessornew.setOrgConfId(conf.getMissionId());
//	    	fidPageProcessornew.setListUrlRegex(conf.getStartPage());
	    	fidPageProcessornew.setPageUrlTemplet(conf.getStartPage());
	    	fidPageProcessornew.setArticleUrlRegex(conf.getArticlePageUrlRegex());
	    	fidPageProcessornew.setXpathMap(conf.getXpath());
	    	fidPageProcessornew.setQuantity(conf.getQuantity());
	    	/**
	    	 * 注入两个操作类
	    	 */
	    	fidPageProcessornew.setCrawlerProcess(crawlerProcess);
	    	fidPageProcessornew.setRobotNlpOrgDataService(robotNlpOrgDataService);
	    	
	    	SpikeFileCacheQueueScheduler urlCache = new SpikeFileCacheQueueScheduler(SystemConf.PROCESS_URL_CACHE_FILE);
	    	urlCache.setRegx(conf.getArticlePageUrlRegex());
	    	fidPageProcessornew.setUrlCache(urlCache);
	    	
	    	
		    Spider mission = Spider.create(fidPageProcessornew) 
		    		//起始页地址
		    		.addUrl(getPageInfo(conf.getStartPage(),"1"))
		    		.setScheduler(urlCache)
		    		//.addPipeline(new FidPipeline("F:\\webmagic"))
		    		.thread(1);
		    mission.start();
		    crawlerProcess.startOn(orgConfId);
		    return mission;        
		   
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("配置爬虫任务出错！！");
			e.printStackTrace();
			return null;
		}
	}
	public static String getPageInfo(String str, String pageno){
		//字符串为空，或者不包含fidPageParam标示符则返回null
		if(str == null || str.trim().length() == 0 || str.indexOf("fidPageParam") == -1) return null;
		return str.replaceAll("fidPageParam", pageno);
	}
	public static void main(String[] args){
		
		/*   post方式
		 PageProcessor pageProcessor = new sd_notice();
		Spider spider = Spider.create(pageProcessor);
		for (int i = 1; i < 10; i++) {
		    String url = null;
		    Map<String, Object> nameValuePair = new HashMap<String, Object>();
		    NameValuePair[] values = new NameValuePair[3];
		    values[0] = new BasicNameValuePair("pagesize", "10");
		    values[1] = new BasicNameValuePair("cbfy", "");
		    values[2] = new BasicNameValuePair("pageno", String.valueOf(i));
		    nameValuePair.put("nameValuePair", values);
		    url = "http://www.zjsfgkw.cn/Notice/NoticeSD?cbfy=&pageno="+String.valueOf(i)+"&pagesize=10";
		    Request request = new Request(url);
		    request.setExtras(nameValuePair);
		    request.setMethod(HttpConstant.Method.POST);
		    spider.addRequest(request);
		    }
		spider.thread(3).run();*/
		
		
		/*System.out.println(getPageInfo("http://news.21fid.com/realthing_fidPageParam_10","1"));
		System.out.println(getPageInfo("http://news.21fid.com/realthing_fidPageram_10","2"));
		System.out.println(getPageInfo("http://news.21fid.com/realthing_fidPageParam_10","3"));
		System.out.println(getPageInfo("http://news.21fid.com/realthing_fidPageParam_10","4"));
		
		System.out.println(getPageInfo("http://news.21fid.com/realthing_fidPageParam_10","8"));*/
	}
	
	
	
}
