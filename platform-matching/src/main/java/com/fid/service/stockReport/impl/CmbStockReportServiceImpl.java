package com.fid.service.stockReport.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.fid.domain.vo.CmbStockReport;
import com.fid.service.stockReport.CmbStockReportService;
import com.fid.util.DBUtil;
import com.fid.util.JsonComparator;

@Service("cmbStockReportService")
public class CmbStockReportServiceImpl implements CmbStockReportService {

	private Logger logger = LoggerFactory.getLogger(CmbStockReportServiceImpl.class);

	@Override
	public List<?> queryByContion(Map<String, Object> map) {
		int size = (Integer) map.get("size");
		String windCodes = (String) map.get("code");
		String[] codes = windCodes.split(",");
		// 原查询方案
		List<CmbStockReport> allStockReport = new ArrayList<CmbStockReport>();
		List<CmbStockReport> listReturnReport = new ArrayList<CmbStockReport>();
		List<Long> listTime = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			conn = DBUtil.getConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		// 获取股票的所有公告信息
		for (String winCode : codes) {
			try {
				String getReportByWinCode = "SELECT" + "	st.id," + "	st.title," + "	st.`code`,"
						+ "	st.wind_code windCode," + "	st.stock_cn_name stockCnName," + "	st.news_time newsTime,"
						+ "	st.publish_time publishTime," + "	ct.cut_images_oss AS smallImage" + " FROM" + "	`"
						+ winCode + "` st" + " LEFT JOIN cms.cms_topic ct ON (" + "	LOCATE(" + "		st.wind_code,"
						+ "		ct.stock_codes" + "	)" + ")" + " WHERE" + "	STATUS = 1" + " GROUP BY" + "	st.id";

				ps = conn.prepareStatement(getReportByWinCode);
				ResultSet rs = ps.executeQuery();

				while (rs.next()) {
					CmbStockReport cmb = new CmbStockReport();
					cmb.setId(rs.getLong(1));
					cmb.setTitle(rs.getString(2));
					cmb.setCode(rs.getString(3));
					cmb.setWindCode(rs.getString(4));
					cmb.setStockCnName(rs.getString(5));
					cmb.setNewsTime(rs.getTimestamp(6));
					cmb.setPublishTime(rs.getTimestamp(7));
					String a = rs.getTimestamp(7).toString();
					if (cmb.getId() != null && cmb.getTitle() != null && cmb.getWindCode() != null
							&& cmb.getNewsTime() != null && cmb.getPublishTime() != null && a.startsWith("20")) {
						allStockReport.add(cmb);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					conn.close();
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}
		// 对公告信息按时间进行排序
		Collections.sort(allStockReport, new JsonComparator());
		for (CmbStockReport cmb : allStockReport) {
			listTime.add(cmb.getPublishTime().getTime());
		}
		// 当查询的公告数量小于默认10条时
		if (allStockReport.size() > 0 && allStockReport.size() < size) {
			if (map.get("lastReportTime") == null) {
				listReturnReport.addAll(allStockReport);
			}

		} else {// 当查询的公告数量>=10条时
				// 默认第一页
			int index = 0;
			if (map.get("lastReportTime") != null) {
				long lastReportTime = (Long) map.get("lastReportTime");
				// 最后一条公告的时间
				index = listTime.indexOf(lastReportTime) + 1;
			}

			int j = index + size;
			if (j > allStockReport.size()) {
				j = allStockReport.size();
			}
			while (index < j) {
				listReturnReport.add(allStockReport.get(index));
				index++;
			}
		}

		if (listReturnReport.size() > 0) {
			for (CmbStockReport cmbStockReport : listReturnReport) {
				Map<String, Object> maps = new HashMap<String, Object>();
				maps.put("id", cmbStockReport.getId() + "," + cmbStockReport.getWindCode());
				maps.put("title", cmbStockReport.getTitle());
				maps.put("stockCode", cmbStockReport.getCode());
				maps.put("stockName", cmbStockReport.getStockCnName());
				maps.put("stockWindCode", cmbStockReport.getWindCode());
				maps.put("releaseTime", cmbStockReport.getNewsTime().getTime());
				maps.put("lastReportTime", cmbStockReport.getPublishTime().getTime());
				list.add(maps);
			}
		}
		return list;
	}

	@Override
	public Map<String, Object> queryById(String id) {
		Connection conn = null;
		PreparedStatement ps = null;
		String[] idCode = id.split(",");
		id = idCode[0];
		String windCode = idCode[1];
		Map<String, Object> rtMap = new HashMap<String, Object>();
		try {
			conn = DBUtil.getConnection();
			String sql = "select id,title,code,content,news_time newsTime,stock_cn_name stockCnName,"
					+ "wind_code windCode from `" + windCode + "` where id=" + id + " and status=1";
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				rtMap.put("id", rs.getLong("id"));
				rtMap.put("title", rs.getString("title"));
				rtMap.put("code", rs.getString("code"));
				rtMap.put("content", rs.getString("content"));
				rtMap.put("time", rs.getTimestamp("newsTime"));
				rtMap.put("name", rs.getString("stockCnName"));
				rtMap.put("windCode", rs.getString("windCode"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rtMap;
	}

	@Override
	public List<CmbStockReport> queryByContionForOut(Map<String, Object> map) {
		List<CmbStockReport> allStockReport = new ArrayList<CmbStockReport>();
		List<CmbStockReport> listReturnReport = new ArrayList<CmbStockReport>();
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			String[] codes = ((String) map.get("code")).split(",");
			conn = DBUtil.getConnection();
			// 获取股票的所有公告信息
			for (String winCode : codes) {
				String getReportByWinCode = "select id,title,code,wind_code windCode,stock_cn_name stockCnName,"
						+ "news_time newsTime,publish_time publishTime,content content from `" + winCode
						+ "` where status=1";
				ps = conn.prepareStatement(getReportByWinCode);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					CmbStockReport cmb = new CmbStockReport();
					cmb.setId(rs.getLong(1));
					cmb.setTitle(rs.getString(2));
					cmb.setCode(rs.getString(3));
					cmb.setWindCode(rs.getString(4));
					cmb.setStockCnName(rs.getString(5));
					cmb.setNewsTime(rs.getTimestamp(6));
					cmb.setPublishTime(rs.getTimestamp(7));
					cmb.setContent(rs.getString(8));
					String a = rs.getTimestamp(7).toString();
					if (cmb.getId() != null && cmb.getTitle() != null && cmb.getWindCode() != null
							&& cmb.getNewsTime() != null && cmb.getPublishTime() != null && a.startsWith("20")) {
						allStockReport.add(cmb);
					}
				}
			}
			// 对公告信息按时间进行排序
			Collections.sort(allStockReport, new JsonComparator());
			if (allStockReport.size() > 0) {
				listReturnReport.addAll(allStockReport);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				conn.close();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listReturnReport;
	}

}
