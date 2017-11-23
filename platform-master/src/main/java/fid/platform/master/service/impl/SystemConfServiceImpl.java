package fid.platform.master.service.impl;

import fid.platform.core.common.pojo.robot.SystemConf;
import fid.platform.core.common.pojo.robot.SystemModule;
import fid.platform.core.common.pojo.robot.SystemModuleStatus;
import fid.platform.database.robot.mapper.SystemConfMapper;
import fid.platform.database.robot.mapper.SystemModuleMapper;
import fid.platform.database.robot.mapper.SystemModuleStatusMapper;
import fid.platform.master.service.SystemConfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mengtian on 2017/11/2
 */
@Service
public class SystemConfServiceImpl implements SystemConfService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private SystemConfMapper systemConfMapper;

    @Override
    public List<SystemConf> getAllConf() throws Exception {
        SystemConf systemConf = new SystemConf();
        return systemConfMapper.querySystemConf(systemConf);
    }


    @Override
    public void editConf(SystemConf systemConf) throws Exception {
        systemConfMapper.updateSystemConf(systemConf);
    }

    @Override
    public void addConf(SystemConf systemConf) throws Exception {
        systemConfMapper.insertSystemConf(systemConf);
    }

    @Override
    public SystemConf getConf(Integer module, String confKey) throws Exception {
        SystemConf systemConf = new SystemConf();
        systemConf.setModuleId(module);
        systemConf.setConfKey(confKey);
        List<SystemConf> systemConfs = systemConfMapper.querySystemConf(systemConf);
        return systemConfs == null ? null : systemConfs.get(0);
    }
}
