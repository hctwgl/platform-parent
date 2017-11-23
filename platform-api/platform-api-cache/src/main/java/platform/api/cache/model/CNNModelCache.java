package platform.api.cache.model;


import org.deeplearning4j.nn.graph.ComputationGraph;

/**
 * Created by mengtian on 2017/8/18.
 * 模型缓存
 */
public class CNNModelCache {

    private static ComputationGraph net;

    private static ComputationGraph thirdNet;

    private static ComputationGraph filterNet;

    public void setNet(ComputationGraph net) {
        this.net = net;
    }

    public ComputationGraph getModel() {
        return net;
    }

    public ComputationGraph getFilterNet() {
        return filterNet;
    }

    public void setFilterNet(ComputationGraph filterNet) {
        this.filterNet = filterNet;
    }

    public void setThirdNet(ComputationGraph thirdNet) {
        this.thirdNet = thirdNet;
    }

    public ComputationGraph getThirdModel() {
        return thirdNet;
    }

    private CNNModelCache() {
    }

    public static CNNModelCache getInstance() {
        return CNNModelCacheInstance.INSTANCE;
    }

    private static class CNNModelCacheInstance {
        private static final CNNModelCache INSTANCE = new CNNModelCache();
    }
}
