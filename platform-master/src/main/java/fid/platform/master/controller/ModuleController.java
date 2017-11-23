package fid.platform.master.controller;

import fid.platform.master.service.SystemModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mengtian on 2017/11/19
 * <p>
 * 服务注册
 */
@Controller
@RequestMapping("module")
public class ModuleController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private SystemModuleService systemModuleService;

    /**
     * @api {get,post} /service/serviceRegister 服务上线
     * @apiName serviceRegister
     * @apiGroup module
     * @apiParam {number{0-2位}} moduleType 模块类型
     * @apiSuccess number 模块在系统中注册的id,任意大于0的数字
     * @apiSuccessExample {json} response:
     * [
     * 3,
     * 4
     * ]
     * @apiError number -1
     */
    @RequestMapping(value = "/serviceRegister", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public List<Integer> serviceRegister(@RequestParam("moduleType") Integer moduleType,
                                         HttpServletRequest request) {
        String host = request.getRemoteHost();
        List<Integer> modules = new ArrayList<>();
        try {
            modules = systemModuleService.registerModule(host, moduleType);
        } catch (Exception e) {
            logger.error("注册服务出错", e);
            modules.add(-1);
        }
        return modules;

    }

    /**
     * @api {post} /service/serviceLogout 服务下线
     * @apiName serviceOutline
     * @apiGroup module
     * @apiParam {number{0-2位}} serviceIds master分配给服务id,支持数组
     */
    @RequestMapping(value = "/serviceLogout", method = RequestMethod.POST)
    @ResponseBody
    public void serviceLogout(@RequestParam("serviceIds") Integer[] serviceIds) {
        try {
            systemModuleService.logoutModule(serviceIds);
        } catch (Exception e) {
            logger.error("服务注销失败", e);
        }
    }

}
