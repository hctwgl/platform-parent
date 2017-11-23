package fid.platform.core.deeplearningutil;

import java.io.File;
import java.util.Random;

import fid.platform.core.common.pojo.commons.LabeledSentence;
import org.deeplearning4j.iterator.LabeledSentenceProvider;
import org.deeplearning4j.iterator.provider.CollectionLabeledSentenceProvider;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;


/**
 * 用于构建卷积神经网络中文的遍历器
 * @author Drx
 *
 */
public class ChineseDataSetIteratorUtil {
	
	public static DataSetIterator getDataSetIterator(Word2Vec word2Vec,File csvFile,
													 WordVectors wordVectors, int minibatchSize, int maxSentenceLength,
													 Random rng) {
		
		//把csv文件封装成对应的labeledSentece类->请点进去看
		LabeledSentence labeledSentence = ArticleLabelUtil.getLabeledSentecesFromCSV(word2Vec,csvFile,"UTF-8");

		//中文token,构建时必须加上此factory
		TokenizerFactory tokenizerFactory = new AnsjTokenizerFactory();
		tokenizerFactory.setTokenPreProcessor(new ChineseTokenPreProcess());
		
		// 此处用sentence->collection(dl4j提供了多种遍历方法)
		LabeledSentenceProvider sentenceProvider = new CollectionLabeledSentenceProvider(
				labeledSentence.getSentences(), labeledSentence.getLabels(),rng);
		
		//构建中文遍历器
		CnnChineseSentencesIter cnnSentenceDataSetIterator = new CnnChineseSentencesIter.Builder()
				.sentenceProvider(sentenceProvider)//
				.wordVectors(wordVectors)
				.minibatchSize(minibatchSize)
				.tokenizerFactory(tokenizerFactory)
				.maxSentenceLength(maxSentenceLength)
				.useNormalizedWordVectors(false)
				.build();
		
		return cnnSentenceDataSetIterator;
	}

	/**
	 * 利用labeledSentence获取Iter
	 * @param labeledSentence
	 * @param wordVectors
	 * @param minibatchSize
	 * @param maxSentenceLength
	 * @param rng
	 * @return
	 */
	public static DataSetIterator getSimpleDataSetIterator(LabeledSentence labeledSentence,
			WordVectors wordVectors, int minibatchSize, int maxSentenceLength,
			Random rng) {

		//中文token,构建时必须加上此factory
		TokenizerFactory tokenizerFactory = new AnsjTokenizerFactory();
		tokenizerFactory.setTokenPreProcessor(new ChineseTokenPreProcess());
		
		// 此处用sentence->collection(dl4j提供了多种遍历方法)
		LabeledSentenceProvider sentenceProvider = new CollectionLabeledSentenceProvider(
				labeledSentence.getSentences(), labeledSentence.getLabels(),rng);
		
		//构建中文遍历器
		CnnChineseSentencesIter cnnSentenceDataSetIterator = new CnnChineseSentencesIter.Builder()
				.sentenceProvider(sentenceProvider)//
				.wordVectors(wordVectors)
				.minibatchSize(minibatchSize)
				.tokenizerFactory(tokenizerFactory)
				.maxSentenceLength(maxSentenceLength)
				.useNormalizedWordVectors(false)
				.build();
		
		return cnnSentenceDataSetIterator;
	}
	
}
