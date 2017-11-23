package com.fid.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fid.common.param.RbsCommonParams;

  
public class DBHelper {  
//    public static final String url = "jdbc:mysql://rm-wz9ykd02hk1w8e064o.mysql.rds.aliyuncs.com/cms";  
//    public static final String name = "com.mysql.jdbc.Driver";  
//    public static final String user = "fid";  
//    public static final String password = "fid123"; 
//	
	public static  String url;
    public static  String name;  
    public static  String user;  
    public static  String password; 
    static{
    	url = ReadPropertyUtil.getPropertyByName(ReadPropertyUtil.Config, "fid.tags.matching.url");
    	name = "com.mysql.jdbc.Driver";
    	user = ReadPropertyUtil.getPropertyByName(ReadPropertyUtil.Config, "fid.tags.matching.username");
    	password = ReadPropertyUtil.getPropertyByName(ReadPropertyUtil.Config, "fid.tags.matching.password");
    }
    public Connection conn = null;  
    public PreparedStatement pst = null;  
  
    public DBHelper(){
    	
    }
    public DBHelper(String sql) {  
        try {  
            Class.forName(name);//指定连接类型  
            conn = DriverManager.getConnection(url, user, password);//获取连接  
            pst = conn.prepareStatement(sql);//准备执行语句  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    
    public  void excuteBatch(String sqlNodel,List<String> paramList,int pageSize){
    	int countPage = paramList.size() / pageSize;
    	for (int i = 0; i < countPage; i++) {
    		System.out.println("批量执行语句:"+(countPage+1)*pageSize+"条");
			String sql = sqlNodel + String.join(RbsCommonParams.WORD_SPLIT, paramList.subList(i*pageSize, (i+1)*pageSize));
//			sql = sql.replace(RbsCommonParams.REGEX, RbsCommonParams.REGEX_DB);
			this.excute(sql);
		}
    	int others =  paramList.size() % pageSize;
    	if(others > 0 ){
    		String sql = sqlNodel + String.join(RbsCommonParams.WORD_SPLIT, paramList.subList(countPage*pageSize, paramList.size()));
//    		sql = sql.replace(RbsCommonParams.REGEX, RbsCommonParams.REGEX_DB);
			this.excute(sql);
    	}
    }
    
    
    public synchronized long selectMaxId(String tableName){
    	Connection  conn =  this.getConnection();
		PreparedStatement preparedStatement = null;
		try{
			 String sql = "select max(id) from "+tableName;
			 preparedStatement = conn.prepareStatement(sql);
			 ResultSet resultSet = preparedStatement.executeQuery();
			 while(resultSet.next()){
				 return resultSet.getLong(1);
			 }
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(preparedStatement != null){
				try {
					preparedStatement.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		return 0;
    }
    
    public synchronized void excute(String sql){
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
    	Connection  conn =  this.getConnection();
		PreparedStatement preparedStatement = null;
		try{
			 preparedStatement = conn.prepareStatement(sql);
			 preparedStatement.execute();
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(preparedStatement != null){
				try {
					preparedStatement.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
    }
    public Connection getConnection(){
    	  try {  
              Class.forName(name);//指定连接类型  
              conn = DriverManager.getConnection(url, user, password);//获取连接  
          } catch (Exception e) {  
              e.printStackTrace();  
          }  
    	  return conn;
    }
  
    public void close() {  
        try {  
            this.conn.close();  
            this.pst.close();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
    
    public static void main(String[] args) {
    	System.out.println(new DBHelper().selectMaxId("rbs_tags"));
	}
}  