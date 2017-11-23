package fid.platform.master.service.impl;

import fid.platform.core.common.commonutil.YamlConfigUtil;
import fid.platform.core.common.constant.Constant;
import fid.platform.core.common.pojo.robot.SystemModule;
import fid.platform.core.common.pojo.robot.SystemModuleStatus;
import fid.platform.database.robot.mapper.SystemModuleMapper;
import fid.platform.database.robot.mapper.SystemModuleStatusMapper;
import fid.platform.master.service.SystemModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mengtian on 2017/11/22
 */
@Service
public class SystemModuleServiceImpl implements SystemModuleService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private SystemModuleMapper systemModuleMapper;

    @Resource
    private SystemModuleStatusMapper systemModuleStatusMapper;

    @Override
    public List<SystemModule> getModules(String moduleName) throws Exception {
        SystemModule systemModule = new SystemModule();
        systemModule.setModuleName(moduleName);
        List<SystemModule> modules = systemModuleMapper.querySystemModule(systemModule);
        return modules;
    }

    @Override
    public List<SystemModuleStatus> moduleStatus(SystemModuleStatus moduleStatus) throws Exception {
        return systemModuleStatusMapper.querySystemModuleStatus(moduleStatus);
    }

    @Override
    public Map<String, Object> addModuleStatus(SystemModuleStatus moduleStatus) throws Exception {
        Map<String, Object> result = new HashMap<>();
        SystemModuleStatus temp = new SystemModuleStatus();
        temp.setMachineHost(moduleStatus.getMachineHost());
        List<SystemModuleStatus> moduleStatuses = systemModuleStatusMapper.querySystemModuleStatus(temp);
        if (moduleStatuses != null && moduleStatuses.size() > 0) {
            result.put("success", false);
            result.put("msg", "请求重复添加host");
        } else {
            moduleStatus.setModuleStatus(0);
            systemModuleStatusMapper.insertSystemModuleStatus(moduleStatus);
            result.put("success", true);
        }
        return result;
    }

    @Override
    public List<Integer> registerModule(String host, Integer moduleType) throws Exception {
        //moduleId和moduleType代表同一个意思
        List<Integer> moduleIds = new ArrayList<>();
        host = host + ":" + YamlConfigUtil.getInstance().getConfig("service.port");
        List<SystemModuleStatus> moduleStatuses = systemModuleStatusMapper.querySystemModuleStatusLikeHost(host, moduleType);
        if (moduleType == Constant.MODULE_ALL_TRAIN_MODULE) {
            if (moduleStatuses != null && moduleStatuses.size() > 0) {
                //对应host在系统中已经注册的模块
                List<Integer> dbModuleType = new ArrayList<>();
                for (SystemModuleStatus moduleStatus : moduleStatuses) {
                    moduleStatus.setModuleStatus(Constant.SYSTEM_MODULE_NOT_IN_USE);
                    systemModuleStatusMapper.updateSystemModuleStatus(moduleStatus);
                    dbModuleType.add(moduleStatus.getModuleId());

                    moduleIds.add(moduleStatus.getId());
                }
                //匹配是否所有模块都已经注册，可能存在添加新模块的情况
                if (dbModuleType.size() != Constant.MODULES.size()) {
                    for (Integer module : Constant.MODULES) {
                        if (!dbModuleType.contains(module)) {
                            moduleIds.add(addModule(host, module));
                        }
                    }
                }

            } else {
                for (Integer module : Constant.MODULES) {
                    moduleIds.add(addModule(host, module));
                }
            }
        } else {
            if (moduleStatuses != null && moduleStatuses.size() > 0) {
                SystemModuleStatus systemModuleStatus = moduleStatuses.get(0);
                systemModuleStatus.setModuleStatus(Constant.SYSTEM_MODULE_NOT_IN_USE);
                systemModuleStatusMapper.updateSystemModuleStatus(systemModuleStatus);
                moduleIds.add(systemModuleStatus.getId());
            } else {
                moduleIds.add(addModule(host, moduleType));
            }
        }
        return moduleIds;
    }

    private Integer addModule(String host, Integer moduleType) throws Exception {
        SystemModuleStatus systemModuleStatus = new SystemModuleStatus();
        systemModuleStatus.setModuleId(moduleType);
        systemModuleStatus.setMachineHost(host);
        systemModuleStatus.setModuleStatus(Constant.SYSTEM_MODULE_NOT_IN_USE);
        systemModuleStatusMapper.insertSystemModuleStatus(systemModuleStatus);
        return systemModuleStatus.getId();
    }


    @Override
    public void logoutModule(Integer[] serviceIds) throws Exception {
        for (Integer serviceId : serviceIds) {
            SystemModuleStatus systemModuleStatus = systemModuleStatusMapper.findSystemModuleStatus(String.valueOf(serviceId));
            systemModuleStatus.setModuleStatus(Constant.SYSTEM_MODULE_OUT_SERVICE);
            systemModuleStatusMapper.updateSystemModuleStatus(systemModuleStatus);
        }
    }
}
