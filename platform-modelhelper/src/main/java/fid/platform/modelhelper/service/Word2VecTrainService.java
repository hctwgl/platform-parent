package fid.platform.modelhelper.service;

import fid.platform.core.common.pojo.commons.LabeledSentence;
import fid.platform.core.common.pojo.robot.RobotNlpConfigWord2vec;

public interface Word2VecTrainService {

		/**
		 * 通过missionId获取任务所需的语料
		 * @param missionId
		 * @return
		 */
		LabeledSentence trainDataToLabeledSentence(Long missionId,int avgNum,int truncatNum) throws Exception;

		void saveLabeledSentenceToLocal(Long missionId,LabeledSentence labeledSentence) throws Exception;

		RobotNlpConfigWord2vec getWord2VecConfigByMissionId(Long missionId) throws Exception;

		//---------------训练方式---------------
		void acceptParamsAndTrain(RobotNlpConfigWord2vec configWord2vec) throws Exception;

		void acceptMissionIdAndTrain(Long missionId) throws Exception;
}
