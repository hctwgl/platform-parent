package com.fid;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fid.service.match.ResultService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ResultMatchTest {
	
	@Autowired
	private ResultService resultService;
	
	@Test
	public void testMatch() {
		String contentData = "公司搬迁";
		int dataType = 1;
		resultService.getAllResult(contentData, dataType);
	}

}
