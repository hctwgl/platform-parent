package com.fid.common.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fid.domain.RobotMatchKeywords;
import com.fid.domain.RobotMatchKwRelation;
import com.fid.domain.RobotMatchWords;
import com.fid.domain.RobotNlpTags;
import com.fid.util.DBHelper;
import com.fid.util.RedisEntityUtil;
/**
 * 插入数据到Redis中
 * @description:
 * 
 */
public class DataToRedis {
	private transient final Logger logger = LoggerFactory.getLogger(this.getClass());
	static String sql = null;  
    static DBHelper db1 = null;  
    static ResultSet ret = null;  
  
    public static List<RobotMatchWords> getAllRbsWords(){
    	List<RobotMatchWords>  words = new ArrayList<>();
        sql = "select * from robot_match_words";//SQL语句  
        
        db1 = new DBHelper(sql);//创建DBHelper对象  
        try {  
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {  
            	RobotMatchWords robotMatchWords = new RobotMatchWords();
            	robotMatchWords.setId(ret.getLong("id"));
            	robotMatchWords.setName(ret.getString("name"));
            	robotMatchWords.setMissionid(ret.getInt("missionid"));
            	words.add(robotMatchWords);
            }//显示数据  
            ret.close();  
            db1.close();//关闭连接  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return words;
    }
    
    public  static List<RobotMatchKeywords> getAllRbsKeyWords(){
    	List<RobotMatchKeywords>  list = new ArrayList<>();
    	sql = "select * from robot_match_keywords";
        db1 = new DBHelper(sql);//创建DBHelper对象  
        try {  
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {  
            	RobotMatchKeywords robotMatchKeywords = new RobotMatchKeywords();
            	robotMatchKeywords.setId(ret.getLong("id"));
            	robotMatchKeywords.setName(ret.getString("name"));
            	robotMatchKeywords.settId(ret.getLong("t_id"));
            	robotMatchKeywords.setMissionid(ret.getInt("missionid"));
            	list.add(robotMatchKeywords);
            }//显示数据  
            ret.close();  
            db1.close();//关闭连接  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return list;
    }
    
    public static  List<RobotMatchKwRelation> getAllRbsKwRelations(){
    	List<RobotMatchKwRelation>  list = new ArrayList<>();
        sql = "select * from robot_match_kw_relation";//SQL语句  
        db1 = new DBHelper(sql);//创建DBHelper对象  
        try {  
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {  
            	RobotMatchKwRelation dto = new RobotMatchKwRelation();
            	dto.setId(ret.getLong("id"));
            	dto.setkId(ret.getLong("k_id"));
            	dto.setwId(ret.getLong("w_id"));
            	dto.setMissionid(ret.getInt("missionid"));
            	list.add(dto);
            }//显示数据  
            ret.close();  
            db1.close();//关闭连接  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return list;
    }
    
    
    public static List<RobotNlpTags> getAllRbsConclusionTags(){
    	List<RobotNlpTags>  list = new ArrayList<>();
        sql = "select * from robot_nlp_tags";//SQL语句  
        db1 = new DBHelper(sql);//创建DBHelper对象  
        try {  
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {  
            	RobotNlpTags dto = new RobotNlpTags();
            	dto.setId(ret.getLong("id"));
            	dto.setName(ret.getString("name"));
            	dto.setPid(ret.getLong("pid"));
            	dto.setMissionid(ret.getLong("missionid"));
            	list.add(dto);
            }//显示数据  
            ret.close();  
            db1.close();//关闭连接  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return list;
    }
    
    
    
    public static void insert(){
    	long beginTime = new Date().getTime();
    	{
    		List<RobotMatchWords> list = getAllRbsWords();
    		RedisEntityUtil.insertEntityToRedisListAdmin(list);
    		System.out.println("============1========="+list.size());
    	}
    	{
    		List<RobotMatchKeywords> list = getAllRbsKeyWords();
    		RedisEntityUtil.insertEntityToRedisListAdmin(list);
    		System.out.println("============2========="+list.size());
    	}
    	{
    		List<RobotMatchKwRelation> list = getAllRbsKwRelations();
    		RedisEntityUtil.insertEntityToRedisListAdmin(list);
    		System.out.println("============3========="+list.size());
    	}
    	{
    		List<RobotNlpTags> list = getAllRbsConclusionTags();
    		RedisEntityUtil.insertEntityToRedisListAdmin(list);
    		System.out.println("============4========="+list.size());
    	}
    	
    	long endTime = new Date().getTime();
    	System.out.println("总共耗时秒数为:"+(endTime - beginTime)/1000+"秒");
    }
    
    public static void main(String[] args) throws SQLException {
    	{
    		List<RobotMatchWords> list = getAllRbsWords();
    		RedisEntityUtil.insertEntityToRedisListAdmin(list);
    		System.out.println("============1========="+list.size());
    	}
    	{
    		List<RobotMatchKeywords> list = getAllRbsKeyWords();
    		RedisEntityUtil.insertEntityToRedisListAdmin(list);
    		System.out.println("============2========="+list.size());
    	}
    	{
    		List<RobotMatchKwRelation> list = getAllRbsKwRelations();
    		RedisEntityUtil.insertEntityToRedisListAdmin(list);
    		System.out.println("============3========="+list.size());
    	}
    	{
    		List<RobotNlpTags> list = getAllRbsConclusionTags();
    		RedisEntityUtil.insertEntityToRedisListAdmin(list);
    		System.out.println("============4========="+list.size());
    	}
    	System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
        
    }
}
