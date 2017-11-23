package fid.platform.core.common.constant;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mengtian on 2017/8/18.
 */
public class Constant {
    /**
     * 存放向量词典的目录
     */
    public static final String VEC_DIC_URL_KEY = "robot.word2vec.dataVecDics";

    /**
     * 神经网络 训练完成model存放路径
     */
    public static final String MODEL_PATH = "robot.model.dir";

    public static final String SENTENTCE_PATH = "robot.sentence.dir";

    /**
     * cms_content表已打过标签数据的key
     */
    public static final String CONTENT_INDEX_KEY = "robot:target:index";

    public static final String CONTENT_SUBJECT_INDEX_KEY = "robot:target:subject:index";

    public final static int CONTENT_TABLE_TYPE = 1;
    public final static int SUBJECT_TABLE_TYPE = 3;


    /**
     * 任务状态 1、资讯分类，2、数据准备，3、数据处理、4、词典训练、5、神经网络训练
     */
    public static final int MISSION_CLASSIFY = 1;
    public static final int MISSION_DATA_INIT = 2;
    public static final int MISSION_DATA_GENERATOR = 3;
    public static final int MISSION_TRAIN_WORD2VEC = 4;
    public static final int MISSION_TRAIN_MODEL = 5;

    /**
     * 爬虫状态 0、未开始，1、爬取中，2、爬取完成，3、停止爬取，4、终止爬取任务
     */
    public static final Integer CRAWLER_NOTSTART = 0;
    public static final Integer CRAWLER_RUNNING = 1;
    public static final Integer CRAWLER_COMPLETE = 2;
    public static final Integer CRAWLER_STOP = 3;
    public static final Integer CRAWLER_SHUTDOWN = 4;


    /**
     * 系统模块状态 0、空闲， 1、使用中，2、下线
     */
    public static final Integer SYSTEM_MODULE_NOT_IN_USE = 0;
    public static final Integer SYSTEM_MODULE_IN_USE = 1;
    public static final Integer SYSTEM_MODULE_OUT_SERVICE = 2;


    /**
     * 系统各个模块标识
     */
    public static final Integer MODULE_TRAINDATA = 2;
    public static final Integer MODULE_WORD2VEC = 3;
    //所有模块
    public static final Integer MODULE_ALL_TRAIN_MODULE = 10;

    //系统共有多少种类型的模块
    public static final List<Integer> MODULES = Arrays.asList(2, 3);
}
