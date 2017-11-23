package com.fid.service.stockReport;

import java.util.List;
import java.util.Map;

import com.fid.domain.vo.CmbStockReport;


public interface CmbStockReportService {
	
	/**
	 * 根据条件查询
	 */
	public List<?> queryByContion(Map<String,Object> map);
	
	
	/**
	 * 根据id查询详情
	 */
	public Map<String,Object> queryById(String id);
	
	/**
	 * 公告接口
	 */
	public List<CmbStockReport> queryByContionForOut(Map<String,Object> map);
	
}
