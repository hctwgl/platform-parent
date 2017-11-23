package fid.platform.modelhelper.service.impl;

import fid.platform.core.common.pojo.robot.RobotNlpMission;
import fid.platform.database.robot.mapper.RobotNlpMissionMapper;
import fid.platform.modelhelper.service.MissionStatuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MissionStatuServiceImpl implements MissionStatuService {

		@Resource
		private RobotNlpMissionMapper robotNlpMissionMapper;

		@Override
		public RobotNlpMission getMissionStatus(Long missionId) {
				return robotNlpMissionMapper.selectByPrimaryKey(missionId);
		}

}
