package fid.platform.master.service;

import fid.platform.core.common.pojo.robot.SystemModule;
import fid.platform.core.common.pojo.robot.SystemModuleStatus;

import java.util.List;
import java.util.Map;

/**
 * Created by mengtian on 2017/11/22
 */
public interface SystemModuleService {
    List<SystemModule> getModules(String moduleName) throws Exception;

    List<SystemModuleStatus> moduleStatus(SystemModuleStatus moduleStatus) throws Exception;

    Map<String, Object> addModuleStatus(SystemModuleStatus moduleStatus) throws Exception;

    List<Integer> registerModule(String host, Integer moduleType) throws Exception;

    void logoutModule(Integer[] serviceIds) throws Exception;
}
