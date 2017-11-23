package fid.platform.core.common.commonutil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ReadPropertyUtil {
	
	public static final String ROBOT_ENV = "properties/redis.properties";
	

    public static  String getPropertyByName(String path, String name) {
    	String result = null;
    	InputStream in = ReadPropertyUtil.class.getClassLoader().getResourceAsStream(path);
    	Properties prop = new Properties();
    	try {
			prop.load(new InputStreamReader(in, "UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(in != null){
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	result = prop.getProperty(name);
        return result;    
    } 
    
    public static String getPropertyByName(String name){
    	return getPropertyByName(ROBOT_ENV,name);
    }
    
    public static void main(String[] args) {
		String value = getPropertyByName(ROBOT_ENV, "reids_master_addr");
		String value2 = getPropertyByName(ROBOT_ENV, "reids_master_password");
		System.out.println(value);
		System.out.println(value2);
    	System.out.println(ReadPropertyUtil.class.getClassLoader().getResource("properties/redis.properties").getFile().length());
	}
}
