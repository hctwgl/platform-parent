package fid.platform.core.common.commonutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通用组合工具类,提供以间隔频率获取所有组合的方法(但转化为符合R的data.frame格式的转化前MapList)
 * @author Drx
 *
 */
public class FrequencyParamGenerator {


	/**
	 * 循环实现获取多个list的笛卡尔积
	 * @param checkList
	 * @return
	 */
	public static List<List<Object>> getDimVal(List<List<Object>> checkList){
		List<List<Object>> resultList = new ArrayList<List<Object>>();
		//checkList转String(可适应多种情况)
		List<List<String>> dimValue = new ArrayList<List<String>>();
		for (List<Object> list : checkList) {
			List<String> tempList = new ArrayList<String>();
			for (Object object : list) {
				tempList.add(object.toString());
			}
			dimValue.add(tempList);
		}
		
		int total = 1;  
        for (List<String> list : dimValue) {  
            total *= list.size();  
        }  
        String[] myResult = new String[total];  
      
        int itemLoopNum = 1;  
        int loopPerItem = 1;  
        int now = 1;  
        for (List<String> list : dimValue) {  
            now *= list.size();  
  
            int index = 0;  
            int currentSize = list.size();  
  
            itemLoopNum = total / now;  
            loopPerItem = total / (itemLoopNum * currentSize);  
            int myIndex = 0;  
  
            for (String string : list) {  
                for (int i = 0; i < loopPerItem; i++) {  
                    if (myIndex == list.size()) {  
                        myIndex = 0;  
                    }  
  
                    for (int j = 0; j < itemLoopNum; j++) {  
                        myResult[index] = (myResult[index] == null? "" : myResult[index] + ",") + list.get(myIndex);  
                        index++;  
                    }  
                    myIndex++;  
                }  
  
            }  
        }  
        //封装字符串组合至通用
        List<String> stringResult = Arrays.asList(myResult); 
        for (String string : stringResult) {  
        	List<Object> addList = new ArrayList<Object>();
            String[] stringArray = string.split(",");  
            List<String> asList = Arrays.asList(stringArray);
            for (String str : asList) {
            	addList.add(str);
			}
            resultList.add(addList);  
        }
		return resultList;
	}

}
