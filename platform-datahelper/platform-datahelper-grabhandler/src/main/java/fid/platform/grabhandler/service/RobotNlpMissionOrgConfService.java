package fid.platform.grabhandler.service;

import fid.platform.core.common.pojo.robot.RobotNlpMissionOrgConf;

public interface RobotNlpMissionOrgConfService {

	int insertRobotNlpMissionOrgConf(RobotNlpMissionOrgConf robotNlpMissionOrgConf) throws Exception;
	
	RobotNlpMissionOrgConf getRobotNlpMissionOrgConfByMissionId(Long missionId) throws Exception;
	
	RobotNlpMissionOrgConf getRobotNlpMissionOrgConfById(Long id) throws Exception;
	
}
