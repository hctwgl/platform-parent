package fid.platform.database.robot.mapper;


import fid.platform.core.common.pojo.robot.SystemModuleStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Title: TODO (用一句话描述该文件做什么)
 * @Package fid.platform.database.robot.mapper
 * @Description: TODO (用一句话描述该文件做什么)
 * @Author auto generated
 * @Date 2017-11-17 15:25:57
 */
public interface SystemModuleStatusMapper {
    void insertSystemModuleStatus(SystemModuleStatus systemModuleStatus) throws Exception;

    void updateSystemModuleStatus(SystemModuleStatus systemModuleStatus) throws Exception;

    void deleteSystemModuleStatus(String id) throws Exception;

    List<SystemModuleStatus> querySystemModuleStatus(SystemModuleStatus systemModuleStatus) throws Exception;

    Integer querySystemModuleStatusCount(SystemModuleStatus systemModuleStatus) throws Exception;

    SystemModuleStatus findSystemModuleStatus(String id) throws Exception;

    List<SystemModuleStatus> querySystemModuleStatusLikeHost(@Param("host") String host,
                                                             @Param("moduleType") Integer moduleType) throws Exception;
}