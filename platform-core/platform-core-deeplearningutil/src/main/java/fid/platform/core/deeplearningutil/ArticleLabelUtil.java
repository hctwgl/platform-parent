package fid.platform.core.deeplearningutil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import fid.platform.core.common.commonutil.AnsjLibraryLoader;
import fid.platform.core.common.pojo.commons.LabeledSentence;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.apache.commons.lang3.StringUtils;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.nd4j.linalg.api.ndarray.INDArray;

import com.csvreader.CsvReader;
import com.google.common.collect.Lists;

/**
 * 用于把CSV文章转为list(sentences)和list(lebels)对应
 * length一样,一一对应
 *
 * @author Drx
 */
public class ArticleLabelUtil extends AnsjLibraryLoader {

    public static LabeledSentence getLabeledSentecesFromCSV(Word2Vec word2Vec,File CSVFile, String encode) {
        LabeledSentence labeledSentence = new LabeledSentence.Builder().buildEmptyOne();
        try {
            CsvReader reader = new CsvReader(CSVFile.getAbsolutePath(), ',',
                    Charset.forName(encode));
            //解除最大阅读量限制
            reader.setSafetySwitch(false);
            //设置头
            String[] headers = {"text", "label"};
            reader.setHeaders(headers);
            //分别封装文本的list和对应label的list,这里不用guava的Multimap<String,String>类,直接自己封装好理解
            List<String> articles = Lists.newArrayList();
            List<String> labels = Lists.newArrayList();
            while (reader.readRecord()) {
                //添加文章
                String article = reader.get("text");
                //简单处理成行
                String replaceAll = article.replaceAll("[^\u4e00-\u9fa5·\\w]+", " ");
                Result result = NlpAnalysis.parse(replaceAll);
                List<Term> parse = result.getTerms();
                String finalArtStr = "";
                for (Term term : parse) {
                    String str = term.getName();
                    INDArray wordVectorMatrix = word2Vec.getWordVectorMatrix(str);
                    if (wordVectorMatrix == null) {
                        continue;
                    }
                    finalArtStr += term.getName();
                }
                if (StringUtils.isNotBlank(finalArtStr)){
                    articles.add(finalArtStr);
                    String label = reader.get("label");
                    labels.add(label);
                }else {
                    continue;
                }
                articles.add(finalArtStr);
                String label = reader.get("label");
                labels.add(label);
            }
            labeledSentence = new LabeledSentence.Builder().setLabels(labels).setSentences(articles).build();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return labeledSentence;
    }

}
