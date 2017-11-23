package fid.platform.database.robot.mapper;

import fid.platform.core.common.pojo.robot.vo.RobotTrainDataDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by mengtian on 2017/11/21
 */
public interface RobotTrainDataDetailMapper {
    RobotTrainDataDetail queryRobotTrainDataDetail(@Param("missionId") Long missionId);
}
