package fid.platform.modelhelper.controller;

import fid.platform.core.common.pojo.commons.MsgMap;
import fid.platform.modelhelper.service.ProcessService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/processHandler")
public class ProcessHandler {

		@Resource
		private ProcessService processService;

		/**
		 * @api {get,post} /processHandler/getJpsStatus 获取当前机器java进程
		 * @apiName getJpsStatus
		 * @apiGroup processHandler
		 * @apiSuccess code 0、成功 1、失败
		 * @apiSuccess operateSuccess true false
		 * @apiSuccess data id name
		 */
		@RequestMapping("/getJpsStatus")
		public MsgMap getJpsStatus() {
				MsgMap msgMap = new MsgMap();
				try {
						Map<String, String> localJavaProcess = processService.getLocalJavaProcess();
						msgMap.doSuccess(localJavaProcess);
				} catch (Exception e) {
						msgMap.put(MsgMap.key_msg, "获取本地java进程失败");
						msgMap.doFail();
						e.printStackTrace();
				}
				return msgMap;
		}

		/**
		 * @api {post} /processHandler/killProcess 停止某个java进程
		 * @apiName killProcess
		 * @apiGroup processHandler
		 * @apiParam {String} processId
		 * @apiSuccess code 0、成功 1、失败
		 * @apiSuccess operateSuccess true false
		 */
		@RequestMapping("/killProcess")
		public MsgMap killProcess(@RequestParam("processId") String processId) {
				MsgMap msgMap = new MsgMap();
				try {
						processService.killOneProcess(processId);
				} catch (Exception e) {
						msgMap.put(MsgMap.key_msg, "停止进程失败");
						msgMap.doFail();
						e.printStackTrace();
				}
				return msgMap;
		}

		/**
		 * @api {post} /processHandler/resetAllStatus 重设训练进程状态所有为free
		 * @apiName resetAllStatus
		 * @apiGroup processHandler
		 * @apiParam missionId 任务id
		 * @apiSuccess code 0、成功 1、失败
		 * @apiSuccess operateSuccess true false
		 */
		@RequestMapping("/resetAllStatus")
		public MsgMap resetAllStatus(@RequestParam("missionId")Long missionId){
				MsgMap msgMap = new MsgMap();
				try {
						boolean b = processService.resetAllStatusByMissionId(missionId);
						if (b){
								msgMap.put(MsgMap.key_msg,"重设成功");
								msgMap.doSuccess();
						}else {
								msgMap.put(MsgMap.key_msg,"重设失败,请检查是否有任务在运行状态,请先停止该任务");
								msgMap.doFail();
						}
				}catch (Exception e){
						msgMap.put(MsgMap.key_msg,"重设失败,可能由于服务器原因");
						msgMap.doFail();
						e.printStackTrace();
				}
				return msgMap;
		}

}
