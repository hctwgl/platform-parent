package com.fid.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fid.domain.vo.CmbStockReport;
import com.fid.domain.vo.StockVo;
import com.fid.service.stockReport.CmbStockReportService;
import com.fid.test.DealContentForTagsService;
import com.fid.util.CrawlerUtil;

@Component("stockReportTitleTagsToDbTask")
public class StockReportTitleTagsToDbTask {

	private final static Logger logger = LoggerFactory.getLogger(StockReportTitleTagsToDbTask.class);

	@Autowired
	private CmbStockReportService cmbStockReportService;
	
	@Autowired
	private DealContentForTagsService dealContentForTagsService;

	// @Scheduled(cron = "0 0 19 * * ?")
	public void dealStockReportTitleTags() {
		System.out.println("进入程序-------------------------");
		final List<List<StockVo>> resultStockList = getAllListContent();
		// 线程
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				List<StockVo> ListCmsContentVo = null;
				if (resultStockList.size() > 0) {
					try {
						System.out.println("========线程【" + Thread.currentThread().getName() + "】开启========");
						// 得到个股资讯
						ListCmsContentVo = resultStockList.get(0);
						Map<String, Object> param = new HashMap<>();
						for (StockVo cmsContentVo : ListCmsContentVo) {
							String stockCode = cmsContentVo.getStockWindCode();
							param.put("code", stockCode);
							List<CmbStockReport> queryByContionForOut = cmbStockReportService.queryByContionForOut(param);
							if (queryByContionForOut != null && queryByContionForOut.size() > 0) {
								//处理
								dealContentForTagsService.dealMessageToTags(queryByContionForOut);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						ListCmsContentVo.clear();
						System.out.println("========线程【" + Thread.currentThread().getName() + "】结束========");
					}
				}
			}
		});
		t1.start();

		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				List<StockVo> ListCmsContentVo = null;
				if (resultStockList.size() > 1) {
					try {
						System.out.println("========线程【" + Thread.currentThread().getName() + "】开启========");
						// 得到个股资讯
						ListCmsContentVo = resultStockList.get(1);
						Map<String, Object> param = new HashMap<>();
						for (StockVo cmsContentVo : ListCmsContentVo) {
							String stockCode = cmsContentVo.getStockWindCode();
							param.put("code", stockCode);
							List<CmbStockReport> queryByContionForOut = cmbStockReportService.queryByContionForOut(param);
							if (queryByContionForOut != null && queryByContionForOut.size() > 0) {
								//处理
								dealContentForTagsService.dealMessageToTags(queryByContionForOut);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						ListCmsContentVo.clear();
						System.out.println("========线程【" + Thread.currentThread().getName() + "】结束========");
					}
				}
			}
		});
		t2.start();

		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() {
				List<StockVo> ListCmsContentVo = null;
				if (resultStockList.size() > 2) {
					try {
						System.out.println("========线程【" + Thread.currentThread().getName() + "】开启========");
						// 得到个股资讯
						ListCmsContentVo = resultStockList.get(2);
						Map<String, Object> param = new HashMap<>();
						for (StockVo cmsContentVo : ListCmsContentVo) {
							String stockCode = cmsContentVo.getStockWindCode();
							param.put("code", stockCode);
							List<CmbStockReport> queryByContionForOut = cmbStockReportService.queryByContionForOut(param);
							if (queryByContionForOut != null && queryByContionForOut.size() > 0) {
								//处理
								dealContentForTagsService.dealMessageToTags(queryByContionForOut);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						ListCmsContentVo.clear();
						System.out.println("========线程【" + Thread.currentThread().getName() + "】结束========");
					}
				}
			}
		});
		t3.start();

		Thread t4 = new Thread(new Runnable() {
			@Override
			public void run() {
				List<StockVo> ListCmsContentVo = null;
				if (resultStockList.size() > 3) {
					try {
						System.out.println("========线程【" + Thread.currentThread().getName() + "】开启========");
						// 得到个股资讯
						ListCmsContentVo = resultStockList.get(3);
						Map<String, Object> param = new HashMap<>();
						for (StockVo cmsContentVo : ListCmsContentVo) {
							String stockCode = cmsContentVo.getStockWindCode();
							param.put("code", stockCode);
							List<CmbStockReport> queryByContionForOut = cmbStockReportService.queryByContionForOut(param);
							if (queryByContionForOut != null && queryByContionForOut.size() > 0) {
								//处理
								dealContentForTagsService.dealMessageToTags(queryByContionForOut);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						ListCmsContentVo.clear();
						System.out.println("========线程【" + Thread.currentThread().getName() + "】结束========");
					}
				}
			}
		});
		t4.start();

		Thread t5 = new Thread(new Runnable() {
			@Override
			public void run() {
				List<StockVo> ListCmsContentVo = null;
				if (resultStockList.size() > 4) {
					try {
						System.out.println("========线程【" + Thread.currentThread().getName() + "】开启========");
						// 得到个股资讯
						ListCmsContentVo = resultStockList.get(4);
						Map<String, Object> param = new HashMap<>();
						for (StockVo cmsContentVo : ListCmsContentVo) {
							String stockCode = cmsContentVo.getStockWindCode();
							param.put("code", stockCode);
							List<CmbStockReport> queryByContionForOut = cmbStockReportService.queryByContionForOut(param);
							if (queryByContionForOut != null && queryByContionForOut.size() > 0) {
								//处理
								dealContentForTagsService.dealMessageToTags(queryByContionForOut);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						ListCmsContentVo.clear();
						System.out.println("========线程【" + Thread.currentThread().getName() + "】结束========");
					}
				}
			}
		});
		t5.start();

		Thread t6 = new Thread(new Runnable() {
			@Override
			public void run() {
				List<StockVo> ListCmsContentVo = null;
				if (resultStockList.size() > 5) {
					try {
						System.out.println("========线程【" + Thread.currentThread().getName() + "】开启========");
						// 得到个股资讯
						ListCmsContentVo = resultStockList.get(5);
						Map<String, Object> param = new HashMap<>();
						for (StockVo cmsContentVo : ListCmsContentVo) {
							String stockCode = cmsContentVo.getStockWindCode();
							param.put("code", stockCode);
							List<CmbStockReport> queryByContionForOut = cmbStockReportService.queryByContionForOut(param);
							if (queryByContionForOut != null && queryByContionForOut.size() > 0) {
								//处理
								dealContentForTagsService.dealMessageToTags(queryByContionForOut);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						ListCmsContentVo.clear();
						System.out.println("========线程【" + Thread.currentThread().getName() + "】结束========");
					}
				}
			}
		});
		t6.start();

		Thread t7 = new Thread(new Runnable() {
			@Override
			public void run() {
				List<StockVo> ListCmsContentVo = null;
				if (resultStockList.size() > 6) {
					try {
						System.out.println("========线程【" + Thread.currentThread().getName() + "】开启========");
						// 得到个股资讯
						ListCmsContentVo = resultStockList.get(6);
						Map<String, Object> param = new HashMap<>();
						for (StockVo cmsContentVo : ListCmsContentVo) {
							String stockCode = cmsContentVo.getStockWindCode();
							param.put("code", stockCode);
							List<CmbStockReport> queryByContionForOut = cmbStockReportService.queryByContionForOut(param);
							if (queryByContionForOut != null && queryByContionForOut.size() > 0) {
								//处理
								dealContentForTagsService.dealMessageToTags(queryByContionForOut);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						ListCmsContentVo.clear();
						System.out.println("========线程【" + Thread.currentThread().getName() + "】结束========");
					}
				}
			}
		});
		t7.start();

		Thread t8 = new Thread(new Runnable() {
			@Override
			public void run() {
				List<StockVo> ListCmsContentVo = null;
				if (resultStockList.size() > 7) {
					try {
						System.out.println("========线程【" + Thread.currentThread().getName() + "】开启========");
						// 得到个股资讯
						ListCmsContentVo = resultStockList.get(7);
						Map<String, Object> param = new HashMap<>();
						for (StockVo cmsContentVo : ListCmsContentVo) {
							String stockCode = cmsContentVo.getStockWindCode();
							param.put("code", stockCode);
							List<CmbStockReport> queryByContionForOut = cmbStockReportService.queryByContionForOut(param);
							if (queryByContionForOut != null && queryByContionForOut.size() > 0) {
								//处理
								dealContentForTagsService.dealMessageToTags(queryByContionForOut);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						ListCmsContentVo.clear();
						System.out.println("========线程【" + Thread.currentThread().getName() + "】结束========");
					}
				}
			}
		});
		t8.start();

		Thread t9 = new Thread(new Runnable() {
			@Override
			public void run() {
				List<StockVo> ListCmsContentVo = null;
				if (resultStockList.size() > 8) {
					try {
						System.out.println("========线程【" + Thread.currentThread().getName() + "】开启========");
						// 得到个股资讯
						ListCmsContentVo = resultStockList.get(8);
						Map<String, Object> param = new HashMap<>();
						for (StockVo cmsContentVo : ListCmsContentVo) {
							String stockCode = cmsContentVo.getStockWindCode();
							param.put("code", stockCode);
							List<CmbStockReport> queryByContionForOut = cmbStockReportService.queryByContionForOut(param);
							if (queryByContionForOut != null && queryByContionForOut.size() > 0) {
								//处理
								dealContentForTagsService.dealMessageToTags(queryByContionForOut);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						ListCmsContentVo.clear();
						System.out.println("========线程【" + Thread.currentThread().getName() + "】结束========");
					}
				}
			}
		});
		t9.start();

		Thread t10 = new Thread(new Runnable() {
			@Override
			public void run() {
				List<StockVo> ListCmsContentVo = null;
				if (resultStockList.size() > 9) {
					try {
						System.out.println("========线程【" + Thread.currentThread().getName() + "】开启========");
						// 得到个股资讯
						ListCmsContentVo = resultStockList.get(9);
						Map<String, Object> param = new HashMap<>();
						for (StockVo cmsContentVo : ListCmsContentVo) {
							String stockCode = cmsContentVo.getStockWindCode();
							param.put("code", stockCode);
							List<CmbStockReport> queryByContionForOut = cmbStockReportService.queryByContionForOut(param);
							if (queryByContionForOut != null && queryByContionForOut.size() > 0) {
								//处理
								dealContentForTagsService.dealMessageToTags(queryByContionForOut);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						ListCmsContentVo.clear();
						System.out.println("========线程【" + Thread.currentThread().getName() + "】结束========");
					}
				}
			}
		});
		t10.start();

		Thread t11 = new Thread(new Runnable() {
			@Override
			public void run() {
				List<StockVo> ListCmsContentVo = null;
				if (resultStockList.size() > 10) {
					try {
						System.out.println("========线程【" + Thread.currentThread().getName() + "】开启========");
						// 得到个股资讯
						ListCmsContentVo = resultStockList.get(10);
						Map<String, Object> param = new HashMap<>();
						for (StockVo cmsContentVo : ListCmsContentVo) {
							String stockCode = cmsContentVo.getStockWindCode();
							param.put("code", stockCode);
							List<CmbStockReport> queryByContionForOut = cmbStockReportService.queryByContionForOut(param);
							if (queryByContionForOut != null && queryByContionForOut.size() > 0) {
								//处理
								dealContentForTagsService.dealMessageToTags(queryByContionForOut);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						ListCmsContentVo.clear();
						System.out.println("========线程【" + Thread.currentThread().getName() + "】结束========");
					}
				}
			}
		});
		t11.start();

		try {
			t1.join();
			t2.join();
			t3.join();
			t4.join();
			t5.join();
			t6.join();
			t7.join();
			t8.join();
			t9.join();
			t10.join();
			t11.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		resultStockList.clear();
		logger.info("========资讯打标签完毕============");
	}

	/**
	 * 切分list数据
	 * 
	 * @param targe
	 * @param size
	 * @return
	 */
	private static List<List<StockVo>> createList(List<StockVo> targe, int size) {
		List<List<StockVo>> listArr = new ArrayList<List<StockVo>>();
		// 获取被拆分的数组个数
		int arrSize = targe.size() % size == 0 ? targe.size() / size : targe.size() / size + 1;
		for (int i = 0; i < arrSize; i++) {
			List<StockVo> sub = new ArrayList<StockVo>();
			// 把指定索引数据放入到list中
			for (int j = i * size; j <= size * (i + 1) - 1; j++) {
				if (j <= targe.size() - 1) {
					sub.add(targe.get(j));
				}
			}
			listArr.add(sub);
		}
		return listArr;
	}

	/**
	 * 获取个股list数据
	 * 
	 * @return
	 */
	private List<List<StockVo>> getAllListContent() {
		List<List<StockVo>> resultList = new ArrayList<>();
		// 得到所有的个股列表
		List<StockVo> stockListInfo = CrawlerUtil.getStockListInfo();
		if (stockListInfo != null && stockListInfo.size() > 0) {
			// 切分数据
			resultList = createList(stockListInfo, 500);
		}
		return resultList;
	}

}
