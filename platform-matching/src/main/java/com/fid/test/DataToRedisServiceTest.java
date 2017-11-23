package com.fid.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fid.common.data.DataToRedisOther;


//@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class DataToRedisServiceTest{


	@Autowired
	private DataToRedisOther dataToRedisOther;

//    @Test
	public void dataToRedis() {
    	dataToRedisOther.insert();
	}
	
	//把一个字符串中的大写转为小写，小写转换为大写：思路1
	public static String exChange(String str){
	    StringBuffer sb = new StringBuffer();
	    if(str!=null){
	        for(int i=0;i<str.length();i++){
	            char c = str.charAt(i);
	            if(Character.isUpperCase(c)){
	                sb.append(Character.toLowerCase(c));
	            }else if(Character.isLowerCase(c)){
	                sb.append(Character.toUpperCase(c)); 
	            }
	        }
	    }
	     
	    return sb.toString();
	}
	
	
	//把一个字符串中的大写转为小写，小写转换为大写：思路2
	public static String exChange2(String str){
	    for(int i=0;i<str.length();i++){
	        //如果是小写
	        if(str.substring(i, i+1).equals(str.substring(i, i+1).toLowerCase())){
	            str.substring(i, i+1).toUpperCase();
	        }
	    }
	    return str;
	}
	 

}
