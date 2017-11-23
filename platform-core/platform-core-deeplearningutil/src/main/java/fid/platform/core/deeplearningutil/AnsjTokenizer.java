package fid.platform.core.deeplearningutil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import fid.platform.core.common.commonutil.AnsjLibraryLoader;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

public final class AnsjTokenizer extends AnsjLibraryLoader implements Tokenizer {

    private final List<String> tokenizer;
    private final AtomicInteger index;
    private TokenPreProcess tokenPreProcess;

    public AnsjTokenizer(String toTokenize) {
        this.tokenizer = tokenize(toTokenize);
        this.index = new AtomicInteger(0);
    }

    public static List<String> tokenize(String toTokenize) {
        if (Strings.isNullOrEmpty(toTokenize))
            return Collections.emptyList();
        //分析中文语义(分词)
        Iterator<Term> iterator = ArticleVectorUtil.getResultsFromArt(toTokenize).getTerms()
                .iterator();
        List<String> tokenizer = new ArrayList<String>();
        while (iterator.hasNext()) {
            String name = iterator.next().getName();
            if (!Strings.isNullOrEmpty(name))
                tokenizer.add(name);
        }
        //利用guava的不可变集合来提高并发时安全性和内存有效利用---btw google好屌..
        return ImmutableList.copyOf(tokenizer);
    }

    @Override
    public boolean hasMoreTokens() {
        return index.get() < tokenizer.size();
    }

    @Override
    public int countTokens() {
        return tokenizer.size();
    }

    @Override
    public String nextToken() {
        int i = index.getAndIncrement();
        Preconditions.checkPositionIndex(i, tokenizer.size());
        String base = tokenizer.get(i);
        if (tokenPreProcess != null)
            base = tokenPreProcess.preProcess(base);
        return base;
    }

    @Override
    public List<String> getTokens() {
        return tokenizer;
    }

    @Override
    public void setTokenPreProcessor(TokenPreProcess tokenPreProcessor) {
        this.tokenPreProcess = tokenPreProcessor;
    }

    public static void main(String[] args) {
        System.out.println(tokenize("个人所得税税率调高"));
    }
}
