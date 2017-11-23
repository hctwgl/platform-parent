package fid.platform.master.controller;

import fid.platform.core.common.pojo.commons.MsgMap;
import fid.platform.core.common.pojo.robot.RobotNlpMissionOrgConf;
import fid.platform.grabhandler.utils.ResolveXpath;
import fid.platform.master.service.CrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by mengtian on 2017/11/15
 */
@Controller
@RequestMapping("/crawler")
public class CrawlerController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private CrawlerService crawlerService;


    /**
     * @api {post} /crawler/addCrawler 2、添加爬取任务
     * @apiName addCrawler
     * @apiGroup mission-crawler
     * @apiParam {number{0-10位}} missionId 任务id
     * @apiParam {string{0-500}} startPage 爬虫起始页
     * @apiParam {string{0-500}} listPageUrlRegex 爬虫列表页正则
     * @apiParam {string{0-500}} articlePageUrlRegex 爬虫文章页正则
     * @apiParam {string{0-500}} xpath 文章所在元素xpath
     * @apiParam {number{0-20位}} quantity 爬取数量
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     */
    @RequestMapping(value = "/addCrawler", method = RequestMethod.POST)
    @ResponseBody
    public MsgMap addCrawler(@ModelAttribute("orgConf") RobotNlpMissionOrgConf orgConf) {
        MsgMap msgMap = new MsgMap();
        try {
            crawlerService.addCrawler(orgConf);
            msgMap.doSuccess();
        } catch (Exception e) {
            logger.error("新增爬取任务失败", e);
            msgMap.doFail();
        }
        return msgMap;
    }

    /**
     * @api {post} /crawler/start 3、启动爬虫
     * @apiName start
     * @apiGroup mission-crawler
     * @apiParam {Number{0-10位}} missionId 任务id
     * @apiParam {Number{0-20位}} orgConfId 爬虫id
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "operateSuccess": true
     * }
     */
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseBody
    public MsgMap start(@RequestParam("missionId") Long missionId,
                        @RequestParam("orgConfId") Long orgConfId) {
        MsgMap msgMap = new MsgMap();
        try {
            crawlerService.startCrawler(missionId, orgConfId);
            msgMap.doSuccess();
        } catch (Exception e) {
            logger.error("启动爬虫任务出错,任务ID:{}", missionId, e);
            msgMap.doFail();
        }
        return msgMap;
    }


    /**
     * @api {post} /crawler/stop 4、停止爬虫
     * @apiName stop
     * @apiGroup mission-crawler
     * @apiParam {Number{0-10位}} missionId 任务id
     * @apiParam {Number{0-20位}} orgConfId 爬虫id
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "operateSuccess": true
     * }
     */
    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    @ResponseBody
    public MsgMap stop(@RequestParam("missionId") Long missionId,
                       @RequestParam("orgConfId") Long orgConfId) {
        MsgMap map = new MsgMap();
        try {
            crawlerService.stopCrawler(missionId, orgConfId);
            map.doSuccess();
        } catch (Exception e) {
            logger.error("停止爬虫出错,任务ID:{}", missionId, e);
            map.doFail();
        }
        return map;
    }

    /**
     * @api {post} /crawler/shutdown 5、终止爬取任务
     * @apiName shutdown
     * @apiGroup mission-crawler
     * @apiParam {Number{0-10位}} missionId 任务id
     * @apiParam {Number{0-20位}} orgConfId 爬虫id
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "operateSuccess": true
     * }
     */
    @RequestMapping(value = "/shutdown", method = RequestMethod.POST)
    @ResponseBody
    public MsgMap shutdown(@RequestParam("missionId") Long missionId,
                           @RequestParam("orgConfId") Long orgConfId) {
        MsgMap msgMap = new MsgMap();
        try {
            crawlerService.shutdownCrawler(missionId, orgConfId);
            msgMap.doSuccess();
        } catch (Exception e) {
            logger.error("终止爬取任务出错,任务ID:{}", missionId, e);
            msgMap.doFail();
        }
        return msgMap;
    }

    /**
     * @api {POST} /crawler/getXpath 1、获取待爬取网站的xpath
     * @apiName getXpath
     * @apiGroup mission-crawler
     * @apiParam {string{0-500}} url 爬取文章的详情页
     * @apiParam {string{0-500}} text 爬取文章某一部分
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "data": "//div[@class=\"post-body\"]",
     * "operateSuccess": true
     * }
     */
    @RequestMapping(value = "/getXpath", method = RequestMethod.POST)
    @ResponseBody
    public MsgMap getXpath(@RequestParam("url") String url,
                           @RequestParam("text") String text) {

        MsgMap msgMap = new MsgMap();
        try {
            String xpath = ResolveXpath.getXpathByText(url, text);
            if (xpath.equals("没有匹配到！")) {
                msgMap.doFail("没有匹配到！");
            } else {
                msgMap.doSuccess(xpath);
            }
        } catch (IOException e) {
            logger.error("获取网页:{}对应正文:{} 的xpath出错", url, text, e);
        }
        return msgMap;
    }
}
