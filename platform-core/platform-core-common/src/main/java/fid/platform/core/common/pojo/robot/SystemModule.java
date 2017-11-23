package fid.platform.core.common.pojo.robot;

/**
 * @Title: TODO (用一句话描述该文件做什么)
 * @Package fid.platform.core.common.pojo.robot
 * @Description: TODO (用一句话描述该文件做什么)
 * @Author auto generated
 * @Date 2017-11-01 16:13:45
 * @Version V1.0
 * Update Logs:
 */
public class SystemModule {

		/**
		 * 模块id
		 */
		private Integer id;
		/**
		 * 模块名称
		 */
		private String moduleName;

		private String moduleDesc;

		public Integer getId() {
				return id;
		}

		public void setId(Integer id) {
				this.id = id;
		}

		public String getModuleName() {
				return moduleName;
		}

		public void setModuleName(String moduleName) {
				this.moduleName = moduleName;
		}

	public String getModuleDesc() {
		return moduleDesc;
	}

	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}
}
