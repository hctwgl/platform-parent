package platform.api.cache.model;

import fid.platform.core.deeplearningutil.Word2VecUtils;
import org.deeplearning4j.models.word2vec.Word2Vec;

import java.io.FileNotFoundException;

/**
 * Created by mengtian on 2017/8/18.
 * 词典缓存
 */
public class Word2VecCache {

    private static Word2Vec word2Vec = null;

    private static Word2Vec filterVec = null;

    private static String path = null;

    private static String filterPath = null;

    public Word2Vec getWord2Vec() {
        return word2Vec;
    }

    public Word2Vec getFilterVec() {
        return filterVec;
    }

    /**
     * 词典加载
     *
     * @throws FileNotFoundException
     */
    public Word2VecCache restore() throws FileNotFoundException {
        if (path != null){
            word2Vec = Word2VecUtils.restore(path);
        }
        if (filterPath != null){
            filterVec = Word2VecUtils.restore(filterPath);
        }
        return this;
    }

    /**
     * 重新加载词典等数据
     */
    public Word2VecCache reload(String path) throws FileNotFoundException {
        setPath(path);
        restore();
        return this;
    }

    public Word2VecCache setPath(String path) {
        this.path = path;
        return this;
    }
    public Word2VecCache setFilterPath(String filterPath) {
        this.filterPath = filterPath;
        return this;
    }

    private Word2VecCache() {
    }

    public static Word2VecCache getInstance() {
        return CacheInstance.INSTANCE;
    }

    private static class CacheInstance {
        private static final Word2VecCache INSTANCE = new Word2VecCache();
    }
}
