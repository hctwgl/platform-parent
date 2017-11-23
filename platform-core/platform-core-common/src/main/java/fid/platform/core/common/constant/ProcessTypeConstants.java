package fid.platform.core.common.constant;

/**
 * 模型训练进程处理分类处理常量
 */
public class ProcessTypeConstants {

		//进行的任务类型
		public static final int LabeledSentence_Generation = 1;
		public static final int Word2vec_Generation = 2;
		public static final int NeuralNet_Generation = 3;

		//状态
		public static final int Process_Free = 0;
		public static final int Process_Running = 1;
		public static final int Process_Finished = 2;
		public static final int Process_Error = 3;

}
