package fid.platform.trainer.neuralnet;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import fid.platform.core.common.pojo.commons.LabeledSentence;
import fid.platform.core.deeplearningutil.ChineseDataSetIteratorUtil;
import fid.platform.core.deeplearningutil.Word2VecUtils;
import io.protostuff.CodedInput;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.earlystopping.EarlyStoppingConfiguration;
import org.deeplearning4j.earlystopping.EarlyStoppingModelSaver;
import org.deeplearning4j.earlystopping.EarlyStoppingResult;
import org.deeplearning4j.earlystopping.saver.LocalFileGraphSaver;
import org.deeplearning4j.earlystopping.scorecalc.DataSetLossCalculatorCG;
import org.deeplearning4j.earlystopping.termination.MaxEpochsTerminationCondition;
import org.deeplearning4j.earlystopping.trainer.EarlyStoppingGraphTrainer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.ConvolutionMode;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class NeuralnetTrainer {

		private static Logger logger = LoggerFactory.getLogger(NeuralnetTrainer.class);

		@Parameter(names = {"--outputDir","-od"})
		private String outputDir;

		@Parameter(names = {"--dataPath","-dp"})
		private String dataPath;

		@Parameter(names = {"--vecDicPath","-vdp"})
		private String vecDicPath;

		@Parameter(names = {"--netConfigPath","-ncf"})
		private String netConfigPath;

		@Parameter(names = {"--splitRate","-spr"})
		private double splitRate = 0.2;

		public static void main(String[] args) {
				try {
						new NeuralnetTrainer().entryPoint(args);
				}catch (Exception e){
						e.printStackTrace();
				}
		}

		public void entryPoint(String[] args) throws Exception{
				//解析参数
				JCommander jcmd = new JCommander(this);
				jcmd.parse(args);
				jcmd.usage();

				// 基础配置
				int batchSize = 64;
				int vectorSize = 300; // 词典向量的维度,这边是100
				int nEpochs = 5; // 迭代代数
				int truncateReviewsToLength =  256; // 词长大于256则抛弃

				int cnnLayerFeatureMaps = 200; // 卷积神经网络特征图标 / channels / CNN每层layer的深度
				PoolingType globalPoolingType = PoolingType.MAX;
				Random rng = new Random(1234); // 随机抽样

				//加载词典
				Word2Vec word2Vec = Word2VecUtils.restore(vecDicPath);

				//加载网络配置json
				logger.info("正在加载神经网络配置------->"+netConfigPath);
				ObjectMapper objectMapper = new ObjectMapper();
				Map<String,Object> netConfigMap = objectMapper.readValue(new File(netConfigPath), Map.class);


				NeuralNetConfiguration.Builder myJsonComGraBuilder = new NeuralNetConfiguration.Builder();



				// 设置网络配置->我们有多个卷积层，每个带宽3,4,5的滤波器
				ComputationGraphConfiguration config = new NeuralNetConfiguration.Builder()
								// 权重初始化
								.updater(Updater.ADAM)
								.convolutionMode(ConvolutionMode.Same)
								.iterations(1)
								// This is important so we can 'stack' the results later
								.regularization(true)
//                .miniBatch(true)
								.l2(0.0001)
//                .biasInit(0.1)
//                .l1(0.0001)
								.learningRate(0.001)
//                .momentum(0.9)
								.graphBuilder()
								.addInputs("input")
								.addLayer("cnn2", new ConvolutionLayer.Builder()
												.weightInit(WeightInit.RELU)
												.activation(Activation.LEAKYRELU)
												.kernelSize(2, vectorSize)
												.stride(1, vectorSize)//
//                        .dropOut(0.5)
												.nIn(1)//
												.nOut(cnnLayerFeatureMaps)
												.build(), "input")
								.addLayer("cnn3", new ConvolutionLayer.Builder()
												.weightInit(WeightInit.RELU)
												.activation(Activation.LEAKYRELU)
												.kernelSize(3, vectorSize)
												.stride(1, vectorSize)//
//                        .dropOut(0.5)
												.nIn(1)//
												.nOut(cnnLayerFeatureMaps)
												.build(), "input")
								.addLayer("cnn4", new ConvolutionLayer.Builder()
												.weightInit(WeightInit.RELU)
												.activation(Activation.LEAKYRELU)
												.kernelSize(4, vectorSize)
												.stride(1, vectorSize)//
//                        .dropOut(0.5)
												.nIn(1)//
												.nOut(cnnLayerFeatureMaps)
												.build(), "input")
								.addVertex("merge", new MergeVertex(), "cnn2", "cnn3","cnn4")// 执行深度级联
								.addLayer("globalPool", new GlobalPoolingLayer.Builder()//
												.poolingType(globalPoolingType)//
												.build(), "merge")
								.addLayer("lstm",new GravesLSTM.Builder()
												.nIn(3 * cnnLayerFeatureMaps)
												.weightInit(WeightInit.XAVIER)
												.activation(Activation.TANH)
												.nOut(300)
												.build(),"globalPool")
								.addLayer("out", new RnnOutputLayer.Builder()
												.lossFunction(LossFunctions.LossFunction.MCXENT)//
												.activation(Activation.SOFTMAX)//
												.nIn(300).nOut(2) // 3
												.build(), "lstm")
								.setOutputs("out")
								.pretrain(false)
								.backprop(true)
								.build();



				ComputationGraph net = new ComputationGraph(config);
				net.init();

				//初始化用户界面后端
				System.out.println("初始化UI监控...");

				UIServer uiServer = UIServer.getInstance();

				//设置网络信息（随时间变化的梯度、分值等）的存储位置。这里将其存储于内存。
				StatsStorage statsStorage = new InMemoryStatsStorage();         //或者： new FileStatsStorage(File)，用于后续的保存和载入

				//将StatsStorage实例连接至用户界面，让StatsStorage的内容能够被可视化
				uiServer.attach(statsStorage);

				//然后添加StatsListener来在网络定型时收集这些信息
				net.setListeners(new StatsListener(statsStorage));

				System.out.println("Number of parameters by layer:");
				for (Layer l : net.getLayers()) {
						System.out.println("\t" + l.conf().getLayer().getLayerName() + "\t"
										+ l.numParams());
				}

				// 加载向量字典并获取训练集合测试集的DataSetIterators
				System.out.println("Loading word vectors and creating DataSetIterators");

				String sentencePath = dataPath;
				BufferedInputStream inputStream = new BufferedInputStream(
								new FileInputStream(
												new File(sentencePath)));
				LabeledSentence labeledSentence = new LabeledSentence.Builder().buildEmptyOne();
				Schema<LabeledSentence> schema = RuntimeSchema.getSchema(LabeledSentence.class);
				CodedInput codedInput = CodedInput.newInstance(inputStream);
				codedInput.setSizeLimit(536870912);
				schema.mergeFrom(codedInput,labeledSentence);
				//拆分训练集和测试集
				LabeledSentence labeledSentenceTrain = labeledSentence.splitByRate(splitRate, true);
				LabeledSentence labeledSentenceTest = labeledSentence.splitByRate(splitRate, false);
				DataSetIterator trainIter = ChineseDataSetIteratorUtil.getSimpleDataSetIterator(labeledSentenceTrain, word2Vec,
								batchSize, truncateReviewsToLength, new Random(1234));
				DataSetIterator testIter = ChineseDataSetIteratorUtil.getSimpleDataSetIterator(labeledSentenceTest, word2Vec,
								batchSize, truncateReviewsToLength, new Random(1234));

				//数据准备及搭建完毕-----------------------------------------------------------
				//定义早停神经网络模型保存器
				EarlyStoppingModelSaver<ComputationGraph> saver = new LocalFileGraphSaver(outputDir);
				//神经网络早停设置
				EarlyStoppingConfiguration<ComputationGraph> esConf = new EarlyStoppingConfiguration.Builder<ComputationGraph>()
								.epochTerminationConditions(new MaxEpochsTerminationCondition(nEpochs))//
								.evaluateEveryNEpochs(1)//
								.scoreCalculator(new DataSetLossCalculatorCG(testIter, true))//
								.modelSaver(saver)//
								.build();

				//定义早停神经网络训练器
				EarlyStoppingGraphTrainer trainer = new EarlyStoppingGraphTrainer(esConf, net, trainIter);

				//用早停训练神经网络
				EarlyStoppingResult result = trainer.fit();
				System.out.println("Termination reason: " + result.getTerminationReason());
				System.out.println("Termination details: " + result.getTerminationDetails());
				System.out.println("Total epochs: " + result.getTotalEpochs());
				System.out.println("Best epoch number: " + result.getBestModelEpoch());
				System.out.println("Score at best epoch: " + result.getBestModelScore());

				//打印每一代的评分
				Map<Integer, Double> scoreVsEpoch = result.getScoreVsEpoch();
				List<Integer> list = new ArrayList<>(scoreVsEpoch.keySet());
				Collections.sort(list);
				System.out.println("Score vs. Epoch:");
				for (Integer i : list) {
						System.out.println(i + "\t" + scoreVsEpoch.get(i));
				}

		}
}
