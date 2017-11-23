package fid.platform.grabhandler.utils;

/**
 * Created by mengtian on 2017/11/15
 */
public interface CrawlerProcess {
    /**
     * 启动完成时执行
     *
     * @param orgConfId
     */
    void startOn(Long orgConfId);

    /**
     * 爬取任务完成后
     *
     * @param orgConfId
     */
    void complete(Long orgConfId);
}
