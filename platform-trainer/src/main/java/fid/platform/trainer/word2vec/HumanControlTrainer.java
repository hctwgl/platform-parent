package fid.platform.trainer.word2vec;

import com.clearspring.analytics.util.Lists;
import fid.platform.core.deeplearningutil.Word2VecUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

public class HumanControlTrainer {

		private static Logger logger = LoggerFactory.getLogger(HumanControlTrainer.class);

		public static void main(String[] args) {
				File f2 = new File("/home/data/robot_anounce_train_data.txt");
				List<File> files = Lists.newArrayList();
				files.add(f2);
				Word2VecUtils
								.newWord2Vec()
								.addAllTextFile(files)
//                .addSentences(contentStrList)
								.charset(Charset.forName("UTF-8"))
								.setIterations(1)
								.setLayerSize(100)
								.saveAt("/home/model/vec/resultAnoce.bin", true)
								.build();
				System.out.println("build finished");
		}
}
