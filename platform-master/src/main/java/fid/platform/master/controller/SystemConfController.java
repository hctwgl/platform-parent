package fid.platform.master.controller;

import fid.platform.core.common.pojo.commons.MsgMap;
import fid.platform.core.common.pojo.robot.SystemConf;
import fid.platform.core.common.pojo.robot.SystemModule;
import fid.platform.core.common.pojo.robot.SystemModuleStatus;
import fid.platform.master.service.SystemConfService;
import fid.platform.master.service.SystemModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by mengtian on 2017/11/2
 * 系统配置
 */
@Controller
@RequestMapping("systemConf")
public class SystemConfController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private SystemConfService systemConfService;

    @Resource
    private SystemModuleService systemModuleService;

    /**
     * @api {get} /systemConf/allConf 获取系统项目所有配置
     * @apiName allConf
     * @apiGroup systemConf
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccess isShow 页面是否展示0、展示，1、不展示
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "data": [
     * {
     * "id": 1,
     * "moduleId": 1,
     * "confKey": "path",
     * "confDesc": "测试修改",
     * "confValue": "/user/local/demo",
     * "isShow": 1,
     * "createTime": 1509695366000
     * }
     * ],
     * "operateSuccess": true
     * }
     */
    @RequestMapping(value = "/allConf", method = RequestMethod.GET)
    @ResponseBody
    public MsgMap allConf() {
        MsgMap msgMap = new MsgMap();
        try {
            List<SystemConf> confs = systemConfService.getAllConf();
            msgMap.doSuccess(confs);
        } catch (Exception e) {
            logger.error("查询系统配置失败", e);
            msgMap.doFail();
        }
        return msgMap;
    }

    /**
     * @api {post} /systemConf/editConf 编辑系统配置
     * @apiName editConf
     * @apiGroup systemConf
     * @apiParam {number{0-8位}} id 配置项id
     * @apiParam {string{0-200}} confDesc 配置说明
     * @apiParam {string{0-200}} confValue 配置值
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "operateSuccess": true
     * }
     */
    @RequestMapping(value = "/editConf", method = RequestMethod.POST)
    @ResponseBody
    public MsgMap editConf(@ModelAttribute SystemConf systemConf) {
        MsgMap msgMap = new MsgMap();
        try {
            systemConfService.editConf(systemConf);
            msgMap.doSuccess();
        } catch (Exception e) {
            logger.error("更新配置失败", e);
            msgMap.doFail();
        }
        return msgMap;
    }

    /**
     * @api {post} /systemConf/addConf 新增系统配置
     * @apiName addConf
     * @apiGroup systemConf
     * @apiParam {number{0-8位}} id 配置项id
     * @apiParam {number{0-8位}} moduleId 所属模块id
     * @apiParam {string{0-200}} confKey 配置项key
     * @apiParam {string{0-200}} confDesc 配置说明
     * @apiParam {string{0-200}} confValue 配置值
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "operateSuccess": true
     * }
     */
    @RequestMapping(value = "/addConf", method = RequestMethod.POST)
    @ResponseBody
    public MsgMap addConf(@ModelAttribute SystemConf systemConf) {
        MsgMap msgMap = new MsgMap();
        try {
            systemConfService.addConf(systemConf);
            msgMap.doSuccess();
        } catch (Exception e) {
            logger.error("添加系统配置出错", e);
            msgMap.doFail();
        }

        return msgMap;
    }

    /**
     * @api {get} /systemConf/modules 系统存在模块
     * @apiName modules
     * @apiGroup systemConf
     * @apiParam {string{0-200}} moduleName(可选) 模块名称
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "data": [
     * {
     * "id": 1,
     * "moduleName": "system",
     * "moduleDesc": "系统"
     * }
     * ],
     * "operateSuccess": true
     * }
     */
    @RequestMapping(value = "/modules", method = RequestMethod.GET)
    @ResponseBody
    public MsgMap modules(@RequestParam(value = "moduleName", required = false) String moduleName) {
        MsgMap msgMap = new MsgMap();
        try {
            List<SystemModule> modules = systemModuleService.getModules(moduleName);
            msgMap.doSuccess(modules);
        } catch (Exception e) {
            logger.error("查询模块出错", e);
            msgMap.doFail();
        }
        return msgMap;
    }

    /**
     * @api {get} /systemConf/moduleStatus/ 系统各模块机器状态
     * @apiName moduleStatus
     * @apiGroup systemConf
     * @apiParam {number{0-10位}} moduleId 模块id
     * @apiParam {string{0-200}} moduleName 模块名称
     * @apiParam {string{0-20}} machineHost 模块所在机器host
     * @apiParam {number{0-3位}} moduleStatus 模块状态 0、空闲，1、使用中，2、下线
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "data": [
     * {
     * "id": 1,
     * "moduleId": 3,
     * "moduleName": "向量词典",
     * "machineHost": "183.2.191.55:10086",
     * "moduleStatus": 0
     * }
     * ],
     * "operateSuccess": true
     * }
     */
    @RequestMapping(value = "/moduleStatus", method = RequestMethod.GET)
    @ResponseBody
    public MsgMap moduleStatus(@ModelAttribute SystemModuleStatus moduleStatus) {
        MsgMap msgMap = new MsgMap();
        try {
            List<SystemModuleStatus> moduleStatuses = systemModuleService.moduleStatus(moduleStatus);
            msgMap.doSuccess(moduleStatuses);
        } catch (Exception e) {
            logger.error("获取系统模块状态信息出错", e);
            msgMap.doFail();
        }
        return msgMap;
    }

    /**
     * @api {post} /systemConf/addModuleStatus 添加模块机器配置
     * @apiName addModuleStatus
     * @apiGroup systemConf
     * @apiParam {number{0-10位}} moduleId 模块id
     * @apiParam {string{0-200}} moduleName 模块名称
     * @apiParam {string{0-24}} machineHost 模块服务所在host
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     * @apiSuccessExample {json} response:
     * {
     * "code": "0",
     * "operateSuccess": true
     * }
     */
    @RequestMapping(value = "/addModuleStatus", method = RequestMethod.POST)
    @ResponseBody
    public MsgMap addModuleStatus(@ModelAttribute SystemModuleStatus moduleStatus) {
        MsgMap msgMap = new MsgMap();
        try {
            Map<String, Object> result = systemModuleService.addModuleStatus(moduleStatus);
            if ((boolean) result.get("success")) {
                msgMap.doSuccess();
            } else {
                msgMap.doFail(result.get("msg"));
            }
        } catch (Exception e) {
            logger.error("添加modulestatus出错", e);
            msgMap.doFail();
        }
        return msgMap;
    }
}
