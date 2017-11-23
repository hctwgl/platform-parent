package com.fid.mapper;

import java.util.List;
import java.util.Map;

import com.fid.domain.RobotMatchKeywords;
import com.fid.domain.RobotNlpTags;

public interface RobotNlpTagsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RobotNlpTags record);

    int insertSelective(RobotNlpTags record);

    RobotNlpTags selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RobotNlpTags record);

    int updateByPrimaryKey(RobotNlpTags record);
    
    public List<RobotNlpTags> queryListByParam(Map<String, Object> param);
}