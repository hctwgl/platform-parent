package fid.platform.core.deeplearningutil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import fid.platform.core.common.commonutil.AnsjLibraryLoader;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.UserDicNatureRecognition;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.nd4j.linalg.api.ndarray.INDArray;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * 用于把文章按照对应词典转化成对应学习词典的向量 工具集
 * 
 * @author Drx
 *
 */
public class ArticleVectorUtil extends AnsjLibraryLoader {
	

	/**
	 * 获取文章关键词
	 * @param article
	 * @return
	 */
	public static List<String> getAllKeyWordsFromArt(String article,int max){
		List<String> labelList = Lists.newArrayList();
		String replaceAll = article.replaceAll("[^\u4e00-\u9fa5\\w]+", " ");
		String replaceFinal = replaceAll.replaceAll(" ", "");
		// ansj解析
		KeyWordComputer kwc = new KeyWordComputer(max);
		List<Keyword> keyWords = kwc.computeArticleTfidf(replaceFinal);
		for (Keyword keyword : keyWords) {
			labelList.add(keyword.getName());
		}
		return labelList;
	}
	/**
	 * 获取文章关键词,带title
	 * @param article title
	 * @return
	 */
	public static List<String> getAllKeyWordsFromArt(String article,String title,int max){
		List<String> labelList = Lists.newArrayList();
		String replaceAll = article.replaceAll("[^\u4e00-\u9fa5\\w]+", " ");
		String replaceFinal = replaceAll.replaceAll(" ", "");
		// ansj解析
		KeyWordComputer kwc = new KeyWordComputer(max);
		List<Keyword> keyWords = kwc.computeArticleTfidf(replaceFinal);
		for (Keyword keyword : keyWords) {
			labelList.add(keyword.getName());
		}
		return labelList;
	}
	
	/**
	 * 获取一篇文章的所有分词
	 * @param article
	 * @return
	 */
	public static List<String> getAllLabelsFromArt(String article){
		List<String> labelList = Lists.newArrayList();
			// 把文章格式进行调整->变为一行去除不必要的东西
			String testStr = article.replaceAll(" |　| |\n|\t","");
			String replaceAll = testStr.replaceAll("[^\u4e00-\u9fa5\\w]+", " ");
			// ansj解析
			Result result = NlpAnalysis.parse(replaceAll);
			UserDicNatureRecognition udn = new UserDicNatureRecognition();
			udn.recognition(result);
			List<Term> parse = result.getTerms();
			for (Term term : parse) {
				labelList.add(term.getName());
			}
		return labelList;
	}
	/**
	 * 获取一篇文章的所有分词(result)
	 * @param article
	 * @return
	 */
	public static Result getResultsFromArt(String article){
			// 把文章格式进行调整->变为一行去除不必要的东西
			String testStr = article.replaceAll(" |　| |\n|\t","");
			String replaceAll = testStr.replaceAll("[^\u4e00-\u9fa5\\w]+", " ");
			// ansj解析
			Result result = NlpAnalysis.parse(replaceAll);
			UserDicNatureRecognition udn = new UserDicNatureRecognition();
			udn.recognition(result);
		return result;
	}

	/**
	 * 获取单篇文章的特征向量
	 * 
	 * @param dicFile
	 * @param article
	 * @return
	 */
	public static INDArray getSingleArticleVec(File dicFile, String article) {
		String path = dicFile.getAbsolutePath();
		try {
			// 同类文章的所有词汇
			Word2Vec restore = Word2VecUtils.restore(path);
			List<String> labels = getAllLabelsFromArt(article);
			INDArray wordVectorsMean = restore.getWordVectorsMean(labels);
			return wordVectorsMean;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取单篇文章的特征向量
	 * 
	 * @param article
	 * @return
	 */
	public static INDArray getSingleArticleVec(Word2Vec vec, String article) {
		// 同类文章的所有词汇
		List<String> labels = getAllLabelsFromArt(article);
		Word2Vec restore = vec;
		INDArray wordVectorsMean = restore.getWordVectorsMean(labels);
		return wordVectorsMean;
	}

	/**
	 * 把文章序列转化为均值向量
	 * 
	 * @param dicFile
	 *            训练出来的向量词典
	 * @param articles
	 *            以字符串形式传输的文章
	 * @return
	 */
	public static INDArray getArticlesAvgVec(File dicFile, List<String> articles) {
		String path = dicFile.getAbsolutePath();
		// 以不可变集合存储以加快读取
		ImmutableList<String> articlesIm = ImmutableList.copyOf(articles);
		try {
			// 同类文章的所有词汇
			List<String> labels = Lists.newArrayList();
			Word2Vec restore = Word2VecUtils.restore(path);
			for (String string : articlesIm) {
				// 把文章格式进行调整->变为一行去除不必要的东西
				String replaceAll = string.replaceAll("[^\u4e00-\u9fa5\\w]+",
						" ");
				String replaceFinal = replaceAll.replaceAll(" ", "");
				// ansj解析
				Result result = NlpAnalysis.parse(replaceFinal);
				List<Term> artWords = result.getTerms();
				for (Term term : artWords) {
					labels.add(term.getName());
				}
			}
			INDArray wordVectorsMean = restore.getWordVectorsMean(labels);
			return wordVectorsMean;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 把文章序列转化为均值向量
	 * 
	 * @param articles
	 *            以字符串形式传输的文章
	 * @return
	 */
	public static INDArray getArticlesAvgVec(Word2Vec vec, List<String> articles) {
		// 以不可变集合存储以加快读取
		ImmutableList<String> articlesIm = ImmutableList.copyOf(articles);
		// 同类文章的所有词汇
		List<String> labels = Lists.newArrayList();
		Word2Vec restore = vec;
		for (String string : articlesIm) {
			// 把文章格式进行调整->变为一行去除不必要的东西
			String replaceAll = string.replaceAll("[^\u4e00-\u9fa5\\w]+", " ");
			String replaceFinal = replaceAll.replaceAll(" ", "");
			// ansj解析
			Result result = NlpAnalysis.parse(replaceFinal);
			List<Term> artWords = result.getTerms();
			for (Term term : artWords) {
				labels.add(term.getName());
			}
		}
		INDArray wordVectorsMean = restore.getWordVectorsMean(labels);
		return wordVectorsMean;
	}

	public static StringBuilder getFilterPsg(String sentence,Word2Vec word2Vec) {
		String replaceAll = sentence.replaceAll("[^\u4e00-\u9fa5\\w]+", " ");
		String artFinal = replaceAll.replaceAll(" ", "");
		Result result = NlpAnalysis.parse(artFinal);
		List<Term> parse = result.getTerms();
		StringBuilder finalArtStr = new StringBuilder();
		for (Term term : parse) {
			String str = term.getName();
			INDArray wordVectorMatrix = word2Vec.getWordVectorMatrix(str);
			if (wordVectorMatrix == null) {
				continue;
			}
			finalArtStr.append(term.getName());
		}
		return finalArtStr;
	}

}
