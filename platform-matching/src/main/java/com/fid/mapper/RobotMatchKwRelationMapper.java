package com.fid.mapper;

import java.util.List;
import java.util.Map;

import com.fid.domain.RobotMatchKeywords;
import com.fid.domain.RobotMatchKwRelation;

public interface RobotMatchKwRelationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RobotMatchKwRelation record);

    int insertSelective(RobotMatchKwRelation record);

    RobotMatchKwRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RobotMatchKwRelation record);

    int updateByPrimaryKey(RobotMatchKwRelation record);
    
    public List<RobotMatchKwRelation> queryListByParam(Map<String, Object> param);
}