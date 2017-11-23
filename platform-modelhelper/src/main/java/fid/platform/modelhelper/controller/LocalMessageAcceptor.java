package fid.platform.modelhelper.controller;

import fid.platform.core.common.pojo.commons.MsgMap;
import fid.platform.modelhelper.service.ProcessService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/messageAcceptor")
public class LocalMessageAcceptor {

    @Resource
    private ProcessService processService;

    /**
     * @api {post} /messageAcceptor/acceptProcessStatus 模块状态修改接口
     * @apiName acceptProcessStatus
     * @apiGroup messageAcceptor
     * @apiParam missionId 任务id
     * @apiParam processType 进程类别
     * @apiParam status 进程状态
     * @apiSuccess code 0、成功 1、失败
     * @apiSuccess operateSuccess true false
     */
    @RequestMapping("/acceptProcessStatus")
    public MsgMap acceptProcessStatus(@RequestParam("missionId") Long missionId,
                                      @RequestParam("processType") int processType,
                                      @RequestParam("status") int status) {
        MsgMap msgMap = new MsgMap();
        if (processService.acceptAndChangeStatus(missionId, processType, status)) {
            msgMap.put(MsgMap.key_msg, "status changed ---->" + status);
            msgMap.doSuccess();
        } else {
            msgMap.put(MsgMap.key_msg, "status change failed ---->missionId:" + missionId);
            msgMap.doFail();
        }
        return msgMap;
    }

}
