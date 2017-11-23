package testservice;

import fid.platform.core.common.pojo.commons.LabeledSentence;
import fid.platform.core.common.pojo.commons.PsgAnalysisMain;
import fid.platform.modelhelper.service.Word2VecTrainService;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

public class ServiceTest {

		@Test
		public void testdm() throws Exception {
				PropertyConfigurator.configure(ServiceTest.class.getProtectionDomain().getCodeSource().getLocation().getFile()
								.concat(File.separator).concat("properties")
								.concat(File.separator).concat("log4j.properties"));

				ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
								new String[]{"spring/applicationContext-dao.xml", "spring/applicationContext-service.xml",
												"spring/applicationContext-trans.xml"});

				Word2VecTrainService domainParserService = applicationContext.getBean(Word2VecTrainService.class);

//				LabeledSentence labeledSentence = domainParserService.trainDataToLabeledSentence(2L);
//				domainParserService.saveLabeledSentenceToLocal(2L,labeledSentence);
				domainParserService.acceptMissionIdAndTrain(2L);

		}

}
