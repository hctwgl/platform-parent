package fid.platform.master.service;

import fid.platform.core.common.pojo.robot.RobotNlpConfigNet;
import fid.platform.core.common.pojo.robot.RobotNlpConfigWord2vec;
import fid.platform.core.common.pojo.robot.RobotNlpMission;
import fid.platform.core.common.pojo.robot.vo.RobotCrawlerDetail;
import fid.platform.core.common.pojo.robot.vo.RobotNlpUnion;
import fid.platform.core.common.pojo.robot.vo.RobotTrainDataDetail;
import fid.platform.core.common.pojo.robot.vo.RobotWord2Vec;

import java.util.List;
import java.util.Map;

/**
 * Created by mengtian on 2017/11/14
 */
public interface MissionService {
    /**
     * 查询任务
     *
     * @param mission
     * @return
     * @throws Exception
     */
    List<RobotNlpMission> getMissions(RobotNlpMission mission) throws Exception;

    /**
     * 添加训练任务
     *
     * @param mission
     * @throws Exception
     */
    void addMissions(RobotNlpMission mission) throws Exception;

    /**
     * 添加任务word2vec配置
     *
     * @param word2vec
     * @throws Exception
     */
    Map<String, Object> addWord2VecConf(RobotNlpConfigWord2vec word2vec) throws Exception;

    /**
     * 多条件查询任务
     *
     * @param nlpUnion
     * @return
     * @throws Exception
     */
    List<RobotNlpUnion> getRobotNlpUnion(RobotNlpUnion nlpUnion) throws Exception;

    /**
     * 生成训练集
     *
     * @param missionId
     * @throws Exception
     */
    Map<String, Object> generateTrainData(Long missionId, Integer balanceNum, Integer dropNum) throws Exception;

    Map<String, Object> trainWord2vec(Long missionId) throws Exception;

    RobotNlpUnion getMission(Long missionId) throws Exception;

    RobotCrawlerDetail getCrawlerDetail(Long missionId) throws Exception;

    RobotTrainDataDetail getTrainData(Long missionId) throws Exception;

    RobotWord2Vec getWord2Vec(Long missionId) throws Exception;

    Map<String, Object> addNetConf(RobotNlpConfigNet configNet) throws Exception;
}
