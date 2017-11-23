package fid.platform.database.robot.mapper;

import fid.platform.core.common.pojo.robot.RobotNlpTags;

import java.util.List;

public interface RobotNlpTagsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RobotNlpTags record);

    int insertSelective(RobotNlpTags record);

    RobotNlpTags selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RobotNlpTags record);

    int updateByPrimaryKey(RobotNlpTags record);

    //-----------------------自定义查询----------------------------
    RobotNlpTags getParentTag(Long id);

    List<RobotNlpTags> getChildTagsByTagId(Long id);
}