package com.fid.mapper;

import java.util.List;
import java.util.Map;

import com.fid.domain.RobotMatchKeywords;
import com.fid.domain.RobotMatchWords;

public interface RobotMatchWordsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RobotMatchWords record);

    int insertSelective(RobotMatchWords record);

    RobotMatchWords selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RobotMatchWords record);

    int updateByPrimaryKey(RobotMatchWords record);
    
    public List<RobotMatchWords> queryListByParam(Map<String, Object> param);
    
    public int insertBatch(List<RobotMatchWords> list);
}