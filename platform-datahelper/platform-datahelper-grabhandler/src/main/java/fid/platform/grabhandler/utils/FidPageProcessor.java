package fid.platform.grabhandler.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fid.platform.core.common.pojo.robot.RobotNlpOrgData;
import fid.platform.grabhandler.controller.SpiderMissionManage;
import fid.platform.grabhandler.service.RobotNlpOrgDataService;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * spider
 * 爬虫抓取逻辑
 * @author yangfeng
 *
 */
public class FidPageProcessor implements PageProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(FidPageProcessor.class);
	private Integer pageno = 1;//当前页
	private long time =0;//记录爬取次数
	List<String> listCache = new ArrayList<String>();//记录已添加到队列的详情页url。
	/**
	 * 爬虫任务需要的参数↓
	 */
	private Long orgConfId;
	private String pageUrlTemplet;//分页url的模板
	private String articleUrlRegex;//文章url模板，替换fidPageParam为分页参数，实现翻页
	private String xpathMap;// = new HashMap<String,String>();//xpath
	private Long quantity;
	
	private String tag;//标签
	private String attrKey;//属性
	private String attrVal;//属性值
	
	private SpikeFileCacheQueueScheduler urlCache;
	private CrawlerProcess crawlerProcess;
    private RobotNlpOrgDataService robotNlpOrgDataService;
//	private String listUrlRegex;//文章列表url正则
	/*public void setListUrlRegex(String listUrlRegex) {
		this.listUrlRegex = listUrlRegex;
	}*/
    public void setPageUrlTemplet(String pageUrlTemplet) {
		this.pageUrlTemplet = pageUrlTemplet;
	}
	public void setUrlCache(SpikeFileCacheQueueScheduler urlCache) {
		this.urlCache = urlCache;
	}
	public void setArticleUrlRegex(String articleUrlRegex) {
		this.articleUrlRegex = articleUrlRegex;
	}
	public void setXpathMap(String xpathMap) {
		this.xpathMap = xpathMap;
		try {
			tag = xpathMap.substring(xpathMap.indexOf("//")+2, xpathMap.lastIndexOf("["));
			attrKey = xpathMap.substring(xpathMap.indexOf("@")+1, xpathMap.lastIndexOf("="));
			attrVal = xpathMap.substring(xpathMap.indexOf("\"")+1, xpathMap.lastIndexOf("\""));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("xpath截取出错！(xpath="+xpathMap+")",e);
		}
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public void setOrgConfId(Long orgConfId) {
		this.orgConfId = orgConfId;
	}
	public void setCrawlerProcess(CrawlerProcess crawlerProcess) {
		this.crawlerProcess = crawlerProcess;
	}
	public void setRobotNlpOrgDataService(RobotNlpOrgDataService robotNlpOrgDataService) {
		this.robotNlpOrgDataService = robotNlpOrgDataService;
	}


	private Site site = Site
            .me()
            //设置域名，需设置域名后，addCookie才可生效
            //.setDomain("licaishi.sina.com.cn")
            .setRetryTimes(5)//设置重试次数，
            //.setSleepTime(3000)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
	
	@Override
    public Site getSite() {
        return site;
    }
    @Override
    public void process(Page page) {
    	try {
    		synchronized(this){
	    		if(time >= quantity) {//停掉任务
	    			/*logger.info("停止任务！");*/
	    			crawlerProcess.complete(orgConfId);
	    		}
	    		if(StringUtil.isBlank(articleUrlRegex)){//一级页面，这里暂时用判断文章链接是否为空的方式判断是否为二级页面，可以改成读数据库配置
	    			System.out.println("一级页面");
//	    			oneLevelPageProcess(page);
	    		}else{//二级页面
	    			System.out.println("二级页面");
	    			twoLevelPageProcess(page);
	    		}
    		}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
    	
    }
    /**
     * 下一页
     * @param page
     */
    private void nextPage(Page page){
		
			/*for(int i=1 ;i<6; i++){
				
			}*/
			pageno++;
			page.addTargetRequest(SpiderMissionManage.getPageInfo(pageUrlTemplet,pageno+""));
			System.out.println("下一页:"+SpiderMissionManage.getPageInfo(pageUrlTemplet,pageno+""));
	}
    /**
     * 新的二级页面处理方式：不用分页的正则表达式，只需要一个分页的模板
     * @param page
     */
    public void twoLevelPageProcess(Page page){
    	System.out.println("===================================");
    	System.out.println("抓取队列"+urlCache.getQueue().size());
    	System.out.println("记录缓存队列"+listCache.size());
    	if (page.getUrl().regex(articleUrlRegex).match()) {
    		/*
    		 * 匹配详情页：
    		 * 1、先检测url队列是否不足，不足则翻页
    		 * 2、解析详情页数据
    		 */
    		if(urlCache.getQueue().size()<5){
    			nextPage(page);
    		}
			/*System.out.println("("+time+") 文章链接["+page.getUrl()+"] ::::::");
        	logger.info("("+time+") 文章链接["+page.getUrl()+"] ::::::");*/
        	
        	String pagelink = page.getUrl().toString();
        	String text = page.getHtml().xpath(xpathMap).toString();
        	if(!StringUtil.isBlank(text)){
        		Map<String,Object> resultItemsMap = new HashMap<String, Object>();
        		resultItemsMap.put("链接", pagelink);
        		resultItemsMap.put("正文", text);
        		if(time < quantity){
        			saveDate(resultItemsMap);
        		}
        	}
			
    	}else{
    		/*
    		 * 匹配分页：
    		 * 1、将详情页url加入到队列
    		 * 2、若队列还是不足，则翻页（因为每次从第一页开始，以前拿到过的数据不存入缓存，则会出现队列url不足）
    		 */
			List<String> oldReqs = page.getHtml().links().regex(articleUrlRegex).all();
			List<String> newReqs = new ArrayList<String>();//存没有爬取过的详情页链接
			
			//检查每一页的数据
			//如果本页数据全都拿过，则翻页
			if(urlCache.getUrls().containsAll(oldReqs)){
				nextPage(page);
			}else{
				//过滤拿过的请求
				for(String oldReq : oldReqs){
					if(!urlCache.getUrls().contains(oldReq)){//如果不包含  将url存入新集合
						newReqs.add(oldReq);
					}
				}
				//计算可添加到队列数量   listCache
				//可添加数量    = 总数   - 已缓存数
				long addibleCount = quantity - listCache.size();
				if(addibleCount > 0 ){//添加整个集合
					if(newReqs.size() <= addibleCount){
						listCache.addAll(newReqs);
						page.addTargetRequests(newReqs);
						System.out.println("添加"+newReqs.size()+"条url到抓取队列");
					}else{//截取集合，将队列填充满
						List<String> addibleCountList =  newReqs.subList(0, (int)addibleCount);
						listCache.addAll(addibleCountList);
						page.addTargetRequests(addibleCountList);
						System.out.println("添加"+addibleCount+"条url到抓取队列");
					}
				}
				
			}
    	}
    }
    /**
     * 保存数据库
     * @param resultItemsMap
     */
    public void saveDate(Map<String,Object> resultItemsMap) {
        try {
            RobotNlpOrgData data = new RobotNlpOrgData();
			data.setAccepttime(new Date());
			data.setMissionId(orgConfId);
			data.setSource(resultItemsMap.get("链接").toString());
			data.setTxt(resultItemsMap.get("正文").toString());
			robotNlpOrgDataService.insertRobotNlpOrgData(data);
			time++;
        } catch (Exception e) {
            logger.warn("write file error", e);
        }
    }
    /**
     * 保存文件      url缓存文件和数据保存文件路径写死了(windows路径)
     * @param resultItemsMap
     */
    public void saveDateToFile(Map<String,Object> resultItemsMap) {
        String path = SystemConf.PROCESS_DATA_SAVE_FILE ;//+ PATH_SEPERATOR + resultItems.get("链接") + PATH_SEPERATOR;
        try {
        	//这里的文件名是将url做md5加密
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path +File.separator+ DigestUtils.md5Hex(resultItemsMap.get("链接").toString()) + ".txt"),"UTF-8"));
            for (Map.Entry<String, Object> entry : resultItemsMap.entrySet()) {
            	if(entry.getValue().toString().trim().length() == 0 || entry.getKey().trim().length() == 0) continue;
                if (entry.getValue() instanceof Iterable) {
                    Iterable value = (Iterable) entry.getValue();
                    printWriter.println(entry.getKey() + ":");
                    for (Object o : value) {
                        printWriter.println(o);
                    }
                } else {
                    printWriter.println(entry.getKey() + ":\t" + entry.getValue());
                }
            }
            printWriter.close();
			
        } catch (Exception e) {
            logger.warn("write file error", e);
        }
    }
    //==============================================备份==============
    /**
     * 一级页面处理过程  按照深交所页面逻辑开发，（无法用正则匹配页面url，即，分页是用js函数出发点击事件，而不是直接把链接写在href）
     */
    /*public void oneLevelPageProcess(Page page){
    	if (page.getUrl().regex(listUrlRegex).match()) {
        		System.out.println(" 列表链接["+page.getUrl()+"] ::::::");
//            	logger.info("("+time+") 文章链接["+page.getUrl()+"] ::::::");
        		Elements elements = null;
        		if(attrKey.trim().equals("class")){
        			elements = page.getHtml().getDocument().getElementsByClass(attrVal);
        		}else{
//        			elements = page.getHtml().getDocument().getElementById(attrVal);
        		}
        		if(elements != null){
        			for(Element e : elements){
        				String content = e.text();//For example, given HTML <p>Hello <b>there</b> now! </p>, p.text() returns "Hello there now!"
        				System.out.println(" ("+time+")正文："+content);
        				time++;
        			}
        		}
        		
            	String pagelink = page.getUrl().toString();
            	String text = page.getHtml().xpath(xpathMap).toString();
            	if(!StringUtil.isBlank(text)){
            		Map<String,Object> resultItemsMap = new HashMap<String, Object>();
            		resultItemsMap.put("链接", pagelink);
            		resultItemsMap.put("正文", text);
            		if(time < quantity){
            			saveDate(resultItemsMap);
            		}
            		
            	}
            	page.setSkip(true);
            //将列表页加入到待抓取队列中
            page.addTargetRequests(page.getHtml().links().regex(listUrlRegex).all());
        }
    }*/
    
    /**
     * 二级页面处理过程 
     */
 /*   public void twoLevelPageProcess(Page page){
        if (page.getUrl().regex(listUrlRegex).match()) {//抓取到列表页       http://licaishi\.sina\.com\.cn/web/index\?ind_id=\d+&fee=all&page=\d+
        	page.setSkip(true);
        	System.out.println(":::::: 列表链接["+page.getUrl()+"] ::::::");
        	//将待抓取文章url加入到抓取队列中                                                 ↓.xpath("//div[@id=\"pageList\"]")
            page.addTargetRequests(page.getHtml().links().regex(articleUrlRegex).all());
            //将列表页加入到待抓取队列中
            page.addTargetRequests(page.getHtml().links().regex(listUrlRegex).all());
        } else {//抓取到文章页     http://licaishi\.sina\.com\.cn/view/\d+\?ind_id=1
        	System.out.println("("+time+") 文章链接["+page.getUrl()+"] ::::::");
        	logger.info("("+time+") 文章链接["+page.getUrl()+"] ::::::");
        	page.putField("链接", page.getUrl());
        	page.putField("正文", page.getHtml().xpath(xpathMap));
        	if (page.getResultItems().get("正文") == null ) {
    		page.setSkip(true);
        	}
        	String pagelink = page.getUrl().toString();
        	String text = page.getHtml().xpath(xpathMap).toString();
        	if(!StringUtil.isBlank(text)){
        		Map<String,Object> resultItemsMap = new HashMap<String, Object>();
        		resultItemsMap.put("链接", pagelink);
        		resultItemsMap.put("正文", text);
        		if(time < quantity){
        			saveDate(resultItemsMap);
        		}
        	}
            	
        }
    }*/
    
    
    
}
