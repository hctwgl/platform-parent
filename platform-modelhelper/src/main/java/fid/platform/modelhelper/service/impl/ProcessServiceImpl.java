package fid.platform.modelhelper.service.impl;

import com.google.common.collect.Maps;
import fid.platform.core.common.constant.ProcessTypeConstants;
import fid.platform.core.common.pojo.robot.RobotNlpProcessStatus;
import fid.platform.database.robot.mapper.RobotNlpProcessStatusMapper;
import fid.platform.modelhelper.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class ProcessServiceImpl implements ProcessService {

		private static Logger logger = LoggerFactory.getLogger(ProcessServiceImpl.class);

		@Resource
		private RobotNlpProcessStatusMapper robotNlpProcessStatusMapper;

		@Override
		public Map<String, String> getLocalJavaProcess() throws Exception {
				Map<String, String> resultMap = Maps.newHashMap();
				Process ps = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "jps"});
				ps.waitFor();
				InputStream inputStream = ps.getInputStream();
				BufferedReader read = new BufferedReader(new InputStreamReader(inputStream));
				String line = null;
				while ((line = read.readLine()) != null) {
						String[] split = line.split(" ");
						resultMap.put(split[0], split[1]);
				}
				return resultMap;
		}

		@Override
		public void killOneProcess(String processId) throws Exception {
				Process ps = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "kill -9 " + processId});
				ps.waitFor();
				ps.destroy();
		}

		@Override
		public boolean isRunEnable(Long missionId, int processType) {
				List<RobotNlpProcessStatus> processList = robotNlpProcessStatusMapper.getAllProcessByMissionId(missionId);
				//无任务直接为true
				if (processList == null || processList.size() == 0) {
						return false;
				}
				//步骤检测
				RobotNlpProcessStatus labeledSentenceStatus = robotNlpProcessStatusMapper.getNewestStatusByTypeAndMissionId(missionId, ProcessTypeConstants.LabeledSentence_Generation);
				RobotNlpProcessStatus word2vecStatus = robotNlpProcessStatusMapper.getNewestStatusByTypeAndMissionId(missionId, ProcessTypeConstants.Word2vec_Generation);
				RobotNlpProcessStatus neuralNetStatus = robotNlpProcessStatusMapper.getNewestStatusByTypeAndMissionId(missionId, ProcessTypeConstants.NeuralNet_Generation);

				switch (processType) {
						case ProcessTypeConstants.LabeledSentence_Generation:
								if (!labeledSentenceStatus.getProcessstatus().equals(ProcessTypeConstants.Process_Free) ||
												!word2vecStatus.getProcessstatus().equals(ProcessTypeConstants.Process_Free) ||
												!neuralNetStatus.getProcessstatus().equals(ProcessTypeConstants.Process_Free)) {
										return false;
								}
								break;
						case ProcessTypeConstants.Word2vec_Generation:
								if (!labeledSentenceStatus.getProcessstatus().equals(ProcessTypeConstants.Process_Finished) ||
												!word2vecStatus.getProcessstatus().equals(ProcessTypeConstants.Process_Free) ||
												!neuralNetStatus.getProcessstatus().equals(ProcessTypeConstants.Process_Free)) {
										return false;
								}
								break;
						case ProcessTypeConstants.NeuralNet_Generation:
								if (!labeledSentenceStatus.getProcessstatus().equals(ProcessTypeConstants.Process_Finished) ||
												!word2vecStatus.getProcessstatus().equals(ProcessTypeConstants.Process_Finished) ||
												!neuralNetStatus.getProcessstatus().equals(ProcessTypeConstants.Process_Free)) {
										return false;
								}
								break;
				}
				return true;
		}

		@Override
		public boolean acceptAndChangeStatus(Long missionId, int processType, int status) {
				try {
						RobotNlpProcessStatus word2vecStatus = robotNlpProcessStatusMapper.getNewestStatusByTypeAndMissionId(missionId, processType);
						word2vecStatus.setProcessstatus(status);
						robotNlpProcessStatusMapper.updateByPrimaryKey(word2vecStatus);
				} catch (Exception e) {
						e.printStackTrace();
						return false;
				}
				return true;
		}

		@Override
		public boolean resetAllStatusByMissionId(Long missionId) {
				try {
						List<RobotNlpProcessStatus> allProcessList = robotNlpProcessStatusMapper.getAllProcessByMissionId(missionId);
						for (RobotNlpProcessStatus robotNlpProcessStatus : allProcessList) {
								if (robotNlpProcessStatus.getProcessstatus() == ProcessTypeConstants.Process_Running) {
										return false;
								}
						}
						for (RobotNlpProcessStatus robotNlpProcessStatus : allProcessList) {
								robotNlpProcessStatus.setCreatetime(new Date());
								robotNlpProcessStatus.setProcessstatus(ProcessTypeConstants.Process_Free);
								robotNlpProcessStatusMapper.updateByPrimaryKey(robotNlpProcessStatus);
						}
				} catch (Exception e) {
						logger.error("重设训练进程状态失败", e);
						return false;
				}
				return true;
		}

}
