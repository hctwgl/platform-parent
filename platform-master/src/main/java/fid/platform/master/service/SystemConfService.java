package fid.platform.master.service;

import fid.platform.core.common.pojo.robot.SystemConf;
import fid.platform.core.common.pojo.robot.SystemModule;
import fid.platform.core.common.pojo.robot.SystemModuleStatus;

import java.util.List;
import java.util.Map;

/**
 * Created by mengtian on 2017/11/2
 */
public interface SystemConfService {
    List<SystemConf> getAllConf() throws Exception;

    void editConf(SystemConf systemConf) throws Exception;

    void addConf(SystemConf systemConf) throws Exception;

    SystemConf getConf(Integer module, String confKey) throws Exception;

}
