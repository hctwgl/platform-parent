package fid.platform.core.common.pojo.robot.vo;

/**
 * Created by mengtian on 2017/11/17
 */
public class RobotWord2Vec {
    private Long id;
    private Long missionId;
    private Long minWordFrequency;
    private Long iteration;
    private Long seed;
    private Long layerSize;
    private Double learningRate;
    private Long windowSize;

    private Integer processStatus;

    public static final int FREE = 0;
    public static final int PROCESSING = 1;
    public static final int FINISH = 2;
    public static final int ERROR = 3;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public Long getMinWordFrequency() {
        return minWordFrequency;
    }

    public void setMinWordFrequency(Long minWordFrequency) {
        this.minWordFrequency = minWordFrequency;
    }

    public Long getIteration() {
        return iteration;
    }

    public void setIteration(Long iteration) {
        this.iteration = iteration;
    }

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }

    public Long getLayerSize() {
        return layerSize;
    }

    public void setLayerSize(Long layerSize) {
        this.layerSize = layerSize;
    }

    public Double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(Double learningRate) {
        this.learningRate = learningRate;
    }

    public Long getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(Long windowSize) {
        this.windowSize = windowSize;
    }

    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }
}
