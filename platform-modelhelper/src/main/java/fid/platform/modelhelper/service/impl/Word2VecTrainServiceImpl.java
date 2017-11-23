package fid.platform.modelhelper.service.impl;

import com.google.common.collect.Lists;
import fid.platform.core.common.constant.ProcessTypeConstants;
import fid.platform.core.common.pojo.commons.LabeledSentence;
import fid.platform.core.common.pojo.robot.RobotNlpConfigWord2vec;
import fid.platform.core.common.pojo.robot.RobotNlpProcessStatus;
import fid.platform.core.common.pojo.robot.RobotNlpTrainData;
import fid.platform.database.robot.mapper.RobotNlpConfigWord2vecMapper;
import fid.platform.database.robot.mapper.RobotNlpProcessStatusMapper;
import fid.platform.database.robot.mapper.RobotNlpTrainDataMapper;
import fid.platform.modelhelper.service.ProcessService;
import fid.platform.modelhelper.service.Word2VecTrainService;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class Word2VecTrainServiceImpl implements Word2VecTrainService {

		@Resource
		private RobotNlpTrainDataMapper robotNlpTrainDataMapper;

		@Resource
		private RobotNlpConfigWord2vecMapper robotNlpConfigWord2vecMapper;

		@Resource
		private RobotNlpProcessStatusMapper robotNlpProcessStatusMapper;

		@Resource
		private ProcessService processService;

		@Override
		public LabeledSentence trainDataToLabeledSentence(Long missionId,int avgNum,int truncatNum) throws Exception {
				//获取状态,并更新为运行中
				RobotNlpProcessStatus labeledSentenceStatus = robotNlpProcessStatusMapper.getNewestStatusByTypeAndMissionId(missionId, ProcessTypeConstants.LabeledSentence_Generation);
				labeledSentenceStatus.setProcessstatus(ProcessTypeConstants.Process_Running);
				labeledSentenceStatus.setCreatetime(new Date());
				robotNlpProcessStatusMapper.updateByPrimaryKey(labeledSentenceStatus);
				try {
						List<RobotNlpTrainData> trainDataByMissionId = robotNlpTrainDataMapper.getTrainDataByMissionId(missionId);
						List<String> labelList = Lists.newArrayList();
						List<String> sentenceList = Lists.newArrayList();
						for (RobotNlpTrainData robotNlpTrainData : trainDataByMissionId) {
								labelList.add(String.valueOf(robotNlpTrainData.getTid()));
								sentenceList.add(robotNlpTrainData.getTxt());
						}
						LabeledSentence labeledSentence = new LabeledSentence.Builder().setLabels(labelList).setSentences(sentenceList).build();
						if (avgNum > 1){
								labeledSentence = labeledSentence.balanceOnLimitControll(avgNum, truncatNum);
						}
						//存至本地
						saveLabeledSentenceToLocal(missionId,labeledSentence);
						//完成修改状态
						labeledSentenceStatus.setProcessstatus(ProcessTypeConstants.Process_Finished);
						labeledSentenceStatus.setCreatetime(new Date());
						robotNlpProcessStatusMapper.updateByPrimaryKey(labeledSentenceStatus);
						return labeledSentence;
				}catch (Exception e){
						labeledSentenceStatus.setProcessstatus(ProcessTypeConstants.Process_Error);
						labeledSentenceStatus.setCreatetime(new Date());
						robotNlpProcessStatusMapper.updateByPrimaryKey(labeledSentenceStatus);
						e.printStackTrace();
				}
				return null;
		}

		@Override
		public void saveLabeledSentenceToLocal(Long missionId, LabeledSentence labeledSentence) throws Exception {
				//获取配置
				RobotNlpConfigWord2vec word2vecConfig = getWord2VecConfigByMissionId(missionId);
				String dataPath = word2vecConfig.getDatapath();
				File f = new File(dataPath);
				File parentFile = f.getParentFile();
				if (!parentFile.exists()){
						parentFile.mkdirs();
				}
				//储存
				LinkedBuffer buffer = LinkedBuffer.allocate();
				Schema<LabeledSentence> schema = RuntimeSchema.getSchema(LabeledSentence.class);
				BufferedOutputStream outputStream = new BufferedOutputStream(
								new FileOutputStream(
												new File(dataPath)
								));
				ProtobufIOUtil.writeTo(outputStream, labeledSentence, schema, buffer);
				outputStream.flush();
				outputStream.close();

		}

		@Override
		public void acceptParamsAndTrain(RobotNlpConfigWord2vec configWord2vec) throws Exception {
				try {
						RobotNlpProcessStatus word2vecStatus = robotNlpProcessStatusMapper.getNewestStatusByTypeAndMissionId(configWord2vec.getMissionid(), ProcessTypeConstants.Word2vec_Generation);
						word2vecStatus.setProcessstatus(ProcessTypeConstants.Process_Running);
						word2vecStatus.setCreatetime(new Date());
						robotNlpProcessStatusMapper.updateByPrimaryKey(word2vecStatus);
						File f = new File(configWord2vec.getLogfile());
						File parentFile = f.getParentFile();
						if (!parentFile.exists()){
								parentFile.mkdirs();
						}
						StringBuilder execStr = new StringBuilder();
						//添加头
						execStr.append("nohup java -jar ");
						//添加jar位置
						execStr.append(configWord2vec.getJarfile() + " ");
						//添加输出位置
						execStr.append("-op " + configWord2vec.getOutputpath() + " ");
						//missionId
						execStr.append("-mid " + configWord2vec.getMissionid() + " ");
						//添加数据路径参数
						execStr.append("-dp " + configWord2vec.getDatapath() + " ");
						//添加最低抛弃词频
						execStr.append("-mwf " + configWord2vec.getMinwordfrequency() + " ");
						//添加遍历次数
						execStr.append("-iter " + configWord2vec.getIteration() + " ");
						//添加随机种数
						execStr.append("-seed " + configWord2vec.getSeed() + " ");
						//添加词典维度
						execStr.append("-ls " + configWord2vec.getLayersize() + " ");
						//添加学习率
						execStr.append("-lr " + configWord2vec.getLearningrate() + " ");
						//添加窗口大小
						execStr.append("-ws " + configWord2vec.getWindowsize() + " ");
						//日志输出尾
						execStr.append("> " + configWord2vec.getLogfile() + " 2>&1 &");
						Process ps = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", execStr.toString()});
//						InputStream inputStream = ps.getInputStream();
						ps.waitFor();
						ps.destroy();
				} catch (IOException e) {
						e.printStackTrace();
				} catch (InterruptedException e) {
						e.printStackTrace();
				}
		}

		@Override
		public void acceptMissionIdAndTrain(Long missionId) throws Exception {
				RobotNlpConfigWord2vec word2VecConfigByMissionId = getWord2VecConfigByMissionId(missionId);
				acceptParamsAndTrain(word2VecConfigByMissionId);
		}

		@Override
		public RobotNlpConfigWord2vec getWord2VecConfigByMissionId(Long missionId) throws Exception{
				return robotNlpConfigWord2vecMapper.getWord2VecConfigByMissionId(missionId);
		}

}
