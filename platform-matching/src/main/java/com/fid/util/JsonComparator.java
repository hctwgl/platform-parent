package com.fid.util;

import java.util.Comparator;
import java.util.Date;

import com.fid.domain.vo.CmbStockReport;



/**
 * 根据日期对json数组排序
 * 
 * @author Administrator
 *
 */
public class JsonComparator implements Comparator<CmbStockReport> {

	@Override
	public int compare(CmbStockReport c1, CmbStockReport c2) {
		try {
			Date date1 = c1.getPublishTime();
			Date date2 = c2.getPublishTime();
			if (date1.getTime() < date2.getTime()) {
				return 1;
			} else if (date1.getTime() > date2.getTime()) {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
