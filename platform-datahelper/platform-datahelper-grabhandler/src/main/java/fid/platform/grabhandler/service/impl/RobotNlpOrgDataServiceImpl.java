package fid.platform.grabhandler.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fid.platform.core.common.pojo.robot.RobotNlpOrgData;
import fid.platform.database.robot.mapper.RobotNlpOrgDataMapper;
import fid.platform.grabhandler.service.RobotNlpOrgDataService;

@Service
public class RobotNlpOrgDataServiceImpl implements RobotNlpOrgDataService{

	private static final Logger logger = LoggerFactory.getLogger(RobotNlpOrgDataServiceImpl.class);

    @Resource
    private RobotNlpOrgDataMapper robotNlpOrgDataMapper;
	
    @Override
    public void insertRobotNlpOrgData(RobotNlpOrgData robotNlpOrgData) throws Exception {
    	robotNlpOrgDataMapper.insert(robotNlpOrgData);
    }
    
}
