package fid.platform.database.robot.mapper;

import fid.platform.core.common.pojo.robot.RobotNlpMissionOrgConf;

import java.util.List;

public interface RobotNlpMissionOrgConfMapper {

    int deleteByPrimaryKey(Long id);

    int insert(RobotNlpMissionOrgConf record);

    RobotNlpMissionOrgConf selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RobotNlpMissionOrgConf record);

    RobotNlpMissionOrgConf selectByMissionId(Long missionId);

    List<RobotNlpMissionOrgConf> queryMissionOrgConfs(Long missionId) throws Exception;

}
