package nativetrainer;

import com.beust.jcommander.Parameter;
import fid.platform.core.common.pojo.commons.LabeledSentence;
import fid.platform.core.deeplearningutil.Word2VecUtils;
import fid.platform.trainer.word2vec.Word2vecTrainer;
import io.protostuff.CodedInput;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Word2VecNativeTrainer {

		private static final Logger logger = LoggerFactory.getLogger(Word2vecTrainer.class);

		public static void main(String[] args) throws Exception{
				String outputPath = "";
				String dataPath = "";
				int minWordFrequency = 5;
				int iteration = 1;
				int seed = 42;
				int layerSize = 100;
				double learningRate = 0.01;
				int windowSize = 5;

								//-----------data------------
				logger.info("收集训练信息完毕....");
				logger.info("正在准备数据...........");
				Schema<LabeledSentence> schema = RuntimeSchema.getSchema(LabeledSentence.class);

				BufferedInputStream filterInputStream = new BufferedInputStream(new FileInputStream(new File(dataPath)));
				LabeledSentence vecDicLabeledSentence = new LabeledSentence.Builder().buildEmptyOne();
				CodedInput codedInput = CodedInput.newInstance(filterInputStream);
				codedInput.setSizeLimit(536870912);
				schema.mergeFrom(codedInput, vecDicLabeledSentence);
				filterInputStream.close();
				logger.info("数据准备完毕!!");

				logger.info("开始训练向量字典........");
				Word2Vec word2Vec = new Word2VecUtils.Builder()
								.addSentences(vecDicLabeledSentence.getSentences())
								.setMinWordFrequency(minWordFrequency)
								.setIterations(iteration)
								.setSeed(seed)
								.setLayerSize(layerSize)
								.setLearningRate(learningRate)
								.setWindowSize(windowSize)
								.saveAt(outputPath,false)
								.build();

				logger.info("向量字典训练完毕");

		}


}
