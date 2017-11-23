package fid.platform.trainer.word2vec;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fid.platform.core.common.constant.ProcessTypeConstants;
import fid.platform.core.common.constant.UrlConstants;
import fid.platform.core.common.pojo.commons.LabeledSentence;
import fid.platform.core.deeplearningutil.Word2VecUtils;
import io.protostuff.CodedInput;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Word2vecTrainer {

		private static final Logger logger = LoggerFactory.getLogger(Word2vecTrainer.class);

		@Parameter(names = {"--outputPath", "-op"})
		private String outputPath;
		@Parameter(names = {"--dataPath", "-dp"})
		private String dataPath;
		@Parameter(names = {"--missionId", "-mid"})
		private Long missionId;

		@Parameter(names = {"--minWordFreq", "-mwf"})
		private int minWordFrequency = 5;
		@Parameter(names = {"--iteration", "-iter"})
		private int iteration = 1;
		@Parameter(names = {"-seed"})
		private int seed = 42;
		@Parameter(names = {"--layerSize", "-ls"})
		private int layerSize = 100;
		@Parameter(names = {"--learningRate", "-lr"})
		private double learningRate = 0.01;
		@Parameter(names = {"--windowSize", "-ws"})
		private int windowSize = 5;

		public static void main(String[] args) throws Exception {
				logger.info("收集训练信息中....");
				new Word2vecTrainer().entryPoint(args);
		}

		public void entryPoint(String[] args) throws IOException {
				try {
						JCommander jcmd = new JCommander(this);
						jcmd.parse(args);
						jcmd.usage();

						logger.info("outputPath:" + outputPath);
						logger.info("dataPath:" + dataPath);
						logger.info("missionId:" + missionId);
						logger.info("minWordFrequency:" + minWordFrequency);
						logger.info("seed:" + seed);
						logger.info("layerSize:" + layerSize);
						logger.info("learningRate:" + learningRate);
						logger.info("windowSize:" + windowSize);
						//-----------data------------
						logger.info("收集训练信息完毕....");
						logger.info("创建文件路径....");
						File f = new File(outputPath);
						File parentFile = f.getParentFile();
						if (!parentFile.exists()) {
								parentFile.mkdirs();
						}
						logger.info("正在准备数据...........");
						Schema<LabeledSentence> schema = RuntimeSchema.getSchema(LabeledSentence.class);

						BufferedInputStream filterInputStream = new BufferedInputStream(new FileInputStream(new File(dataPath)));
						LabeledSentence vecDicLabeledSentence = new LabeledSentence.Builder().buildEmptyOne();
						CodedInput codedInput = CodedInput.newInstance(filterInputStream);
						codedInput.setSizeLimit(536870912);
						schema.mergeFrom(codedInput, vecDicLabeledSentence);
						filterInputStream.close();
						logger.info("数据转换中....");
						List<String> trainSentence = Lists.newArrayList();
						List<String> sentences = vecDicLabeledSentence.getSentences();
						for (String sentence : sentences) {
								String str = sentence.trim();
								String replaceAll = str.replaceAll("（）|%", "");
								replaceAll = replaceAll.replaceAll("[0-9]+,.+[0-9]+|[0-9]+,.+\\.[0-9]+|[0-9]\\d*\\.\\d*|0\\.\\d*[0-9]\\d*", "");
								replaceAll = replaceAll.replaceAll(" +|　+| +", " ");
								String[] split = replaceAll.split("。");
								for (String s : split) {
										if (s.length() <= 8) {
												continue;
										}
										trainSentence.add(s);
								}
						}
						logger.info("数据准备完毕!!");

						logger.info("开始训练向量字典........");
						Word2Vec word2Vec = new Word2VecUtils.Builder()
										.addSentences(trainSentence)
										.setMinWordFrequency(minWordFrequency)
										.setIterations(iteration)
										.setSeed(seed)
										.setLayerSize(layerSize)
										.setLearningRate(learningRate)
										.setWindowSize(windowSize)
										.saveAt(outputPath, true)
										.build();

						Map<String, String> data = Maps.newHashMap();
						data.put("missionId", missionId.toString());
						data.put("processType", String.valueOf(ProcessTypeConstants.Word2vec_Generation));
						data.put("status", String.valueOf(ProcessTypeConstants.Process_Finished));
						HttpRequest.post(UrlConstants.ModelHelper_Local_Status_Url).form(data).created();
						logger.info("状态修改完毕,为:"+ProcessTypeConstants.Process_Finished);
						logger.info("向量字典训练完毕");
				} catch (Exception e) {
						logger.error("word2vec had occur an error", e);
						Map<String, String> data = Maps.newHashMap();
						data.put("missionId", missionId.toString());
						data.put("processType", String.valueOf(ProcessTypeConstants.Word2vec_Generation));
						data.put("status", String.valueOf(ProcessTypeConstants.Process_Error));
						HttpRequest.post(UrlConstants.ModelHelper_Local_Status_Url).form(data).created();
						logger.info("状态修改完毕,为:"+ProcessTypeConstants.Process_Error);
				}

		}

}
