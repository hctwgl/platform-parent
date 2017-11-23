package fid.platform.database.robot.mapper;

import fid.platform.core.common.pojo.robot.SystemModule;

import java.util.List;

/**
 * @Title: TODO (用一句话描述该文件做什么)
 * @Package fid.platform.database.robot.mapper
 * @Description: TODO (用一句话描述该文件做什么)
 * @Author auto generated
 * @Date 2017-11-01 16:13:45
 * @Version V1.0
 * Update Logs:
 */
public interface SystemModuleMapper {

    void insertSystemModule(SystemModule systemModule) throws Exception;

    void updateSystemModule(SystemModule systemModule) throws Exception;

    void deleteSystemModule(String id) throws Exception;

    List<SystemModule> querySystemModule(SystemModule systemModule) throws Exception;

    Integer querySystemModuleCount(SystemModule systemModule) throws Exception;

    SystemModule findSystemModule(String id) throws Exception;
}