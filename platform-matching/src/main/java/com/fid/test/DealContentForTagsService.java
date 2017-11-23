package com.fid.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fid.domain.RobotNlpTrainData;
import com.fid.domain.tags.CmsContentVo;
import com.fid.domain.tags.ContentTags;
import com.fid.domain.vo.CmbStockReport;
import com.fid.domain.vo.TagsForContentVo;
import com.fid.service.RobotNlpTrainDataService;
import com.fid.service.match.ResultService;

import net.sf.json.JSONObject;

@Service("dealContentForTagsService")
public class DealContentForTagsService {

	@Autowired
	private ResultService resultService;

	@Autowired
	private RobotNlpTrainDataService robotNlpTrainDataService;
	
	

    /**
     * 处理个股公告
     * @param cmbStockReport
     * @return
     */
	public boolean dealMessageToTags(List<CmbStockReport> cmbStockReport) {
		boolean flag = false;
		//公告类资讯type为1
		Integer dataType = 1;
		List<RobotNlpTrainData> needToDbList = new ArrayList<>();
		try {
			if (cmbStockReport != null && cmbStockReport.size() > 0) {
				for (CmbStockReport stockReport : cmbStockReport) {
					String title = stockReport.getTitle();
					String txt = title + stockReport.getContent();
					Map<String, Object> allResult = resultService.getAllResult(title, dataType);
					//解析处理结果
					RobotNlpTrainData robotNlpTrainData = null;
					if (allResult.containsKey("labelData")) {
						List<TagsForContentVo> list = (List<TagsForContentVo>) allResult.get("labelData");
						if (list != null && list.size()> 0) {
							for (TagsForContentVo tagsForContentVo : list) {
								robotNlpTrainData = new RobotNlpTrainData();
								robotNlpTrainData.setMissionid((long)dataType);
								robotNlpTrainData.setTid(tagsForContentVo.getId());
								robotNlpTrainData.settName(tagsForContentVo.getName());
								robotNlpTrainData.setTxt(txt);
								needToDbList.add(robotNlpTrainData);
								
								if (needToDbList.size() == 10) {
									//批量插入
									robotNlpTrainDataService.addBatch(needToDbList);
									System.out.println("执行批量插入--------"+needToDbList.size());
									needToDbList.clear();
								}
							}
						}
					}
				}
				if (needToDbList.size() > 0) {
					//批量插入
					robotNlpTrainDataService.addBatch(needToDbList);
				}
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

}
