package fid.platform.core.common.commonutil;

import com.google.common.collect.Maps;

import java.util.Map;

public class CacheMaps {

    private static CacheMaps cacheMaps = new CacheMaps();

    private static Map<Long,Map<String,Object>> cache = Maps.newHashMap();

    public static final String VECTOR_DICTIONRY = "VECTOR_DICTIONRY";
    public static final String NN_MODEL = "NN_MODEL";
    public static final String LABELED_SENTENCE = "LABELED_SENTENCE";

    private CacheMaps(){


    }

    public static CacheMaps getInstance() {
        return cacheMaps;
    }

    /**
     * 设置分任务缓存
     * @param missionId 任务id
     * @param typeKey 缓存键,预设置的键在CacheMaps常量中
     * @param item 缓存对象
     */
    public void setCache(Long missionId,String typeKey,Object item){
        if (cache.get(missionId)!=null){
            cache.get(missionId).put(typeKey,item);
        }else {
            Map<String, Object> innerMap = Maps.newHashMap();
            innerMap.put(typeKey, item);
            cache.put(missionId, innerMap);
        }
    }

    public void setCache(Long missionId,Map<String,Object> itemMap){
        cache.put(missionId,itemMap);
    }

    public Map<Long,Map<String,Object>> getAllCache(){
        return cache;
    }

    public Map<String,Object> getCacheByMission(Long missionId){
        return cache.get(missionId);
    }
}
