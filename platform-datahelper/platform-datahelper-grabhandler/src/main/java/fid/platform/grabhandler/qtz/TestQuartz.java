package fid.platform.grabhandler.qtz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



public class TestQuartz {

	private static Logger logger = LoggerFactory.getLogger(TestQuartz.class);
	public void doTest(){
		logger.info("hello quartz !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("hello quartz !!");
	}
}
