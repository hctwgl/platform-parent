package fid.platform.database.robot.mapper;

import fid.platform.core.common.pojo.robot.RobotNlpConfigNet;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RobotNlpConfigNetMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RobotNlpConfigNet record);

    int insertSelective(RobotNlpConfigNet record);

    RobotNlpConfigNet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RobotNlpConfigNet record);

    int updateByPrimaryKey(RobotNlpConfigNet record);

    List<RobotNlpConfigNet> queryRobotNlpConfigNet(RobotNlpConfigNet configNet) throws Exception;

    //-------------自定义查询--------------
    RobotNlpConfigNet getNetConfigByMissionId(@Param("missionId")Long missionId);
}