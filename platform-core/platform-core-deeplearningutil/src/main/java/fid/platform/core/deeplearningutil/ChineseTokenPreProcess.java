package fid.platform.core.deeplearningutil;

import com.google.common.base.Strings;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;

public final class ChineseTokenPreProcess implements TokenPreProcess {
    @Override
    public String preProcess(String token) {
        if (Strings.isNullOrEmpty(token))
            return null;
        //去除所空格回车tab
        token = token.replaceAll(" |　| |\n|\t","");
        return token.replaceAll("[^\u4e00-\u9fa5·\\w]+", " ");
    }
}
