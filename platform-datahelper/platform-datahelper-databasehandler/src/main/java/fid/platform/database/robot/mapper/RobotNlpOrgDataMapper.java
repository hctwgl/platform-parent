package fid.platform.database.robot.mapper;

import fid.platform.core.common.pojo.robot.RobotNlpOrgData;

public interface RobotNlpOrgDataMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RobotNlpOrgData record);

    int insertSelective(RobotNlpOrgData record);

    RobotNlpOrgData selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RobotNlpOrgData record);

    int updateByPrimaryKeyWithBLOBs(RobotNlpOrgData record);

    int updateByPrimaryKey(RobotNlpOrgData record);
}