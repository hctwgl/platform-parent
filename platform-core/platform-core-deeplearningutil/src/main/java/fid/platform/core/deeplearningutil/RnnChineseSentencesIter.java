package fid.platform.core.deeplearningutil;

import static org.nd4j.linalg.indexing.NDArrayIndex.all;
import static org.nd4j.linalg.indexing.NDArrayIndex.point;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import fid.platform.core.common.pojo.commons.LabeledSentence;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;

public class RnnChineseSentencesIter implements DataSetIterator {

    private final WordVectors wordVectors;
    private final int batchSize;
    private final int vectorSize;
    private final int truncateLength;
    private int maxLength;
    private final LabeledSentence labeledSentence;
    private final List<Pair<String, List<String>>> categoryData = new ArrayList<>();
    private int cursor = 0;
    private int totalNews = 0;
    private final TokenizerFactory tokenizerFactory;
    private int newsPosition = 0;
    private final List<String> labels;
    private int currCategory = 0;

    private RnnChineseSentencesIter(LabeledSentence labeledSentence, WordVectors wordVectors,
                                    int batchSize, int truncateLength,
                                    TokenizerFactory tokenizerFactory) {
        this.labeledSentence = labeledSentence;
        this.batchSize = batchSize;
        this.vectorSize = wordVectors.getWordVector(wordVectors.vocab()
                .wordAtIndex(0)).length;
        this.wordVectors = wordVectors;
        this.truncateLength = truncateLength;
        this.tokenizerFactory = tokenizerFactory;
        this.populateData();
        this.labels = new ArrayList<>();
        /**
         * TODO
         * 装入labels
         */
        for (int i = 0; i < categoryData.size(); i++) {
            this.labels.add(categoryData.get(i).getLeft());
        }
    }

    public static Builder Builder() {
        return new Builder();
    }

    @Override
    public DataSet next(int num) {
        if (cursor >= this.totalNews)
            throw new NoSuchElementException();
        try {
            return nextDataSet(num);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private DataSet nextDataSet(int num) throws IOException {
        // Loads news into news list from categoryData List along with category
        // of each news
        List<String> news = new ArrayList<>(num);
        int[] category = new int[num];
        // private final List<Pair<String, List<String>>> categoryData
        for (int i = 0; i < num && cursor < totalExamples(); i++) {
            if (currCategory < categoryData.size()) {

                news.add(this.categoryData.get(currCategory).getValue()
                        .get(newsPosition >= this.categoryData.get(currCategory).getValue().size()-1 ? newsPosition - new Random().nextInt(this.categoryData.get(currCategory).getValue().size()) / 2:
                newsPosition));
                category[i] = Integer.parseInt(this.categoryData.get(
                        currCategory).getKey());
                currCategory++;
                cursor++;
            } else {
                currCategory = 0;
                newsPosition++;
                i--;
            }
        }
        // 把字典内不存在的词移除
        List<List<String>> allTokens = new ArrayList<>(news.size());
        maxLength = 0;
        for (String s : news) {
            List<String> tokens = tokenizerFactory.create(s).getTokens();
            List<String> tokensFiltered = new ArrayList<>();
            for (String t : tokens) {
                if (wordVectors.hasWord(t))
                    tokensFiltered.add(t);
            }
            allTokens.add(tokensFiltered);
            maxLength = Math.max(maxLength, tokensFiltered.size());
        }
        // If longest news exceeds 'truncateLength': only take the first
        // 'truncateLength' words
        // System.out.println("maxLength : " + maxLength);
        if (maxLength > truncateLength)
            maxLength = truncateLength;
        // Create data for training
        // Here: we have news.size() examples of varying lengths
        INDArray features = Nd4j.create(news.size(), vectorSize, maxLength);
        INDArray labels = Nd4j.create(news.size(), this.categoryData.size(),
                maxLength); // Three labels: Crime, Politics, Bollywood
        // Because we are dealing with news of different lengths and only one
        // output at the final time step: use padding arrays
        // Mask arrays contain 1 if data is present at that time step for that
        // example, or 0 if data is just padding
        INDArray featuresMask = Nd4j.zeros(news.size(), maxLength);
        INDArray labelsMask = Nd4j.zeros(news.size(), maxLength);
        int[] temp = new int[2];
        for (int i = 0; i < news.size(); i++) {
            List<String> tokens = allTokens.get(i);
            temp[0] = i;
            //把每一个词的词向量放入训练集
            for (int j = 0; j < tokens.size() && j < maxLength; j++) {
                String token = tokens.get(j);
                INDArray vector = wordVectors.getWordVectorMatrix(token);
                features.put(new INDArrayIndex[]{point(i), all(), point(j)},
                        vector);
                temp[1] = j;
                featuresMask.putScalar(temp, 1.0);
            }
            int idx = category[i];
            int lastIdx = Math.min(tokens.size(), maxLength);
            labels.putScalar(new int[]{i, idx - 1, lastIdx - 1}, 1.0);
            labelsMask.putScalar(new int[]{i, lastIdx - 1}, 1.0);
        }
        DataSet ds = new DataSet(features, labels, featuresMask, labelsMask);
        return ds;
    }

    public INDArray loadFeaturesFromFile(File file, int maxLength)
            throws IOException {
        String news = FileUtils.readFileToString(file);
        return loadFeaturesFromString(news, maxLength);
    }

    public INDArray loadFeaturesFromString(String reviewContents, int maxLength) {
        List<String> tokens = tokenizerFactory.create(reviewContents)
                .getTokens();
        List<String> tokensFiltered = new ArrayList<>();
        for (String t : tokens) {
            if (wordVectors.hasWord(t))
                tokensFiltered.add(t);
        }
        int outputLength = Math.max(maxLength, tokensFiltered.size());
        INDArray features = Nd4j.create(1, vectorSize, outputLength);
        for (int j = 0; j < tokens.size() && j < maxLength; j++) {
            String token = tokens.get(j);
            INDArray vector = wordVectors.getWordVectorMatrix(token);
            features.put(new INDArrayIndex[]{point(0), all(), point(j)},
                    vector);
        }
        return features;
    }

    /*
     * This function loads news headlines from files stored in resources into
     * categoryData List.
     * TODO
     * 预改方法记录-----
     * 此方法在此提取的为训练集（rnnsenec--为所有积极言论分类）    -    （rnnsenectest--为所有消极言论分类）文本
     * 需要将此方法改造为lebelsentence的形式
     * dataDirectory为测试集合的全集
     * dataDirectory的形式为：List<Pair<String, List<String>>>
     *                                label---senteneceList
     *-----so  dataDirectory的size即为label的size
     *
     *
     */
    private void populateData() {
        List<String> tempNewsList = new ArrayList<>();
        String tempLabel = "";
        Pair<String, List<String>> tempPair = Pair.of("", new ArrayList<>());
        for (int i = 0; i < this.labeledSentence.getLabels().size(); i++) {
            String label = this.labeledSentence.getLabels().get(i);
            String psgs = this.labeledSentence.getSentences().get(i);
            if ((!label.equals(tempLabel) || i == this.labeledSentence.getLabels().size() - 1) && i != 0) {
                tempPair = Pair.of(tempLabel, tempNewsList);
                this.categoryData.add(tempPair);
                tempNewsList = new ArrayList<>();
                tempNewsList.add(psgs);
                this.totalNews++;
                tempLabel = label;
            } else {
                tempNewsList.add(psgs);
                tempLabel = label;
                this.totalNews++;
            }
        }

    }

    @Override
    public int totalExamples() {
        return this.totalNews;
    }

    @Override
    public int inputColumns() {
        return vectorSize;
    }

    @Override
    public int totalOutcomes() {
        return this.categoryData.size();
    }

    @Override
    public void reset() {
        cursor = 0;
        newsPosition = 0;
        currCategory = 0;
    }

    public boolean resetSupported() {
        return true;
    }

    @Override
    public boolean asyncSupported() {
        return true;
    }

    @Override
    public int batch() {
        return batchSize;
    }

    @Override
    public int cursor() {
        return cursor;
    }

    @Override
    public int numExamples() {
        return totalExamples();
    }

    @Override
    public void setPreProcessor(DataSetPreProcessor preProcessor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getLabels() {
        return this.labels;
    }

    @Override
    public boolean hasNext() {
        return cursor < numExamples();
    }

    @Override
    public DataSet next() {
        return next(batchSize);
    }

    @Override
    public void remove() {
    }

    @Override
    public DataSetPreProcessor getPreProcessor() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public int getMaxLength() {
        return this.maxLength;
    }

    public static class Builder {
        private LabeledSentence labeledSentence;
        private WordVectors wordVectors;
        private int batchSize;
        private int truncateLength;
        TokenizerFactory tokenizerFactory;

        Builder() {
        }

        public Builder labeledSentence(LabeledSentence labeledSentence) {
            this.labeledSentence = labeledSentence;
            return this;
        }

        public Builder wordVectors(WordVectors wordVectors) {
            this.wordVectors = wordVectors;
            return this;
        }

        public Builder batchSize(int batchSize) {
            this.batchSize = batchSize;
            return this;
        }

        public Builder truncateLength(int truncateLength) {
            this.truncateLength = truncateLength;
            return this;
        }


        public Builder tokenizerFactory(
                TokenizerFactory tokenizerFactory) {
            this.tokenizerFactory = tokenizerFactory;
            return this;
        }

        public RnnChineseSentencesIter build() {
            return new RnnChineseSentencesIter(labeledSentence, wordVectors, batchSize,
                    truncateLength, tokenizerFactory);
        }

        public String toString() {
            return "org.deeplearning4j.examples.recurrent.ProcessNews.NewsIterator.Builder(labeledSentence--labelSize="
                    + this.labeledSentence.getLabels().size()
                    + ", wordVectors="
                    + this.wordVectors
                    + ", batchSize="
                    + this.batchSize
                    + ", truncateLength="
                    + this.truncateLength + ")";
        }
    }
}
