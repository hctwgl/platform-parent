package fid.platform.grabhandler.qtz;

import java.util.HashMap;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import fid.platform.grabhandler.utils.FidPageProcessor;
import fid.platform.grabhandler.utils.FidPipeline;
import us.codecraft.webmagic.Spider;

/**
 * 爬虫的控制
 * @author yangfeng
 *
 */
public class SpiderTask{

	private static Logger logger = LoggerFactory.getLogger(SpiderTask.class);
	private static boolean isRuning = false;
	
	//匹配列表页url的正则表达式（匹配每一页的数据）
	public static final String URL_LIST = "http://licaishi\\.sina\\.com\\.cn/web/index\\?ind_id=\\d+&fee=all&page=\\d+";
	//匹配文章页面url的正则表达式（匹配每一页中每篇文章）
	public static final String URL_POST = "http://licaishi\\.sina\\.com\\.cn/view/\\d+\\?ind_id=1";
	private HashMap<String, String> xpathmap;//
	
	
	private HashMap<String, String> buidXpath(HashMap<String, String> xpathmap){
		xpathmap = new HashMap<String, String>();
		xpathmap.put("正文", "//div[@class=\"s_left\"]//div[@class=\"p_article\"]/outerHtml()/tidyText()");
    	//xpathmap.put("标题", "//div[@class=\"s_left\"]/h1/text()");
    	//xpathmap.put("日期", "//div[@class=\"s_left\"]//div[@class=\"p_info\"]/time/text()");
    	//xpathmap.put("作者", "//div[@class=\"s_right\"]/div//div[@class=\"planner\"]//div[@class=\"names\"]/a/p/text()");
		return xpathmap;
	}
	
	public void doSpiderTask(){
		/*try {
			if(!isRuning){
				isRuning = true;
				logger.info("开始执行任务 ... ");
		    	//初始化spring的上下文环境引用
//				ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
				
				//      //*[@id="article_content"]    /html/body/div[5]/main/article/div[1]
		    	
		    	FidPageProcessor fidPageProcessornew = new FidPageProcessor();
		    	fidPageProcessornew.setListUrlRegex(URL_LIST);
		    	fidPageProcessornew.setArticleUrlRegex(URL_POST);
		    	fidPageProcessornew.setXpathMap(buidXpath(xpathmap));
			    Spider.create(fidPageProcessornew)
			    		//起始页地址
			    		.addUrl("http://licaishi.sina.com.cn/web/index?ind_id=1&fee=all&page=1")
			    		.addPipeline(new FidPipeline("F:\\webmagic"))
			    		.thread(1)
			            .run();
			   
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("执行任务出错！！");
			isRuning = false;
		}
		*/
		
	}
	
	
}