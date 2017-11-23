package fid.platform.modelhelper.service;


import java.util.Map;

/**
 * 控制进程状态的服务
 */
public interface ProcessService {

		Map<String,String> getLocalJavaProcess() throws Exception;

		void killOneProcess(String processId) throws Exception;

		boolean isRunEnable(Long missionId,int processType);

		boolean acceptAndChangeStatus(Long missionId,int processType,int status);

		boolean resetAllStatusByMissionId(Long missionId);
}
