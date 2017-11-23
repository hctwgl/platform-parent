package fid.platform.database.robot.mapper;

import fid.platform.core.common.pojo.robot.RobotNlpMission;

import java.util.List;

public interface RobotNlpMissionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RobotNlpMission record);

    int insertSelective(RobotNlpMission record);

    RobotNlpMission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RobotNlpMission record);

    int updateByPrimaryKey(RobotNlpMission record);

    List<RobotNlpMission> queryRobotNlpMission(RobotNlpMission robotNlpMission) throws Exception;

    Integer queryRobotNlpMissionCount(RobotNlpMission robotNlpMission) throws Exception;
}