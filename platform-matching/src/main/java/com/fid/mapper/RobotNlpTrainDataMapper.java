package com.fid.mapper;

import java.util.List;
import java.util.Map;

import com.fid.domain.RobotMatchKeywords;
import com.fid.domain.RobotNlpTrainData;

public interface RobotNlpTrainDataMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RobotNlpTrainData record);

    int insertSelective(RobotNlpTrainData record);

    RobotNlpTrainData selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RobotNlpTrainData record);

    int updateByPrimaryKeyWithBLOBs(RobotNlpTrainData record);

    int updateByPrimaryKey(RobotNlpTrainData record);
    
    public List<RobotNlpTrainData> queryListByParam(Map<String, Object> param);
    
    public int addBatch(List<RobotNlpTrainData> list);
}