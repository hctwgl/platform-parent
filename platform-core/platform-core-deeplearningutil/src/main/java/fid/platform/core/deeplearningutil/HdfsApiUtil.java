package fid.platform.core.deeplearningutil;

import fid.platform.core.common.commonutil.YamlConfigUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URI;

public class HdfsApiUtil {

    private static Logger logger = LoggerFactory.getLogger(HdfsApiUtil.class);

    private static String HADOOP_URI = "";

    static {
        HADOOP_URI = YamlConfigUtil.getInstance().getConfig("hdfs.uri");
    }

    public static FileSystem getHdFileSystem() {
        Configuration conf = new Configuration();
        FileSystem fs = null;
        logger.info("正在从{}获取文件系统...",HADOOP_URI);
        try {
            if(StringUtils.isEmpty(HADOOP_URI)){
                logger.info("hadoop uri is not set");
                return null;
            }else {
                URI uri = new URI(HADOOP_URI.trim());
                fs = FileSystem.get(uri,conf);
            }
        }catch (Exception e){
            logger.error("获取hadoop文件系统失败",e);
        }
        return fs;
    }

    /**
     * 创建文件夹path从根目录开始写
     * @param path
     */
    public static String mkdir(String path){
        FileSystem fs = getHdFileSystem();
        logger.info("正在创建hadoop文件夹{}",path);
        try {
            if (StringUtils.isEmpty(path)){
                logger.error("path for your creation is not set.");
                return null;
            }else {
                fs.mkdirs(new Path(path.trim()));
                fs.close();
            }
        } catch (IOException e) {
            logger.error("创建hadoop文件夹失败",e);
        }
        return path;
    }

    public static String deleteFileOrDir(String path){
        FileSystem fs = getHdFileSystem();
        logger.info("正在删除hadoop文件夹{}",path);
        try {
            if (StringUtils.isEmpty(path)){
                logger.error("path for your creation is not set.");
                return null;
            }else {
                fs.delete(new Path(path), true);
                fs.close();
            }
        } catch (IOException e) {
            logger.error("删除hadoop文件夹失败",e);
        }
        return path;
    }

    public static String rename(String oldPath,String newPath){
        FileSystem fs = getHdFileSystem();
        logger.info("正在重命名hadoop文件夹{}",HADOOP_URI+oldPath);
        try {
            if (StringUtils.isEmpty(oldPath) || StringUtils.isEmpty(newPath)){
                logger.error("path or renamePath for your dir is not set.");
                return null;
            }else {
                fs.rename(new Path(HADOOP_URI + oldPath), new Path(HADOOP_URI + newPath));
                fs.close();
            }
        } catch (IOException e) {
            logger.error("重命名hadoop文件夹失败",e);
        }
        return newPath;
    }

    /**
     * 对于绝对路径下文件是否存在的测试
     * @param path
     * @return
     */
    public static boolean isExist(String path){
        FileSystem fs = getHdFileSystem();
        boolean exists = false;
        logger.info("正在判断hadoop文件夹{}是否存在",HADOOP_URI+path);
        try {
            if (StringUtils.isEmpty(path)){
                logger.error("path is not set.");
                return false;
            }else {
                exists = fs.exists(new Path(HADOOP_URI + path));
                fs.close();
            }
        } catch (IOException e) {
            logger.error("判断是否存在hadoop文件夹失败",e);
        }
        return exists;
    }

    public static String writeStringToFile(String string,String path,boolean overwrite){
        FileSystem fs = getHdFileSystem();
        byte[] bytes = string.getBytes();
        logger.info("开始写入hadoop文件{}",HADOOP_URI+path);
        try {
            FSDataOutputStream fsDataOutputStream = fs.create(new Path(path),overwrite);
            fsDataOutputStream.write(bytes,0,bytes.length);
        } catch (IOException e) {
            logger.error("创建hadoop文件或写入失败,可能由于文件已存在却未定义重写文件",e);
        }
        return path;
    }

    public static String uploadFile(String localPath,String hdfsPathAndName,boolean overwrite){
        FileSystem fs = getHdFileSystem();
        try {
            FileInputStream fis = new FileInputStream(localPath);
            OutputStream fsDataOutputStream = fs.create(new Path(hdfsPathAndName), overwrite);
            IOUtils.copyBytes(fis,fsDataOutputStream,1024,true);
        } catch (IOException e) {
            logger.error("创建hadoop文件或上传失败,可能由于文件已存在却未定义重写文件",e);
        }
        return hdfsPathAndName;
    }

    public static String downLoadFile(String hdfsPath,String localPath){
        FileSystem fs = getHdFileSystem();
        try {
            FSDataInputStream fis = fs.open(new Path(hdfsPath));
            OutputStream os = new FileOutputStream(localPath);
            IOUtils.copyBytes(fis,os,1024,true);
        } catch (IOException e) {
            logger.error("下载文件失败",e);
        }
        return localPath;
    }

}
