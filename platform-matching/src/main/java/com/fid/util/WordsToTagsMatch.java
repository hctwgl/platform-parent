package com.fid.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fid.common.param.RbsCommonParams;

public class WordsToTagsMatch {

	public static Set<String> getTags(Set<String> tags, Set<String> words, String[] cizus) {
		Set<String> cizuSet = new LinkedHashSet<>();
		for (String cizu : cizus) {
			if (StringUtil.isBlank(cizu))
				continue;
			String[] strs = cizu.split(RbsCommonParams.WORD_SPLIT);
			List<String> wordList = new ArrayList<>();
			for (String word : strs) {
				wordList.add(word);
				words.add(word);
			}
			cizuSet.add(String.join(",", wordList));
		}

		List<List<String>> list = new ArrayList<>();
		List<String> cizuList = new ArrayList<>(cizuSet);
		if (cizuList.size() > 0) {
			if (cizuList.get(0) != null) {
				List<String> initList = Arrays.asList(cizuList.get(0).split(RbsCommonParams.WORD_SPLIT));
				for (String string : initList) {
					List<String> oneList = new ArrayList<>();
					oneList.add(string);
					list.add(oneList);
				}
			}
			if (cizuList.size() > 1) {
				for (int i = 1; i < cizuList.size(); i++) {
					list = getDealList(list, Arrays.asList(cizuList.get(i).split(RbsCommonParams.WORD_SPLIT)));
				}
			}
		}

		for (List<String> stringList : list) {
			if (stringList == null || stringList.size() == 0)
				continue;
			tags.add(String.join(RbsCommonParams.WORD_SPLIT, stringList));
		}
		return tags;
	}

	// 塑造
	public static List<List<String>> getDealList(List<List<String>> needList, List<String> needToAppendList) {
		List<List<String>> result = new ArrayList<>();
		if (needList == null || needList.size() == 0)
			return result;
		if (needToAppendList == null || needToAppendList.size() == 0)
			return needList;
		for (List<String> list : needList) {
			for (String string : needToAppendList) {
				List<String> newList = new ArrayList<>(list);
				newList.add(string);
				result.add(newList);
			}
		}
		return result;
	}

}
