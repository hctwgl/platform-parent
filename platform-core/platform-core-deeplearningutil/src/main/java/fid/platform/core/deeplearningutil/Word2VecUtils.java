package fid.platform.core.deeplearningutil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.NonNull;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.parallelism.ParallelWrapper;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csvreader.CsvReader;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.io.Files;

public final class Word2VecUtils {

    private static final Logger logger = LoggerFactory.getLogger(Word2VecUtils.class);

    private Word2VecUtils() {

    }

    private static Word2Vec fit(Collection<String> sentences, File file, int minWordFrequency, int iterations, int layerSize, int seed, double learningRate, int windowSize) {

        if (sentences == null || sentences.isEmpty())
            return null;
        SentenceIterator iterator = new CollectionSentenceIterator(sentences);
        TokenizerFactory tokenizerFactory = new AnsjTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new ChineseTokenPreProcess());

        return fit(iterator, tokenizerFactory, file,minWordFrequency,iterations,layerSize,seed,learningRate,windowSize);
    }

    private static Word2Vec fit(SentenceIterator iterator, TokenizerFactory tokenizerFactory, File file, int minWordFrequency, int iterations, int layerSize, int seed, double learningRate, int windowSize) {

        logger.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(minWordFrequency)
                .iterations(iterations)
                .layerSize(layerSize)
                .seed(seed)
                .learningRate(learningRate)
                .windowSize(windowSize)
                .iterate(iterator)
                .tokenizerFactory(tokenizerFactory)
                .build();

        logger.info("Fitting Word2Vec model....");
        vec.fit();

        if (file != null) {
            logger.info("model will be write to path[{}]", file.getAbsolutePath());
            WordVectorSerializer.writeWord2VecModel(vec, file);
        } else {
            logger.info("model will not be saved");
        }

        return vec;
    }

    //更新并新训练模型
    public static Word2Vec upTraining(List<String> sentences,Word2Vec vec,File saveAtFile){
    	if (sentences == null || sentences.isEmpty())
            return null;
    	Word2Vec newVec = vec;
        SentenceIterator iterator = new CollectionSentenceIterator(sentences);
        TokenizerFactory tokenizerFactory = new AnsjTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new ChineseTokenPreProcess());
        
    	vec.setTokenizerFactory(tokenizerFactory);
    	vec.setSentenceIterator(iterator);
    	vec.fit();
//    	saveAtFile.deleteOnExit();
//    	WordVectorSerializer.writeWord2VecModel(vec, saveAtFile);
    	return newVec;
    }
    
    public static Word2Vec restore(@NonNull String path) throws FileNotFoundException {

        Preconditions.checkArgument(!Strings.isNullOrEmpty(path), "illegal path");
        Word2Vec vec = WordVectorSerializer.readWord2VecModel(path);
        return vec;
    }

    //文本处理-->数据库
    private static StringBuilder readAllFromSentenceList(List<String> sentences){
        StringBuilder builder = new StringBuilder();
        List<String> lines = new ArrayList<String>();
        for (String sentence : sentences) {
            String replaceAll = sentence.replaceAll("[^\u4e00-\u9fa5\\w]+", " ");
            String replaceAll2 = replaceAll.replaceAll(" ", "");
            lines.add(replaceAll2);
        }
        builder.append(lines);
    	return builder;
    }
    
    //文本处理-->csv
    private static StringBuilder readAllFromCsv(Collection<File> files, Charset charset){
    	StringBuilder builder = new StringBuilder();
    	for (File file : files) {
            logger.info("reading text from [{}]...", file.getAbsolutePath());
            List<String> lines = new ArrayList<String>();
            try {
            	CsvReader csvReader = new CsvReader(file.getAbsolutePath(), ',',
            			charset);
                    csvReader.setSafetySwitch(false);
                    while (csvReader.readRecord()) {
    				String rawRecord = csvReader.get(0);
    				String replaceAll = rawRecord.replaceAll("[^\u4e00-\u9fa5\\w]+", " ");
    				String replaceAll2 = replaceAll.replaceAll(" |\\d+", "");
    				lines.add(replaceAll2);
    			}
    			csvReader.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
            if (lines.size() == 0)
                continue;
            for (String line : lines)
                builder.append(line);
        }
    	return builder;
    }
    
    //文本处理-->txt
    private static StringBuilder readAllText(Collection<File> files, Charset charset) {
        StringBuilder builder = new StringBuilder();
        for (File file : files) {
            logger.info("reading text from [{}]...", file.getAbsolutePath());
            List<String> lines = null;
            try {
                lines = Files.readLines(file, charset);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
            if (lines == null)
                continue;
            for (String line : lines)
                builder.append(line);
        }
        return builder;
    }

    private static List<String> splitIntoSentences(CharSequence cs, String regex) {
        return RegexUtils.group(cs, regex);
    }
    
    public static Builder newWord2Vec() {
        return new Builder();
    }

    public static final class Builder {
        private final Set<File> files = new HashSet<File>();
        private Charset charset = Charsets.UTF_8;
        private File file = null;
        private List<String> sentences = new ArrayList<String>();
        private int minWordFrequency = 7;
        private int iterations = 1;
        private int layerSize = 128;
        private int seed = 42;
        private double learningRate = 1e-3;
        private int windowSize = 5;

        public Builder setMinWordFrequency(int minWordFrequency) {
            this.minWordFrequency = minWordFrequency;
            return this;
        }

        public Builder setIterations(int iterations) {
            this.iterations = iterations;
            return this;
        }

        public Builder setLayerSize(int layerSize) {
            this.layerSize = layerSize;
            return this;
        }

        public Builder setSeed(int seed) {
            this.seed = seed;
            return this;
        }

        public Builder setLearningRate(double learningRate) {
            this.learningRate = learningRate;
            return this;
        }

        public Builder setWindowSize(int windowSize) {
            this.windowSize = windowSize;
            return this;
        }

        //添加句子
        public Builder addSentences(@NonNull List<String> sentences) {
            Preconditions.checkArgument(sentences != null && sentences.size() != 0, "invalid file");
            this.sentences = sentences;
            return this;
        }
        
        //添加文本
        public Builder addTextFile(@NonNull File file) {
            Preconditions.checkArgument(file != null && file.isFile(), "invalid file");
            files.add(file);
            return this;
        }
        
        //添加所有文本
        public Builder addAllTextFile(@NonNull Collection<File> files) {
            Preconditions.checkArgument(files != null && files.size() != 0, "empty files");
            for (File file : files) {
                addTextFile(file);
            }
            return this;
        }
        
        //编码编码
        public Builder charset(@NonNull Charset charset) {
            Preconditions.checkNotNull(charset, "null charset");
            this.charset = charset;
            return this;
        }
        
        //存储向量词典
        public Builder saveAt(@NonNull String path, boolean delOld) {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(path), "illegal path");
            File file = new File(path);
            if (file.exists()) {
                logger.info("[{}] already exists", file.getAbsolutePath());
                if (delOld) {
                    logger.info("[{}] will be deleted", file.getAbsolutePath());
                    file.delete();
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                } else {
                    file = null;
                }
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            this.file = file;
            return this;
        }

        //建立模型的方法(在参数准备完毕之后)
        public Word2Vec build() {
        	CharSequence cs;
        	StringBuilder sbAll = new StringBuilder();
        	if(sentences.size() !=0 ){
        		sbAll.append(readAllFromSentenceList(sentences));
        	}
        	if(files.size() != 0){
        		sbAll.append(readAllText(files, charset));
        	}
            cs = sbAll;
            List<String> sentences = splitIntoSentences(cs, "[^，,。.？?！!\\\\s]+");
            return fit(sentences, file,minWordFrequency,iterations,layerSize,seed,learningRate,windowSize);
        }
    }


}
