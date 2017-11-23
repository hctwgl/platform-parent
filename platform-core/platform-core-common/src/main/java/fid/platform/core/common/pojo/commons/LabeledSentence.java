package fid.platform.core.common.pojo.commons;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import java.beans.Transient;
import java.util.*;

/**
 * 用于封装句子List对应的label List
 *
 * @author Drx
 */
public class LabeledSentence {

		private List<String> sentences;

		private List<String> labels;

		private LabeledSentence() {

		}

		private LabeledSentence(List<String> sentences, List<String> labels) {
				this.sentences = sentences;
				this.labels = labels;
		}

		public List<String> getSentences() {
				return sentences;
		}

		public void setSentences(List<String> sentences) {
				this.sentences = sentences;
		}

		public List<String> getLabels() {
				return labels;
		}

		private void setLabels(List<String> labels) {
				this.labels = labels;
		}

		public int getLabelClassCount() {
				return new HashSet<String>(labels).size();
		}

		/**
		 * 获取所有标签
		 *
		 * @return
		 */
		public List<String> getAllTypeTag() {
				List<String> result = Lists.newArrayList();
				String tempType = labels.get(0);
				result.add(tempType);
				for (String label : labels) {
						if (!label.equals(tempType)) {
								result.add(label);
								tempType = label;
						}
				}
				return result;
		}

		/**
		 * 获取每个标签的数据数量
		 *
		 * @return
		 */
		public Map<String, Integer> getEachTypeCount() {
				Map<String, Integer> result = Maps.newLinkedHashMap();
				String tempType = labels.get(0);
				int tempCount = 1;
				for (int i = 1; i < labels.size(); i++) {
						if (labels.get(i).equals(tempType)) {
								tempCount += 1;
						} else {
								result.put(tempType, tempCount);
								tempType = labels.get(i);
								tempCount = 1;
						}
						if (i == labels.size() - 1) {
								result.put(tempType, tempCount);
						}
				}
				return result;
		}

		/**
		 * 获取数据总数
		 *
		 * @return
		 */
		public int getTotalExample() {
				return this.labels.size();
		}

		/**
		 * 平均化labeledSentence
		 * 采用简单复制原始数据的方法做数据平均化
		 *
		 * @param balanceNum 平均至多少
		 * @param dropNum    小于多少抛弃类
		 * @return
		 */
		public LabeledSentence balanceOnLimitControll(int balanceNum, int dropNum) {
				if (balanceNum == 0){
						return this;
				}
				LabeledSentence result = new LabeledSentence();
				List<String> resultLabelList = Lists.newArrayList();
				List<String> resultContentList = Lists.newArrayList();
				//临时的标签id,数量和存储列表
				String tagTemp = this.labels.get(0);
				List<String> tempLabelList = Lists.newArrayList();
				List<String> tempContentList = Lists.newArrayList();
				for (int i = 0; i < this.getLabels().size(); i++) {
						String tagNow = this.getLabels().get(i);
						//查看是否是下一个标签
						if (tagNow.equals(tagTemp)) {
								tempLabelList.add(this.getLabels().get(i));
								tempContentList.add(this.getSentences().get(i));
						}
						if (!tagNow.equals(tagTemp) || i == this.getLabels().size() - 1) {
								//如果最后只有一条,需要多执行一次平均
								int times = 1;
								if (!tagNow.equals(tagTemp) && i == this.getLabels().size() - 1) {
										times = 2;
								}
								for (int t = 0; t < times; t++) {
										if (tempLabelList.size() >= dropNum && tempLabelList.size() > 0) {
												//执行平均
												List<String> balanceLabelList = Lists.newArrayList();
												List<String> balanceContentList = Lists.newArrayList();
												if (tempLabelList.size() < balanceNum) {
														for (int j = 0; j < balanceNum; j++) {
																int mod = j % tempLabelList.size();
																balanceLabelList.add(tempLabelList.get(mod));
																balanceContentList.add(tempContentList.get(mod));
														}
														resultLabelList.addAll(balanceLabelList);
														resultContentList.addAll(balanceContentList);
														tempLabelList = Lists.newArrayList();
														tempContentList = Lists.newArrayList();
														tempLabelList.add(this.getLabels().get(i));
														tempContentList.add(this.getSentences().get(i));
														tagTemp = tagNow;
												} else {
														for (int j = 0; j < balanceNum; j++) {
																balanceLabelList.add(tempLabelList.get(j));
																balanceContentList.add(tempContentList.get(j));
														}
														resultLabelList.addAll(balanceLabelList);
														resultContentList.addAll(balanceContentList);
														tempLabelList = Lists.newArrayList();
														tempContentList = Lists.newArrayList();
														tempLabelList.add(this.getLabels().get(i));
														tempContentList.add(this.getSentences().get(i));
														tagTemp = tagNow;
												}
										} else {
												tempLabelList = Lists.newArrayList();
												tempContentList = Lists.newArrayList();
												tempLabelList.add(this.getLabels().get(i));
												tempContentList.add(this.getSentences().get(i));
												tagTemp = tagNow;
										}
								}
						}
				}
				result.setLabels(resultLabelList);
				result.setSentences(resultContentList);
				return result;
		}

		/**
		 * @param rate
		 * @param isStartWithFront
		 * @return
		 */
		public LabeledSentence splitByRate(double rate, boolean isStartWithFront) {
				LabeledSentence result = new LabeledSentence();
				//获取每个类别的数量
				Map<String, Integer> eachTypeCount = getEachTypeCount();
				Map<String, Integer> newEachTypeCount = Maps.newHashMap();
				String label = "";
				Double ratedCountDouble = 0D;
				//获取分割数量
				Set<Map.Entry<String, Integer>> entries = eachTypeCount.entrySet();
				for (Map.Entry<String, Integer> entry : entries) {
						label = entry.getKey();
						if (entry.getValue() < 2) {
								throw new IllegalArgumentException("数据数量太少,分割后无法分辨:类label:" + label + " 数量为:" + entry.getValue());
						}
						ratedCountDouble = entry.getValue() * rate;
						//重新放入分割后的map并+1
						newEachTypeCount.put(label, ratedCountDouble.intValue() + 1);
				}
				//执行分割
				List<String> newLabelList = Lists.newArrayList();
				List<String> newSentenceList = Lists.newArrayList();
				Set<Map.Entry<String, Integer>> newEntries = newEachTypeCount.entrySet();
				for (Map.Entry<String, Integer> newEntry : newEntries) {
						label = newEntry.getKey();
						int curCount = 0;
						int newCount = newEntry.getValue();
						//从前面开始
						if (isStartWithFront) {
								for (int i = 0; i < this.labels.size(); i++) {
										String curLabel = this.labels.get(i);
										String curSentence = this.sentences.get(i);
										if (label.equals(curLabel) && curCount < newCount){
												newLabelList.add(curLabel);
												newSentenceList.add(curSentence);
												curCount++;
												if (curCount == newCount){
														break;
												}
										}
								}
						//从末尾开始
						}else {
								for (int i = this.labels.size()-1; i >= 0 ; i--) {
										String curLabel = this.labels.get(i);
										String curSentence = this.sentences.get(i);
										if (label.equals(curLabel) && curCount < newCount){
												newLabelList.add(curLabel);
												newSentenceList.add(curSentence);
												curCount++;
												if (curCount == newCount){
														break;
												}
										}
								}
						}
				}
				result.labels = newLabelList;
				result.sentences = newSentenceList;

				return result;
		}

		public static final class Builder {

				private List<String> sententces;
				private List<String> labels;

				public Builder setSentences(List<String> sentences) {
						this.sententces = sentences;
						return this;
				}

				public Builder setLabels(List<String> labels) {
						this.labels = labels;
						return this;
				}

				public LabeledSentence buildEmptyOne() {
						return new LabeledSentence();
				}

				public LabeledSentence build() {
						if (sententces.size() == 0 || labels.size() == 0) {
								throw new IllegalArgumentException("句子或标签数量为0");
						}
						if (sententces.size() != labels.size()) {
								throw new IllegalArgumentException("标签和句子数量不匹配,句子数量为:" + sententces.size() + ",标签数量为:" + labels.size());
						}
						Map<String, List<String>> tempMap = Maps.newHashMap();
						//装入map排序
						for (int i = 0; i < labels.size(); i++) {
								String label = labels.get(i);
								String sentence = sententces.get(i);
								if (tempMap.containsKey(label)) {
										tempMap.get(label).add(sentence);
								} else {
										List<String> sentenceTempList = Lists.newArrayList();
										sentenceTempList.add(sentence);
										tempMap.put(label, sentenceTempList);
								}
						}
						Set<String> keys = tempMap.keySet();
						List<String> keyList = new ArrayList<>(keys);
						Collections.sort(keyList);
						List<String> labels = Lists.newArrayList();
						List<String> sentences = Lists.newArrayList();
						LabeledSentence labeledSentence = new LabeledSentence();
						for (String key : keyList) {
								List<String> currentSentenceList = tempMap.get(key);
								int eachSize = currentSentenceList.size();
								for (int i = 0; i < eachSize; i++) {
										labels.add(key);
										sentences.add(currentSentenceList.get(i));
								}
						}
						labeledSentence.setLabels(labels);
						labeledSentence.setSentences(sentences);

						return labeledSentence;
				}

		}

}
