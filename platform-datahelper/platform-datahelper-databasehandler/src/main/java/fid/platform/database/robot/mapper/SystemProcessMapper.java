package fid.platform.database.robot.mapper;

import fid.platform.core.common.pojo.robot.SystemProcess;

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
public interface SystemProcessMapper {

		void insertSystemProcess(SystemProcess systemProcess) throws Exception;

		void updateSystemProcess(SystemProcess systemProcess) throws Exception;

		void deleteSystemProcess(String id) throws Exception;

		List<SystemProcess> querySystemProcess(SystemProcess systemProcess) throws Exception;

		Integer querySystemProcessCount(SystemProcess systemProcess) throws Exception;

		SystemProcess findSystemProcess(String id) throws Exception;
}