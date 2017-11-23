package com.fid.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fid.domain.vo.CmbStockReport;
import com.fid.service.stockReport.CmbStockReportService;

//@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class StockReportTest {
	
	@Autowired
	private CmbStockReportService cmbStockReportService;
	
	@Autowired
	private DealContentForTagsService dealContentForTagsService;
	
//	@Test
	public void test() {
		Map<String, Object> map = new HashMap<>();
		map.put("code", "000001.SZ");
		List<CmbStockReport> queryByContionForOut = cmbStockReportService.queryByContionForOut(map);
		dealContentForTagsService.dealMessageToTags(queryByContionForOut);
	}

}
