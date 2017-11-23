package basicTest;

import com.google.common.collect.Lists;
import fid.platform.core.common.pojo.commons.LabeledSentence;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class testLabelSentence {

		public static void main(String[] args) {
				List<String> labels = Lists.newArrayList();
				List<String> contents = Lists.newArrayList();
				labels.add("1");
				labels.add("2");
				labels.add("1");
				labels.add("2");
				labels.add("3");
				labels.add("3");
				labels.add("1");
				labels.add("4");
				labels.add("4");
				labels.add("5");
				labels.add("5");

				contents.add("aa");
				contents.add("bb");
				contents.add("cc");
				contents.add("dd");
				contents.add("dd");
				contents.add("ee");
				contents.add("ff");
				contents.add("gg");
				contents.add("hh");
				contents.add("hh");
				contents.add("hh");

				LabeledSentence labeledSentence = new LabeledSentence.Builder().setLabels(labels).setSentences(contents).build();

				System.out.println(labeledSentence.getTotalExample());
				System.out.println(labeledSentence.getAllTypeTag());
				System.out.println(labeledSentence.getLabelClassCount());

				Map<String, Integer> eachTypeCount = labeledSentence.getEachTypeCount();
				Set<Map.Entry<String, Integer>> entries = eachTypeCount.entrySet();
				for (Map.Entry<String, Integer> entry : entries) {
						System.out.println(entry.getKey()+":"+entry.getValue());
				}
				System.out.println(labeledSentence.getLabels());
				System.out.println(labeledSentence.getSentences());

				LabeledSentence labeledSentence1 = labeledSentence.balanceOnLimitControll(0, 0);

				System.out.println(labeledSentence1.getTotalExample());
				System.out.println(labeledSentence1.getAllTypeTag());
				System.out.println(labeledSentence1.getLabelClassCount());

				Map<String, Integer> eachTypeCount1 = labeledSentence1.getEachTypeCount();
				Set<Map.Entry<String, Integer>> entries1 = eachTypeCount1.entrySet();
				for (Map.Entry<String, Integer> entry : entries1) {
						System.out.println(entry.getKey()+":"+entry.getValue());
				}
				System.out.println(labeledSentence1.getLabels());
				System.out.println(labeledSentence1.getSentences());
				//----------------------------*****************------------------------------
				System.out.println("**************split**************");
				LabeledSentence labeledSentence2 = labeledSentence1.splitByRate(0.3, false);
				System.out.println(labeledSentence2.getTotalExample());
				System.out.println(labeledSentence2.getAllTypeTag());
				System.out.println(labeledSentence2.getLabelClassCount());

				Map<String, Integer> eachTypeCount2 = labeledSentence2.getEachTypeCount();
				Set<Map.Entry<String, Integer>> entries2 = eachTypeCount2.entrySet();
				for (Map.Entry<String, Integer> entry : entries2) {
						System.out.println(entry.getKey()+":"+entry.getValue());
				}
				System.out.println(labeledSentence2.getLabels());
				System.out.println(labeledSentence2.getSentences());
				Double result = 103 * 0.3;

				System.out.println(result.intValue());
		}
}
