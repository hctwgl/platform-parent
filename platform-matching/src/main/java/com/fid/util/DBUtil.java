package com.fid.util;

import com.alibaba.druid.pool.DruidDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * jdbc封装
 * Created by Administrator on 2016/8/2.
 */
@SuppressWarnings("all")
public class DBUtil {
    private static String driver="com.mysql.jdbc.Driver";
    private static String url="jdbc:mysql://rm-wz9ykd02hk1w8e064o.mysql.rds.aliyuncs.com/stock_announcement?autoReconnect=true&useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull&generateSimpleParameterMetadata=true&allowMultiQueries=true";
    private static String user="fid";
    private static String pwd="fid123";
    private static DruidDataSource ds;
    // 1：加载驱动
    static{
        try {
            //加载属性文件数据
//            Properties prop=new Properties();
//            prop.load(DBUtil.class.getClassLoader().getResourceAsStream("jdbc.properties"));
//            driver=prop.getProperty("jdbc.mysql.zsCmb.driver");
//            url=prop.getProperty("jdbc.mysql.zsCmb.url");
//            user=prop.getProperty("jdbc.mysql.zsCmb.user");
//            pwd=prop.getProperty("jdbc.mysql.zsCmb.pwd");
            ds=new DruidDataSource();
            ds.setDriverClassName(driver);
            ds.setUrl(url);
            ds.setUsername(user);
            ds.setPassword(pwd);
            ds.setMaxActive(50);
            ds.setMaxIdle(3);
            ds.setMaxWait(30000);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读取属性文件错误！",e);
        }
    }
    //创建连接
    public static Connection getConnection() throws SQLException{
        return ds.getConnection();
    }
    // 3：归还连接
    public static void close(Connection conn){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("归还连接错误！",e);
            }
        }
    }
    //测试
    public static void main(String[] args) throws SQLException {
        Connection conn=getConnection();
        System.out.println(conn.getClass().getName());
        close(conn);
    }

}
