package fid.platform.api.service.DomainRecongnize.impl;

import fid.platform.api.service.DomainRecongnize.DomainParserService;
import fid.platform.core.common.commonutil.DomainParseUtil;
import fid.platform.core.common.pojo.commons.PsgAnalysisMain;
import org.springframework.stereotype.Service;

@Service
public class DomainParserServiceImpl implements DomainParserService {

		@Override
		public PsgAnalysisMain getPsgDomain(String psg) {
				return DomainParseUtil.recongnizeDomain(psg);
		}
}
