package fid.platform.modelhelper.controller;

import fid.platform.core.common.constant.ProcessTypeConstants;
import fid.platform.core.common.pojo.commons.MsgMap;
import fid.platform.modelhelper.service.NeuralNetService;
import fid.platform.modelhelper.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/neuralNet")
public class NeuralNetController {

		private static Logger logger = LoggerFactory.getLogger(NeuralNetController.class);

		@Resource
		private NeuralNetService neuralNetService;

		@Resource
		private ProcessService processService;

		/**
		 * @api {post} /neuralNet/startSimpleTraining 开始训练最简配置神经网络
		 * @apiName resetAllStatus
		 * @apiGroup processHandler
		 * @apiParam missionId 任务id
		 * @apiSuccess code 0、成功 1、失败
		 * @apiSuccess operateSuccess true false
		 * @apiSuccess data url 网络监测地址
		 */
		@RequestMapping("/startTraining")
		public MsgMap startSimpleTraining(@RequestParam("missionId")Long missionId){
				MsgMap msgMap = new MsgMap();
				try {
						if (processService.isRunEnable(missionId, ProcessTypeConstants.NeuralNet_Generation)){
								String observerUrl = neuralNetService.trainSimpleCnnTextModel(missionId);
								msgMap.put(MsgMap.key_msg,"网络训练已经开始,请在半分钟至1分钟后通过下面的页面实时监测您的训练~");
								msgMap.doSuccess(observerUrl);
						}else {
								msgMap.put(MsgMap.key_msg,"网络训练开始失败,请检查您是否有其他任务正在执行,或者未重置错误任务");
								msgMap.doFail();
						}
				}catch (Exception e){
						msgMap.put(MsgMap.key_msg,"网络训练开始失败,可能是服务器出现问题");
						msgMap.doFail();
				}
				return msgMap;
		}

}
