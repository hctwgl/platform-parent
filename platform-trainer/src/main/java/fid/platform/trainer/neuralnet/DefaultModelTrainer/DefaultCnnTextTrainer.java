package fid.platform.trainer.neuralnet.DefaultModelTrainer;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultCnnTextTrainer {

		private static Logger logger = LoggerFactory.getLogger(DefaultCnnTextTrainer.class);

		@Parameter(names = {"--outputPath","-op"})
		private String outputPath;
		@Parameter(names = {"--dataPath","-dp"})
		private String dataPath;
		@Parameter(names = {"--vecDicPath","-vdp"})
		private String vecDicPath;

		@Parameter(names = {"--nOut","-no"})
		private int nOut;
		@Parameter(names = {"--learningRate","-lr"})
		private double learningRate;
		@Parameter(names = {"--dropOut","-dp"})
		private double dropOut;


		public static void main(String[] args) {
				try {
						new DefaultCnnTextTrainer().entryPoint(args);
				} catch (Exception e) {
						e.printStackTrace();
				}
		}

		public void entryPoint(String[] args) throws Exception{
				JCommander jcmd = new JCommander(this);
				jcmd.parse();
				jcmd.usage();



		}

}
