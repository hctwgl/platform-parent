package fid.platform.grabhandler.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.annotation.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.tools.javac.main.Main;

import fid.platform.core.common.pojo.robot.RobotNlpOrgData;
import fid.platform.grabhandler.service.RobotNlpOrgDataService;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

/**
 * Store results in files.<br>
 * 抓取数据的处理
 * @author 杨锋 <br>
 * @since 0.1.0
 */
@ThreadSafe
public class FidPipeline extends FilePersistentBase implements Pipeline {

    private Logger logger = LoggerFactory.getLogger(getClass());
    /*@Resource
    private RobotNlpOrgDataService robotNlpOrgDataService;*/
    public FidPipeline() {
        setPath("/data/webmagic/");
    }

    public FidPipeline(String path) {
        setPath(path);
    }
/*    @Override
    public void process(ResultItems resultItems, Task task) {
        //String path = this.path + PATH_SEPERATOR + task.getUUID() + PATH_SEPERATOR;
        try {
        	
        	 * 一：存文件
        	 
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getFile(path + DigestUtils.md5Hex(resultItems.getRequest().getUrl()) + ".txt")),"UTF-8"));
            //printWriter.println("url:\t" + resultItems.getRequest().getUrl());
            for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
                if (entry.getValue() instanceof Iterable) {
                    Iterable value = (Iterable) entry.getValue();
                    printWriter.println(entry.getKey() + ":");
                    for (Object o : value) {
                        printWriter.println(o);
                    }
                } else {
                    printWriter.println(entry.getKey() + ":\t" + entry.getValue());
                }
            }
            printWriter.close();
            
            
             * 二：存数据库
             
           RobotNlpOrgData data = (RobotNlpOrgData)resultItems.get("pagedata");
			robotNlpOrgDataService.insertRobotNlpOrgData(data);
			
            
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("write database error", e);
        }
    }*/
    
    @Override
    public void process(ResultItems resultItems, Task task) {
        //String path = this.path + PATH_SEPERATOR + resultItems.get("链接") + PATH_SEPERATOR;
        try {
        	//这里的文件名是将url做md5加密
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(getFile(path + DigestUtils.md5Hex(resultItems.getRequest().getUrl()) + ".txt")),"UTF-8"));
            //printWriter.println("url:\t" + resultItems.getRequest().getUrl());
            for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            	if(entry.getValue().toString().trim().length() == 0 || entry.getKey().trim().length() == 0) continue;
                if (entry.getValue() instanceof Iterable) {
                    Iterable value = (Iterable) entry.getValue();
                    printWriter.println(entry.getKey() + ":");
                    for (Object o : value) {
                        printWriter.println(o);
                    }
                } else {
                    printWriter.println(entry.getKey() + ":\t" + entry.getValue());
                }
            }
            printWriter.close();
        } catch (IOException e) {
            logger.warn("write file error", e);
        }
    }
    /*public static void main(String[] args){
    	System.out.println(DigestUtils.md5Hex("http://licaishi.sina.com.cn/web/index?ind_id=1&fee=all&page=1"));
    	System.out.println(DigestUtils.md5Hex("http://licaishi.sina.com.cn/web/index?ind_id=1&fee=all&page=1"));
    	System.out.println(DigestUtils.md5Hex("http://licaishi.sina.com.cn/web/index?ind_id=1&fee=all&page=1"));
    	System.out.println(DigestUtils.md5Hex("http://licaishi.sina.com.cn/web/index?ind_id=1&fee=all&page=1"));
    }*/
}

