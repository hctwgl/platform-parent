package jcommanderTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Map;
import java.util.Set;

public class JacksonTest {

		public static void main(String[] args) throws Exception{
				String netConfigPath = "/home/config.json";
				ObjectMapper objectMapper = new ObjectMapper();
				Map<String,Object> netConfigMap = objectMapper.readValue(new File(netConfigPath), Map.class);
				Set<Map.Entry<String, Object>> entries = netConfigMap.entrySet();
				for (Map.Entry<String, Object> entry : entries) {
						System.out.println(entry.getKey()+":"+entry.getValue());
				}

		}

}
