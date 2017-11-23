package fid.platform.modelhelper.controller;

import com.google.common.collect.Maps;
import fid.platform.core.common.constant.ProcessTypeConstants;
import fid.platform.core.common.pojo.commons.MsgMap;
import fid.platform.modelhelper.thread.Runnable.LabeledSentenceThreadContainer;
import fid.platform.modelhelper.service.ProcessService;
import fid.platform.modelhelper.service.Word2VecTrainService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/word2vec")
public class Word2VecController {

		@Resource
		private Word2VecTrainService word2VecTrainService;

		@Resource
		private ProcessService processService;

		@Resource
		private LabeledSentenceThreadContainer labeledSentenceThreadContainer;
		/**
		 * @api {post} /word2vec/saveData 保存成labeledSentence(数据准备完毕后)
		 * @apiName saveDataToLabeledSentence
		 * @apiGroup word2vec
		 * @apiPram	{number{0-10位}} missionId 任务id
		 * @apiPram	{number{0-10位}} balanceNum 平衡数量
		 * @apiPram	{number{0-10位}} dropNum 抛弃最小值
		 * @apiSuccess code 0、成功 1、失败
		 * @apiSuccess status 0、空闲 1、运行中 2、完成 3、出错
		 * @apiSuccess operateSuccess true false
		 */
		@RequestMapping("/saveData")
		public MsgMap saveData(@RequestParam("missionId") Long missionId,@RequestParam("balanceNum")int balanceNum,@RequestParam("dropNum")int dropNum){
				MsgMap msgMap = new MsgMap();
				try {
						if (processService.isRunEnable(missionId, ProcessTypeConstants.LabeledSentence_Generation)){
								labeledSentenceThreadContainer.backgroundSaveWord2vecData(missionId,balanceNum,dropNum);
								msgMap.put(MsgMap.key_msg,"数据正在存储中,之后就可以开始训练你自己的word2vec啦!");
								Map<String,Integer> dataMap = Maps.newHashMap();
								dataMap.put("status",ProcessTypeConstants.Process_Running);
								msgMap.doSuccess(dataMap);
						}else {
								msgMap.put(MsgMap.key_msg,"数据存储失败!原因可能是您有任务正在进行,或者失败的任务未重置");
								msgMap.doFail();
						}
				} catch (Exception e) {
						msgMap.put(MsgMap.key_msg,"数据存储失败!");
						Map<String,Integer> dataMap = Maps.newHashMap();
						dataMap.put("status",ProcessTypeConstants.Process_Error);
						msgMap.doFail(dataMap);
						e.printStackTrace();
				}
				return msgMap;
		}

		/**
		 * @api {post} /word2vec/startTrainMission 开始训练向量词典word2vec
		 * @apiName startTrainingWord2Vec
		 * @apiGroup word2vec
		 * @apiPram {number{0-10位}} missionId
		 * @apiSuccess code 0、成功 1、失败
		 * @apiSuccess operateSuccess true false
		 */
		@RequestMapping("/startTrainMission")
		public MsgMap startTrainMission(@RequestParam("missionId")Long missionId){
				MsgMap msgMap = new MsgMap();
				try {
						if (processService.isRunEnable(missionId,ProcessTypeConstants.Word2vec_Generation)){
								word2VecTrainService.acceptMissionIdAndTrain(missionId);
								msgMap.put(MsgMap.key_msg,"训练开始");
								msgMap.doSuccess();
						}else {
								msgMap.put(MsgMap.key_msg,"本服务器已有任务正在运行,请稍后再试");
								msgMap.doFail();
						}
				} catch (Exception e) {
						msgMap.put(MsgMap.key_msg,"训练出错,请检查数据是否正确!");
						msgMap.doFail();
						e.printStackTrace();
				}
				return msgMap;
		}

}
