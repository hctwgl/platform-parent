package fid.platform.master.controller;

import fid.platform.core.common.commonutil.YamlConfigUtil;
import fid.platform.core.common.pojo.commons.MsgMap;
import fid.platform.core.common.pojo.robot.RobotNlpConfigNet;
import fid.platform.core.common.pojo.robot.RobotNlpConfigWord2vec;
import fid.platform.core.common.pojo.robot.RobotNlpMission;
import fid.platform.core.common.pojo.robot.vo.RobotCrawlerDetail;
import fid.platform.core.common.pojo.robot.vo.RobotNlpUnion;
import fid.platform.core.common.pojo.robot.vo.RobotTrainDataDetail;
import fid.platform.core.common.pojo.robot.vo.RobotWord2Vec;
import fid.platform.master.service.MissionService;
import fid.platform.master.util.FileDownloadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by mengtian on 2017/11/3
 * 任务控制
 */
@Controller
@RequestMapping("/mission")
public class MissionController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private MissionService missionService;

    /**
     * @api {get} /mission/list 1、获取任务列表
     * @apiName list
     * @apiGroup mission
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccess name 任务名称
     * @apiSuccess description 任务描述
     * @apiSuccess missionOrgConfs
     * 任务爬虫配置（startPage：起始页，listPageUrlRegex：列表页正则，articlePageUrlRegex：文章页正则，xpath：文章所在页面元素，quantity：爬取数量）
     * @apiSuccess status 1、资讯分类，2、数据准备，3、数据处理，4、词典训练，5、神经网络训练
     * @apiSuccess crawlerStatus null/1、爬取中，2、爬取完成，3、中止爬取
     * @apiSuccessExample {json} response:
     * {
     * "id": 1,
     * "name": "testmission",
     * "description": "测试",
     * "status": 0,
     * "crawlerStatus": null
     * }
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public MsgMap missions(@ModelAttribute("nlpUnion") RobotNlpUnion nlpUnion) {
        MsgMap msgMap = new MsgMap();
        try {
            List<RobotNlpUnion> nlpUnionList = missionService.getRobotNlpUnion(nlpUnion);
            msgMap.doSuccess(nlpUnionList);
        } catch (Exception e) {
            logger.error("查询任务列表失败", e);
            msgMap.doFail();
        }
        return msgMap;
    }


    /**
     * @api {get} /mission/missionDetail 2、任务详情
     * @apiName missionDetail
     * @apiGroup mission
     * @apiParam {number{0-10位}} missionId 任务ID
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccess crawlerStatus 爬取状态 0、未开始，1、爬取中，2、爬取完成，3、停止爬取，4、终止爬取任务
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "data": {
     * "id": 1,
     * "name": "testmission",
     * "description": "测试",
     * "status": 0,
     * "crawlerStatus":1
     * },
     * "operateSuccess": true
     * }
     */
    @RequestMapping(value = "/missionDetail", method = RequestMethod.GET)
    @ResponseBody
    public MsgMap missionDetail(@RequestParam("missionId") Long missionId) {
        MsgMap msgMap = new MsgMap();

        try {
            RobotNlpUnion robotNlpUnion = missionService.getMission(missionId);
            msgMap.doSuccess(robotNlpUnion);
        } catch (Exception e) {
            logger.error("获取任务详情出错", e);
            msgMap.doFail();
        }

        return msgMap;
    }


    /**
     * @api {get} /mission/crawlerDetail 0、爬虫详情
     * @apiName crawlerDetail
     * @apiGroup mission-crawler
     * @apiParam {number{0-10位}} missionId 任务ID
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccess crawlerStatus 爬取状态 0、未开始，1、爬取中，2、爬取完成，3、停止爬取，4、终止爬取任务
     * @apiSuccess startPage 爬取页面
     * @apiSuccess quantity 爬取数量
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "data": {
     * "id": 1,
     * "missionId": 1,
     * "crawlerStatus": 1,
     * "crawlerPages": [
     * {
     * "id": 1,
     * "startPage": "http://licaishi.sina.com.cn/web/index?ind_id=1&fee=all&page=1",
     * "quantity": 50
     * }
     * ]
     * },
     * "operateSuccess": true
     * }
     */
    @RequestMapping(value = "/crawlerDetail", method = RequestMethod.GET)
    @ResponseBody
    public MsgMap crawlerDetail(@RequestParam("missionId") Long missionId) {
        MsgMap msgMap = new MsgMap();
        try {
            RobotCrawlerDetail detail = missionService.getCrawlerDetail(missionId);
            msgMap.doSuccess(detail);
        } catch (Exception e) {
            logger.error("查询爬虫详情出错", e);
            msgMap.doFail();
        }

        return msgMap;
    }

    /**
     * 训练集模版下载
     * 弃用
     *
     * @param request
     * @param response
     * @param name
     * @return
     */
    @Deprecated
    @RequestMapping(value = "stencilDownload", method = RequestMethod.GET)
    @ResponseBody
    public MsgMap stencilDownload(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @RequestParam(value = "name", required = false) String name) {
        MsgMap msgMap = new MsgMap();
        try {
            request.setCharacterEncoding("UTF-8");
            // Http 1.0 header
            response.setCharacterEncoding("UTF-8");
            response.setDateHeader("Expires", 0);
            response.addHeader("Pragma", "no-cache");
            // Http 1.1 header
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("text/html;charset=UTF-8");
            String stencilFilePath = YamlConfigUtil.getInstance().getConfig("path.csvStencilPath");
            FileDownloadUtil.downloadExcel(stencilFilePath, name, request, response);
        } catch (UnsupportedEncodingException e) {
            logger.error("下载训练集模版出错", e);
            msgMap.doFail();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msgMap;
    }


    /**
     * 训练集上传
     * 弃用
     *
     * @param files
     * @param request
     * @param response
     * @return
     */
    @Deprecated
    @RequestMapping("/trainDataUpload")
    @ResponseBody
    public MsgMap trainDataUpload(@RequestParam MultipartFile[] files,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        MsgMap msgMap = new MsgMap();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        try {
            request.setCharacterEncoding("UTF-8");
            String uploadFilePath = YamlConfigUtil.getInstance().getConfig("path.uploadFilePath");
            for (MultipartFile file : files) {
                File targetFile = new File(uploadFilePath + File.separator + file.getOriginalFilename());
                file.transferTo(targetFile);
                //todo 文件内容格式验证
            }
            msgMap.doSuccess();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return msgMap;
    }

    /**
     * @api {post} /mission/addMission 3、添加任务
     * @apiName addMission
     * @apiGroup mission
     * @apiParam {string{0-255}} name 任务名称
     * @apiParam {string{0-1000}} description 任务描述
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "operateSuccess": true
     * }
     */
    @RequestMapping(value = "/addMission", method = RequestMethod.POST)
    @ResponseBody
    public MsgMap addMission(@ModelAttribute("mission") RobotNlpMission mission) {
        MsgMap msgMap = new MsgMap();

        try {
            missionService.addMissions(mission);
            msgMap.doSuccess();
        } catch (Exception e) {
            logger.error("新增失败", e);
            msgMap.doFail();
        }
        return msgMap;
    }

    /**
     * @api {get} /mission/trainDataDetail 1、获取训练详情
     * @apiName trainDataDetail
     * @apiGroup mission-train
     * @apiParam {number{0-10位}} missionId 任务id
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccess processStatus 当前训练集状态 0、空闲，1、运行中，2、完成，3、错误
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "data": {
     * "missionId": 2,
     * "processStatus": 0,
     * "createTime": 1511322502000
     * },
     * "operateSuccess": true
     * }
     */
    @RequestMapping(value = "/trainDataDetail", method = RequestMethod.GET)
    @ResponseBody
    public MsgMap trainDataDetail(@RequestParam("missionId") Long missionId) {
        MsgMap msgMap = new MsgMap();
        try {
            RobotTrainDataDetail trainDataDetail = missionService.getTrainData(missionId);
            msgMap.doSuccess(trainDataDetail);
        } catch (Exception e) {
            logger.error("获取训练详情出错", e);
            msgMap.doFail();
        }
        return msgMap;
    }

    /**
     * @api {post} /mission/generateTrainData 2、生成训练集
     * @apiName generateTrainData
     * @apiGroup mission-train
     * @apiParam {number{0-10位}} missionId 任务id
     * @apiParam {number{0-10位}} balanceNum 分类数据平均化数值
     * @apiParam {number{0-10位}} dropNum 分类数据丢弃值
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccess data 生成训练集可请求接口
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "data": "http://183.2.191.55:10086/platform-modelhelper/word2vec/saveData?missionId=2&balanceNum=5&dropNum=2",
     * "operateSuccess": true
     * }
     * @apiError data 失败原因
     * @apiErrorExample {json} response:
     * {
     * "code": "1",
     * "data": "当前暂无空闲服务，请排队等待任务完成。",
     * "operateSuccess": false
     * }
     */
    @RequestMapping(value = "/generateTrainData", method = RequestMethod.POST)
    @ResponseBody
    public MsgMap generateTrainData(@RequestParam("missionId") Long missionId,
                                    @RequestParam("balanceNum") Integer balanceNum,
                                    @RequestParam("dropNum") Integer dropNum) {
        //todo 平均化 少于n条的数据丢弃
        MsgMap msgMap = new MsgMap();

        try {
            Map<String, Object> result = missionService.generateTrainData(missionId, balanceNum, dropNum);
            if ((boolean) result.get("success")) {
                msgMap.doSuccess(result.get("msg"));
            } else {
                msgMap.doFail(result.get("msg"));
            }
        } catch (Exception e) {
            logger.error("生成训练集过程中出错", e);
            msgMap.doFail("生成训练集过程中出错,请联系管理员");
        }
        return msgMap;
    }

    /**
     * @api {post} /mission/addWord2VecConf 1、添加word2vec配置
     * @apiName addWord2VecConf
     * @apiGroup mission-word2vec
     * @apiParam {number{0-10位}} missionid 任务id
     * @apiParam {number{0-11位}} minwordfrequency word2vec抛弃词频
     * @apiParam {number{0-11位}} iteration word2vec迭代次数
     * @apiParam {number{0-11位}} seed word2vec种子数
     * @apiParam {number{0-11位}} layersize word2vec向量维度
     * @apiParam {number{0-11位}} learningrate word2vec学习率
     * @apiParam {number{0-11位}} windowsize word2vec窗口大小
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     */
    @RequestMapping(value = "/addWord2VecConf", method = RequestMethod.POST)
    @ResponseBody
    public MsgMap addWord2VecConf(@ModelAttribute("word2Vec") RobotNlpConfigWord2vec word2vec) {
        MsgMap msgMap = new MsgMap();

        try {
            Map<String, Object> result = missionService.addWord2VecConf(word2vec);
            if ((boolean) result.get("success")) {
                msgMap.doSuccess(result.get("msg"));
            } else {
                msgMap.doFail(result.get("msg"));
            }
        } catch (Exception e) {
            logger.error("配置任务对应word2vec失败", e);
            msgMap.doFail("添加word2vec配置失败");
        }

        return msgMap;
    }

    /**
     * @api {post} /mission/trainWord2vec  2、训练word2vec
     * @apiName trainWord2vec
     * @apiGroup mission-word2vec
     * @apiParam {number{0-10位}} missionId 任务id
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccess data 训练word2vec可请求接口
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "data": "http://183.2.191.55:10086/platform-modelhelper/word2vec/startTrainMission",
     * "operateSuccess": true
     * }
     * @apiError data 错误原因
     * @apiErrorExample {json} response:
     * {
     * "code": "1",
     * "data": "当前暂无空闲服务，请排队等待任务完成。",
     * "operateSuccess": false
     * }
     */
    @RequestMapping(value = "/trainWord2vec", method = RequestMethod.POST)
    @ResponseBody
    public MsgMap trainWord2vec(@RequestParam("missionId") Long missionId) {
        MsgMap msgMap = new MsgMap();
        try {
            Map<String, Object> result = missionService.trainWord2vec(missionId);
            if ((boolean) result.get("success")) {
                msgMap.doSuccess(result.get("msg"));
            } else {
                msgMap.doFail(result.get("msg"));
            }
        } catch (Exception e) {
            logger.error("训练word2vec过程中出错", e);
            msgMap.doFail("训练word2vec过程中出错");
        }
        return msgMap;
    }

    /**
     * @api {get} /mission/word2vecDetail 3、word2vec词典配置详情
     * @apiName word2vecDetail
     * @apiGroup mission-word2vec
     * @apiParam {number{0-10位}} missionId 任务ID
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccess minWordFrequency 抛弃词频
     * @apiSuccess iteration 迭代次数
     * @apiSuccess seed 种子数
     * @apiSuccess layerSize 向量维度
     * @apiSuccess learningRate 学习率
     * @apiSuccess windowSize 窗口大小
     * @apiSuccess processStatus 当前word2vec状态 0、空闲，1、运行中，2、完成，3、错误
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "data": {
     * "id": 2,
     * "missionId": 2,
     * "minWordFrequency": 5,
     * "iteration": 1,
     * "seed": 42,
     * "layerSize": 100,
     * "learningRate": 0.01,
     * "windowSize": 5,
     * "processStatus": 0
     * },
     * "operateSuccess": true
     * }
     */
    @RequestMapping(value = "/word2vecDetail", method = RequestMethod.GET)
    @ResponseBody
    public MsgMap word2vecDetail(@RequestParam("missionId") Long missionId) {
        MsgMap msgMap = new MsgMap();
        try {
            RobotWord2Vec word2Vec = missionService.getWord2Vec(missionId);
            msgMap.doSuccess(word2Vec);
        } catch (Exception e) {
            logger.error("查询word2vec详情出错", e);
            msgMap.doFail();
        }
        return msgMap;
    }

    /**
     * @api {post} /mission/addNetConf 1、添加模型配置
     * @apiName addNetConf
     * @apiGroup mission-model
     * @apiParam {string{0-255}} splitRate 训练集和测试集拆分比率
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     */
    @RequestMapping(value = "/addNetConf", method = RequestMethod.POST)
    @ResponseBody
    public MsgMap addNetConf(@ModelAttribute("configNet") RobotNlpConfigNet configNet) {
        MsgMap msgMap = new MsgMap();
        try {
            Map<String, Object> res = missionService.addNetConf(configNet);
            if ((boolean) res.get("success")) {
                msgMap.doSuccess(res.get("msg"));
            } else {
                msgMap.doFail(res.get("msg"));
            }
        } catch (Exception e) {
            logger.error("添加网络配置出错", e);
            msgMap.doFail("添加网络配置出错");
        }
        return msgMap;
    }

    /**
     * @api {get} /mission/netDetail 2、训练网络配置详情
     * @apiName allConf
     * @apiGroup mission-model
     * @apiParam {number{0-10位}} missionId 任务ID
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccess processStatus 当前网络状态 0、空闲，1、运行中，2、完成，3、错误
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "operateSuccess": true
     * }
     */
    @RequestMapping(value = "/netDetail", method = RequestMethod.GET)
    @ResponseBody
    public MsgMap netDetail(@RequestParam("missionId") Long missionId) {
        MsgMap msgMap = new MsgMap();
        msgMap.doSuccess();
        return msgMap;
    }

}
