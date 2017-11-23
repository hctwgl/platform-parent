package fid.platform.grabhandler.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fid.platform.core.common.pojo.robot.RobotNlpMissionOrgConf;
import fid.platform.database.robot.mapper.RobotNlpMissionOrgConfMapper;
import fid.platform.grabhandler.service.RobotNlpMissionOrgConfService;

@Service
public class RobotNlpMissionOrgConfServiceImpl implements RobotNlpMissionOrgConfService {

	private static final Logger logger = LoggerFactory.getLogger(RobotNlpMissionOrgConfServiceImpl.class);

    @Resource
    private RobotNlpMissionOrgConfMapper robotNlpMissionOrgConfMapper;
	
    /**
     * 插入任务配置
     */
	@Override
	public int insertRobotNlpMissionOrgConf(RobotNlpMissionOrgConf robotNlpMissionOrgConf) throws Exception {
		// TODO Auto-generated method stub
		return robotNlpMissionOrgConfMapper.insert(robotNlpMissionOrgConf);
	}
	/**
	 * 根据任务id查询任务配置
	 */
	@Override
	public RobotNlpMissionOrgConf getRobotNlpMissionOrgConfByMissionId(Long missionId) throws Exception {
		// TODO Auto-generated method stub
		return robotNlpMissionOrgConfMapper.selectByMissionId(missionId);
	}
	@Override
	public RobotNlpMissionOrgConf getRobotNlpMissionOrgConfById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return robotNlpMissionOrgConfMapper.selectByPrimaryKey(id);
	}
	
}
