package fid.platform.api.outservice.controller;

import fid.platform.api.service.DomainRecongnize.DomainParserService;
import fid.platform.core.common.pojo.commons.MsgMap;
import fid.platform.core.common.pojo.commons.PsgAnalysisMain;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/psgDomain")
public class PsgDomainController {

		@Resource
		private DomainParserService domainParserService;

		@RequestMapping("/getPsgDomain")
		public MsgMap getPsgDomain(@RequestParam("psg") String psg){
				MsgMap msgMap = new MsgMap();
				if (StringUtils.isEmpty(psg)){
						msgMap.put(MsgMap.key_msg,"请输入文章");
						msgMap.doFail();
						return msgMap;
				}
				PsgAnalysisMain psgDomain = domainParserService.getPsgDomain(psg);
				if (psgDomain.size() != 0){
						msgMap.doSuccess(psgDomain);
						msgMap.put(MsgMap.key_msg,"拆分成功");
						return msgMap;
				}
				msgMap.put(MsgMap.key_msg,"拆分成分失败");
				msgMap.doFail();
				return msgMap;
		}


}
