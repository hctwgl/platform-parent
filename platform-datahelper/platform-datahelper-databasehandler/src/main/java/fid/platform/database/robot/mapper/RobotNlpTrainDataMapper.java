package fid.platform.database.robot.mapper;

import fid.platform.core.common.pojo.robot.RobotNlpTrainData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RobotNlpTrainDataMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RobotNlpTrainData record);

    int insertSelective(RobotNlpTrainData record);

    RobotNlpTrainData selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RobotNlpTrainData record);

    int updateByPrimaryKeyWithBLOBs(RobotNlpTrainData record);

    int updateByPrimaryKey(RobotNlpTrainData record);

    //----------------------自定义查询---------------------------

    List<RobotNlpTrainData> getTrainDataByMissionId(@Param("missionId") Long missionId);

}