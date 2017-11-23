package com.fid.ansj;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;


import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.recognition.impl.FilterRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.csvreader.CsvReader;
import com.google.common.collect.Lists;

@Component
public class AnsjLibraryLoader {

	private static Logger logger = LoggerFactory.getLogger(AnsjLibraryLoader.class);

	// 提供给调用停用词的子类
	protected static FilterRecognition filter = new FilterRecognition();

	// 使用数据库
	public AnsjLibraryLoader() {

	}

	public static FilterRecognition getFilter() {
		return filter;
	}

	static {
		// 添加自定义词库
		System.out.println("正在加载用户自定义词典");
		String rootPath = AnsjLibraryLoader.class.getResource("/").getPath();
		String csvPath1 = rootPath + "ansjDics/ansjDic.csv";
		String csvPath2 = rootPath + "ansjDics/stopWords.csv";
		System.out.println(csvPath1);
		CsvReader reader;
		try {
			reader = new CsvReader(csvPath1, ',', Charset.forName("GBK"));
			// 解除最大阅读量限制
			reader.setSafetySwitch(false);
			// 获取所有topic标签
			List<String> topicNames = Lists.newArrayList();
			while (reader.readRecord()) {
				String topicName = reader.get(0);
				topicNames.add(topicName);
				UserDefineLibrary.insertWord(topicName.toLowerCase(), "nuser", 1000);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			System.out.println("正在加载用户自定义停用词");
			reader = new CsvReader(csvPath2, ',', Charset.forName("GBK"));
			// 解除最大阅读量限制
			reader.setSafetySwitch(false);
			// 获取所有topic标签
			List<String> stopWords = Lists.newArrayList();
			while (reader.readRecord()) {
				String stopWord = reader.get(0);
				stopWords.add(stopWord);
			}
			filter.insertStopWords(stopWords);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		List<Term> terms = ToAnalysis.parse("合诚股份今日涨幅达多少").recognition(AnsjLibraryLoader.getFilter()).getTerms();
		System.out.println(terms.toString());

	}

}
