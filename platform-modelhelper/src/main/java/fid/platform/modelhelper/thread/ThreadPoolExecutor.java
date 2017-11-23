package fid.platform.modelhelper.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolExecutor {

		public static final int RUNNABLE_MAX_THREAD = 10;

		private static ExecutorService pool = Executors.newFixedThreadPool(RUNNABLE_MAX_THREAD);

		public static ExecutorService getPool(){
				return pool;
		}

}
