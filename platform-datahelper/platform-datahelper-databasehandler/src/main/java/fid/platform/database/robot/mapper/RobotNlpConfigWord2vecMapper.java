package fid.platform.database.robot.mapper;


import fid.platform.core.common.pojo.robot.RobotNlpConfigWord2vec;
import fid.platform.core.common.pojo.robot.vo.RobotWord2Vec;
import org.apache.ibatis.annotations.Param;

public interface RobotNlpConfigWord2vecMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RobotNlpConfigWord2vec record);

    int insertSelective(RobotNlpConfigWord2vec record);

    RobotNlpConfigWord2vec selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RobotNlpConfigWord2vec record);

    int updateByPrimaryKey(RobotNlpConfigWord2vec record);

    //------------------自定义查询-----------------------
    RobotNlpConfigWord2vec getWord2VecConfigByMissionId(@Param("missionId") Long missionId);

    RobotWord2Vec queryPageWord2Vec(Long missionId) throws Exception;
}