package fid.platform.database.robot.mapper;

import fid.platform.core.common.pojo.robot.vo.RobotNlpUnion;

import java.util.List;

/**
 * Created by mengtian on 2017/11/15
 */
public interface RobotNlpUnionMapper {
    List<RobotNlpUnion> queryUnion(RobotNlpUnion union) throws Exception;

    RobotNlpUnion queryUnionById(Long missionId) throws Exception;
}
