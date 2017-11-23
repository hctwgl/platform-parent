package fid.platform.core.common.pojo.robot;

public class RobotNlpConfigWord2vec {
    private Long id;

    private String outputpath;

    private String datapath;

    private String jarfile;

    private String logfile;

    private Integer minwordfrequency;

    private Integer iteration;

    private Integer seed;

    private Integer layersize;

    private Double learningrate;

    private Integer windowsize;

    private Long missionid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOutputpath() {
        return outputpath;
    }

    public void setOutputpath(String outputpath) {
        this.outputpath = outputpath == null ? null : outputpath.trim();
    }

    public String getDatapath() {
        return datapath;
    }

    public void setDatapath(String datapath) {
        this.datapath = datapath == null ? null : datapath.trim();
    }

    public String getJarfile() {
        return jarfile;
    }

    public void setJarfile(String jarfile) {
        this.jarfile = jarfile == null ? null : jarfile.trim();
    }

    public String getLogfile() {
        return logfile;
    }

    public void setLogfile(String logfile) {
        this.logfile = logfile == null ? null : logfile.trim();
    }

    public Integer getMinwordfrequency() {
        return minwordfrequency;
    }

    public void setMinwordfrequency(Integer minwordfrequency) {
        this.minwordfrequency = minwordfrequency;
    }

    public Integer getIteration() {
        return iteration;
    }

    public void setIteration(Integer iteration) {
        this.iteration = iteration;
    }

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public Integer getLayersize() {
        return layersize;
    }

    public void setLayersize(Integer layersize) {
        this.layersize = layersize;
    }

    public Double getLearningrate() {
        return learningrate;
    }

    public void setLearningrate(Double learningrate) {
        this.learningrate = learningrate;
    }

    public Integer getWindowsize() {
        return windowsize;
    }

    public void setWindowsize(Integer windowsize) {
        this.windowsize = windowsize;
    }

    public Long getMissionid() {
        return missionid;
    }

    public void setMissionid(Long missionid) {
        this.missionid = missionid;
    }
}