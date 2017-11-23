package testService;

import fid.platform.api.service.DomainRecongnize.DomainParserService;
import fid.platform.core.common.pojo.commons.PsgAnalysisMain;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.Set;

public class testDomain {

		@Test
		public void testdm(){
				PropertyConfigurator.configure(testDomain.class.getProtectionDomain().getCodeSource().getLocation().getFile()
								.concat(File.separator).concat("properties")
								.concat(File.separator).concat("log4j.properties"));

				ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
								new String[]{"spring/applicationContext-dao.xml", "spring/applicationContext-service.xml",
												"spring/applicationContext-trans.xml"});

				DomainParserService domainParserService = applicationContext.getBean(DomainParserService.class);

				PsgAnalysisMain psgDomain = domainParserService.getPsgDomain("苏宁云商");


		}

}
