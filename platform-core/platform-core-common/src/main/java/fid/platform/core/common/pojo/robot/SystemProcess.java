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
public class SystemProcess {

		/**
		 *
		 */
		private Integer id;
		/**
		 * 运行中的进程id
		 */
		private Integer processId;
		/**
		 * 进程名称
		 */
		private String processName;
		/**
		 * 进程别名（展示）
		 */
		private String processNick;
		/**
		 * 进程对应系统路径
		 */
		private String processPath;
		/**
		 * 进程状态 0、运行中，1、终止
		 */
		private Integer processAlive;

		public Integer getId() {
				return id;
		}

		public void setId(Integer id) {
				this.id = id;
		}

		public Integer getProcessId() {
				return processId;
		}

		public void setProcessId(Integer processId) {
				this.processId = processId;
		}

		public String getProcessName() {
				return processName;
		}

		public void setProcessName(String processName) {
				this.processName = processName;
		}

		public String getProcessNick() {
				return processNick;
		}

		public void setProcessNick(String processNick) {
				this.processNick = processNick;
		}

		public String getProcessPath() {
				return processPath;
		}

		public void setProcessPath(String processPath) {
				this.processPath = processPath;
		}

		public Integer getProcessAlive() {
				return processAlive;
		}

		public void setProcessAlive(Integer processAlive) {
				this.processAlive = processAlive;
		}

		@Override
		public String toString() {
				return "SystemProcess{" +
								"id='" + id + '\'' +
								"processId='" + processId + '\'' +
								"processName='" + processName + '\'' +
								"processNick='" + processNick + '\'' +
								"processPath='" + processPath + '\'' +
								"processAlive='" + processAlive + '\'' +
								'}';
		}
}
