package fid.platform.modelhelper.thread.Runnable;

import fid.platform.modelhelper.thread.ThreadPoolExecutor;
import fid.platform.modelhelper.service.Word2VecTrainService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LabeledSentenceThreadContainer {

		@Resource
		private Word2VecTrainService word2VecTrainService;


		public void backgroundSaveWord2vecData(Long missionId,int avgNum,int truncatNum){
				Word2vecSavingThread thread = new Word2vecSavingThread();
				thread.missionId = missionId;
				thread.avgNum = avgNum;
				thread.truncatNum = truncatNum;
				ThreadPoolExecutor.getPool().submit(thread);
		}

		class Word2vecSavingThread implements Runnable{

				private Long missionId;
				private int avgNum;
				private int truncatNum;

				@Override
				public void run() {
						try {
								word2VecTrainService.trainDataToLabeledSentence(this.missionId,this.avgNum,this.truncatNum);
						} catch (Exception e) {
								e.printStackTrace();
						}
				}
		}


}
