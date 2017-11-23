package fid.platform.modelhelper.service;

public interface NeuralNetService {

		/**
		 * 训练最简参数神经网络
		 * @param missionId
		 * @return 网络检测地址
		 */
		String trainSimpleCnnTextModel(Long missionId);

}
