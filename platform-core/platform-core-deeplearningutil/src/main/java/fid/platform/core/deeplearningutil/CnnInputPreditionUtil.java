package fid.platform.core.deeplearningutil;

import com.google.common.collect.Lists;
import fid.platform.core.common.pojo.commons.SortVecMap;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

import java.util.*;

public class CnnInputPreditionUtil {

		public static List<SortVecMap> preditCnnInput(String psg, Word2Vec word2Vec, ComputationGraph net, DataSetIterator testIter) {
				List<SortVecMap> result = Lists.newArrayList();

				Result parse2 = ArticleVectorUtil.getResultsFromArt(psg);
				List<Term> parse = parse2.getTerms();
				String newArt = "";
				for (Term term : parse) {
						if (word2Vec.getWordVectorMatrix(term.getName()) == null) {
								continue;
						}
						newArt += term.getName();
				}

				INDArray featuresFirstPas = ((CnnChineseSentencesIter) testIter).loadSingleSentence(newArt);

				INDArray predictionsFirstPas = net.outputSingle(featuresFirstPas);
				List<String> labels = testIter.getLabels();

				for (int i = 0; i < labels.size(); i++) {
						SortVecMap vecMap = new SortVecMap();
						vecMap.setDistance(predictionsFirstPas.getDouble(i));
						vecMap.setLabel(labels.get(i));
						result.add(vecMap);
				}

				Collections.sort(result);

				return result;
		}

}
