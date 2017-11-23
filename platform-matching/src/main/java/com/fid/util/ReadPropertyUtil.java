package com.fid.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ReadPropertyUtil {
	
	public static final String Config = "config/config.properties";
	

    public static  String getPropertyByName(String path, String name) {
    	String result = null;
    	InputStream in = ReadPropertyUtil.class.getClassLoader().getResourceAsStream(path);
    	Properties prop = new Properties();
    	try {
			prop.load(new InputStreamReader(in, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(in != null){
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	result = prop.getProperty(name);
        return result;    
    } 
    
    public static String getPropertyByName(String name){
    	return getPropertyByName(Config,name);
    }
    
    public static void main(String[] args) {
		String value = getPropertyByName(ReadPropertyUtil.Config, "fid.tags.matching.url");
		System.out.println(value);
	}
}
