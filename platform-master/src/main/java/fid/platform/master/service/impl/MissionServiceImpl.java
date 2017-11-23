package fid.platform.master.service.impl;

import fid.platform.core.common.commonutil.YamlConfigUtil;
import fid.platform.core.common.constant.Constant;
import fid.platform.core.common.constant.ProcessTypeConstants;
import fid.platform.core.common.pojo.robot.*;
import fid.platform.core.common.pojo.robot.vo.RobotCrawlerDetail;
import fid.platform.core.common.pojo.robot.vo.RobotNlpUnion;
import fid.platform.core.common.pojo.robot.vo.RobotTrainDataDetail;
import fid.platform.core.common.pojo.robot.vo.RobotWord2Vec;
import fid.platform.database.robot.mapper.*;
import fid.platform.master.service.MissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mengtian on 2017/11/14
 */
@Service
public class MissionServiceImpl implements MissionService {

    private final Logger logger = LoggerFactory.getLogger(MissionServiceImpl.class);

    @Resource
    private RobotNlpMissionMapper robotNlpMissionMapper;

    @Resource
    private RobotCrawlerDetailMapper robotCrawlerDetailMapper;

    @Resource
    private RobotNlpConfigWord2vecMapper robotNlpConfigWord2vecMapper;

    @Resource
    private RobotNlpUnionMapper robotNlpUnionMapper;

    @Resource
    private SystemModuleStatusMapper systemModuleStatusMapper;

    @Resource
    private RobotNlpProcessStatusMapper robotNlpProcessStatusMapper;

    @Resource
    private RobotTrainDataDetailMapper robotTrainDataDetailMapper;

    @Resource
    private RobotNlpConfigNetMapper robotNlpConfigNetMapper;

    @Override
    public List<RobotNlpMission> getMissions(RobotNlpMission mission) throws Exception {
        try {
            return robotNlpMissionMapper.queryRobotNlpMission(mission);
        } catch (Exception e) {
            logger.error("查询任务列表失败", e);
            throw new Exception("查询任务列表失败", e);
        }
    }

    @Override
    public void addMissions(RobotNlpMission mission) throws Exception {
        try {
            //todo 这里需不需要主动设置任务id
            mission.setStatus((short) 0);
            robotNlpMissionMapper.insert(mission);
        } catch (Exception e) {
            logger.error("新增任务失败", e);
            throw new Exception("新增任务失败", e);
        }
    }

    @Override
    public Map<String, Object> addWord2VecConf(RobotNlpConfigWord2vec word2vec) throws Exception {
        Map<String, Object> res = new HashMap<>();
        //查询是否已存在
        RobotWord2Vec robotWord2Vec = robotNlpConfigWord2vecMapper.queryPageWord2Vec(word2vec.getMissionid());
        if (robotWord2Vec != null) {
            res.put("success", false);
            res.put("msg", "word2vec模型已配置，请勿重复添加。");
        } else {
            //word2vec
            //对应文件路径
            String missionFile = "mission_" + word2vec.getMissionid();
            String word2vecPath = YamlConfigUtil.getInstance().getConfig("word2vec.path");

            String outputPath = word2vecPath.concat(missionFile).concat(File.separator)
                    .concat(YamlConfigUtil.getInstance().getConfig("word2vec.outputPathName"))
                    .concat("mission").concat(word2vec.getMissionid() + "_w2v.bin");

            String dataPath = word2vecPath.concat(missionFile).concat(File.separator)
                    .concat(YamlConfigUtil.getInstance().getConfig("word2vec.datapathName"))
                    .concat("mission").concat(word2vec.getMissionid() + ".bin");

            String jarFilePath = YamlConfigUtil.getInstance().getConfig("word2vec.trainJar");

            String logFilePath = word2vecPath.concat(missionFile).concat(File.separator)
                    .concat(YamlConfigUtil.getInstance().getConfig("word2vec.logFile"))
                    .concat("mission").concat(word2vec.getMissionid() + ".log");

            word2vec.setOutputpath(outputPath);
            word2vec.setDatapath(dataPath);
            word2vec.setJarfile(jarFilePath);
            word2vec.setLogfile(logFilePath);

            robotNlpConfigWord2vecMapper.insert(word2vec);

            //插入状态
            insertProcessStatus(word2vec.getMissionid(), ProcessTypeConstants.Word2vec_Generation);

            res.put("success", true);
            res.put("msg", "添加成功");

        }
        return res;
    }

    @Override
    public List<RobotNlpUnion> getRobotNlpUnion(RobotNlpUnion nlpUnion) throws Exception {
        try {
            return robotNlpUnionMapper.queryUnion(nlpUnion);
        } catch (Exception e) {
            logger.error("查询爬虫信息失败", e);
            throw new Exception("查询爬虫信息失败", e);
        }
    }

    @Override
    public Map<String, Object> generateTrainData(Long missionId, Integer balanceNum, Integer dropNum) throws Exception {
        Map<String, Object> result = new HashMap<>();
        SystemModuleStatus moduleStatus = new SystemModuleStatus();
        moduleStatus.setModuleId(Constant.MODULE_TRAINDATA);
        moduleStatus.setModuleStatus(Constant.SYSTEM_MODULE_NOT_IN_USE);
        List<SystemModuleStatus> moduleStatuses = systemModuleStatusMapper.querySystemModuleStatus(moduleStatus);
        if (moduleStatuses != null && moduleStatuses.size() > 0) {
            moduleStatus = moduleStatuses.get(0);

            String url = moduleStatus.getMachineHost().concat(YamlConfigUtil.getInstance().getConfig("request.labelSentence"));
            url = url + "?missionId=" + missionId + "&balanceNum=" + balanceNum + "&dropNum=" + dropNum;

            insertProcessStatus(missionId, ProcessTypeConstants.LabeledSentence_Generation);

            //修改主流程状态为数据处理
            RobotNlpMission mission = robotNlpMissionMapper.selectByPrimaryKey(missionId);
            mission.setStatus((short) Constant.MISSION_DATA_GENERATOR);
            robotNlpMissionMapper.updateByPrimaryKey(mission);

            result.put("success", true);
            result.put("msg", url);
        } else {
            result.put("msg", "当前暂无空闲服务，请排队等待任务完成。");
            result.put("success", false);
        }
        return result;
    }

    @Override
    public Map<String, Object> trainWord2vec(Long missionId) throws Exception {
        Map<String, Object> result = new HashMap<>();
        SystemModuleStatus moduleStatus = new SystemModuleStatus();
        moduleStatus.setModuleId(Constant.MODULE_WORD2VEC);
        moduleStatus.setModuleStatus(Constant.SYSTEM_MODULE_NOT_IN_USE);
        List<SystemModuleStatus> moduleStatuses = systemModuleStatusMapper.querySystemModuleStatus(moduleStatus);
        if (moduleStatuses != null && moduleStatuses.size() > 0) {
            moduleStatus = moduleStatuses.get(0);
            String url = moduleStatus.getMachineHost().concat(YamlConfigUtil.getInstance().getConfig("request.word2vec"));
            url = url + "?missionId=" + missionId;

            // 修改主流程状态为词典训练
            RobotNlpMission mission = robotNlpMissionMapper.selectByPrimaryKey(missionId);
            mission.setStatus((short) Constant.MISSION_TRAIN_WORD2VEC);
            robotNlpMissionMapper.updateByPrimaryKey(mission);

            result.put("success", true);
            result.put("msg", url);
        } else {
            result.put("msg", "当前暂无空闲服务，请排队等待任务完成。");
            result.put("success", false);
        }
        return result;
    }

    @Override
    public RobotNlpUnion getMission(Long missionId) throws Exception {
        try {
            return robotNlpUnionMapper.queryUnionById(missionId);
        } catch (Exception e) {
            logger.error("查询任务详情出错", e);
            throw new Exception("查询任务详情出错", e);
        }
    }

    @Override
    public RobotCrawlerDetail getCrawlerDetail(Long missionId) throws Exception {
        try {
            return robotCrawlerDetailMapper.queryCrawlerDetail(missionId);
        } catch (Exception e) {
            logger.error("查询爬虫详情出错", e);
            throw new Exception("查询爬虫详情出错", e);
        }
    }

    @Override
    public RobotTrainDataDetail getTrainData(Long missionId) throws Exception {
        return robotTrainDataDetailMapper.queryRobotTrainDataDetail(missionId);
    }

    @Override
    public RobotWord2Vec getWord2Vec(Long missionId) throws Exception {
        try {
            return robotNlpConfigWord2vecMapper.queryPageWord2Vec(missionId);
        } catch (Exception e) {
            logger.error("查询word2vec配置出错", e);
            throw new Exception("查询word2vec配置出错", e);
        }
    }

    @Override
    public Map<String, Object> addNetConf(RobotNlpConfigNet configNet) throws Exception {
        Map<String, Object> res = new HashMap<>();

        RobotNlpConfigNet temp = new RobotNlpConfigNet();
        temp.setMissionid(configNet.getMissionid());
        List<RobotNlpConfigNet> configNets = robotNlpConfigNetMapper.queryRobotNlpConfigNet(temp);
        if (configNets != null && configNets.size() > 0) {
            res.put("success", false);
            res.put("msg", "请勿重复添加网络配置。");
        } else {
            robotNlpConfigNetMapper.insert(configNet);
            res.put("success", true);
            res.put("msg", "添加网络配置成功");
        }
        return res;
    }

    private void insertProcessStatus(Long missionId, Integer type) {
        //
        RobotNlpProcessStatus processStatus = robotNlpProcessStatusMapper.getNewestStatusByTypeAndMissionId(missionId, type);
        if (processStatus != null) {
            //更新状态
            processStatus.setProcessstatus(ProcessTypeConstants.Process_Free);
            robotNlpProcessStatusMapper.updateByPrimaryKey(processStatus);
        } else {
            //插入状态
            processStatus = new RobotNlpProcessStatus();
            processStatus.setCreatetime(new Date());
            processStatus.setMissionid(missionId);
            processStatus.setProcessstatus(ProcessTypeConstants.Process_Free);
            processStatus.setProcesstype(type);
            robotNlpProcessStatusMapper.insert(processStatus);
        }

    }


}
