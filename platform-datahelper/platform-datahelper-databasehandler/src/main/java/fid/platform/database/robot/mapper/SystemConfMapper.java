package fid.platform.database.robot.mapper;

import fid.platform.core.common.pojo.robot.SystemConf;

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
public interface SystemConfMapper {

		void insertSystemConf(SystemConf systemConf) throws Exception;

		void updateSystemConf(SystemConf systemConf) throws Exception;

		void deleteSystemConf(String id) throws Exception;

		List<SystemConf> querySystemConf(SystemConf systemConf) throws Exception;

		Integer querySystemConfCount(SystemConf systemConf) throws Exception;

		SystemConf findSystemConf(String id) throws Exception;
}