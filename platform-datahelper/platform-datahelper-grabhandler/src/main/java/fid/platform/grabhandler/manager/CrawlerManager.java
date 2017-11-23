package fid.platform.grabhandler.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Spider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mengtian on 2017/11/14
 */
public class CrawlerManager {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerManager.class);

    private CrawlerManager() {
    }

    private static Map<Long, Map<Long, Spider>> spiders = new ConcurrentHashMap<>();

    public static void addSpider(Long missionId, Long orgConfId, Spider spider) {
        if (spiders.containsKey(missionId)) {
            spiders.get(missionId).put(orgConfId, spider);
        } else {
            Map<Long, Spider> spiderIdMap = new HashMap<>();
            spiderIdMap.put(orgConfId, spider);
            spiders.put(missionId, spiderIdMap);
        }
    }

    public static void delSpider(Long missionId, Long orgConfId) {
        try {
            getSpider(missionId, orgConfId).stop();
        } catch (Exception e) {
            logger.error("停止爬虫出错", e);
        }
        spiders.remove(missionId);
    }

    public static void delSpider(Long missionId) {
        try {
            for (Map.Entry<Long, Spider> spiderEntry : spiders.get(missionId).entrySet()) {
                spiderEntry.getValue().stop();
            }
        } catch (Exception e) {
            logger.error("出错了", e);
        }
        spiders.remove(missionId);
    }

    public static Spider getSpider(Long missionId, Long orgConfId) {
        return spiders.get(missionId).get(orgConfId);
    }

    public static Map<Long, Map<Long, Spider>> getSpiders() {
        return spiders;
    }
}
