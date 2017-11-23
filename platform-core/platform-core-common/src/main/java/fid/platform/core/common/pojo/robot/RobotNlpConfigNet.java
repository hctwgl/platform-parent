package fid.platform.core.common.pojo.robot;

public class RobotNlpConfigNet {
    private Long id;

    private String jarfile;

    private String logfile;

    private String outputdir;

    private String datapath;

    private String splitrate;

    private String vecdicpath;

    private String netconfigjson;

    private Long missionid;

    private Double learningrate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOutputdir() {
        return outputdir;
    }

    public void setOutputdir(String outputdir) {
        this.outputdir = outputdir == null ? null : outputdir.trim();
    }

    public String getDatapath() {
        return datapath;
    }

    public void setDatapath(String datapath) {
        this.datapath = datapath == null ? null : datapath.trim();
    }

    public String getSplitrate() {
        return splitrate;
    }

    public void setSplitrate(String splitrate) {
        this.splitrate = splitrate == null ? null : splitrate.trim();
    }

    public String getVecdicpath() {
        return vecdicpath;
    }

    public void setVecdicpath(String vecdicpath) {
        this.vecdicpath = vecdicpath == null ? null : vecdicpath.trim();
    }

    public String getNetconfigjson() {
        return netconfigjson;
    }

    public void setNetconfigjson(String netconfigjson) {
        this.netconfigjson = netconfigjson == null ? null : netconfigjson.trim();
    }

    public Long getMissionid() {
        return missionid;
    }

    public void setMissionid(Long missionid) {
        this.missionid = missionid;
    }

    public Double getLearningrate() {
        return learningrate;
    }

    public void setLearningrate(Double learningrate) {
        this.learningrate = learningrate;
    }
}