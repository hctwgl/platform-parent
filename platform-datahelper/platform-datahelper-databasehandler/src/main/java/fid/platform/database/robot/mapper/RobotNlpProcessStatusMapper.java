package fid.platform.database.robot.mapper;

import fid.platform.core.common.pojo.robot.RobotNlpProcessStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RobotNlpProcessStatusMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RobotNlpProcessStatus record);

    int insertSelective(RobotNlpProcessStatus record);

    RobotNlpProcessStatus selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RobotNlpProcessStatus record);

    int updateByPrimaryKey(RobotNlpProcessStatus record);

    //-----------自定义---------
    RobotNlpProcessStatus getNewestStatusByTypeAndMissionId(@Param("missionId") Long missionId, @Param("processType") int processType);

    List<RobotNlpProcessStatus> getAllProcessByMissionId(@Param("missionId")Long missionId);

}