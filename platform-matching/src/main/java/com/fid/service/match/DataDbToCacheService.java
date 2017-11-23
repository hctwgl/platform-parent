package com.fid.service.match;

import java.util.List;
import java.util.Set;


public interface DataDbToCacheService {
	public boolean wordsToCache();
	
	public Set<String> getMapWordsFormCache();
	
	public List<String> getWordsFormCache();
	
}
