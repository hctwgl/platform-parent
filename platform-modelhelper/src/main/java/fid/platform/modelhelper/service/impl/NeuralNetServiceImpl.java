package fid.platform.modelhelper.service.impl;

import fid.platform.core.common.constant.ProcessTypeConstants;
import fid.platform.core.common.pojo.robot.RobotNlpConfigNet;
import fid.platform.core.common.pojo.robot.RobotNlpProcessStatus;
import fid.platform.database.robot.mapper.RobotNlpConfigNetMapper;
import fid.platform.database.robot.mapper.RobotNlpProcessStatusMapper;
import fid.platform.modelhelper.service.NeuralNetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.net.InetAddress;
import java.util.Date;

@Service
public class NeuralNetServiceImpl implements NeuralNetService {

		private static Logger logger = LoggerFactory.getLogger(NeuralNetServiceImpl.class);

		@Resource
		private RobotNlpConfigNetMapper robotNlpConfigNetMapper;

		@Resource
		private RobotNlpProcessStatusMapper robotNlpProcessStatusMapper;

		@Override
		public String trainSimpleCnnTextModel(Long missionId) {
				String observerUrl = "";
				try {
						//获取信息
						RobotNlpConfigNet netConfig = robotNlpConfigNetMapper.getNetConfigByMissionId(missionId);
						//先修改状态
						RobotNlpProcessStatus neuralNetNewestStatus = robotNlpProcessStatusMapper.getNewestStatusByTypeAndMissionId(missionId, ProcessTypeConstants.NeuralNet_Generation);
						neuralNetNewestStatus.setCreatetime(new Date());
						neuralNetNewestStatus.setProcessstatus(ProcessTypeConstants.Process_Running);
						robotNlpProcessStatusMapper.updateByPrimaryKey(neuralNetNewestStatus);
						//文件夹创建
						//log dir
						String logfile = netConfig.getLogfile();
						File f = new File(logfile);
						File parentFile = f.getParentFile();
						if (!parentFile.exists()) {
								parentFile.mkdirs();
						}
						//net dir
						String outputdir = netConfig.getOutputdir();
						f = new File(outputdir);
						if (!f.exists()) {
								f.mkdirs();
						}
						//构造命令
						StringBuilder execStr = new StringBuilder();
						//添加头
						execStr.append("nohup java -jar ");
						//添加jar位置
						execStr.append(netConfig.getJarfile() + " ");
						//添加输出位置
						execStr.append("-od " + netConfig.getOutputdir() + " ");
						//missionId
						execStr.append("-mid " + netConfig.getMissionid() + " ");
						//添加数据路径参数
						execStr.append("-dp " + netConfig.getDatapath() + " ");
						//添加学习率
						execStr.append("-lr " + netConfig.getLearningrate() + " ");
						//添加向量词典路径
						execStr.append("-vdp " + netConfig.getVecdicpath() + " ");
						//添加分割比率
						execStr.append("-spr " + netConfig.getSplitrate() + " ");
						//日志输出尾
						execStr.append("> " + netConfig.getLogfile() + " 2>&1 &");
						Process ps = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", execStr.toString()});
//						InputStream inputStream = ps.getInputStream();
						ps.waitFor();
						ps.destroy();

						InetAddress inetAddress = InetAddress.getLocalHost();
						observerUrl = inetAddress.getHostAddress() + ":9000";

				} catch (Exception e) {
						logger.error("神经网络训练启动失败",e);
				}
				return observerUrl;
		}

}
