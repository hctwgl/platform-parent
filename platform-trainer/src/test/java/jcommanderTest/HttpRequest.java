package jcommanderTest;

import com.google.common.collect.Maps;
import fid.platform.core.common.constant.ProcessTypeConstants;
import fid.platform.core.common.constant.UrlConstants;

import java.util.Map;

public class HttpRequest {

		public static void main(String[] args) {
				Map<String, String> data = Maps.newHashMap();
				data.put("missionId", "2");
				data.put("processType", String.valueOf(ProcessTypeConstants.Word2vec_Generation));
				data.put("status", String.valueOf(ProcessTypeConstants.Process_Finished));
				com.github.kevinsawicki.http.HttpRequest.post("http://183.2.191.55:10086/platform-modelhelper/messageAcceptor/acceptProcessStatus").form(data).created();
		}

}
