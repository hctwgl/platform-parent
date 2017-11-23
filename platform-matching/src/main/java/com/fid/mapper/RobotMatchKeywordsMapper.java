package com.fid.mapper;

import java.util.List;
import java.util.Map;

import com.fid.domain.RobotMatchKeywords;

public interface RobotMatchKeywordsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RobotMatchKeywords record);

    int insertSelective(RobotMatchKeywords record);

    RobotMatchKeywords selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RobotMatchKeywords record);

    int updateByPrimaryKey(RobotMatchKeywords record);
    
    public List<RobotMatchKeywords> queryListByParam(Map<String, Object> param);
}