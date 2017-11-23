package com.fid.test;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import com.fid.ansj.AnsjLibraryLoader;

public class testAnsj extends AnsjLibraryLoader {

	public static void main(String[] args) {
		List<Term> terms = ToAnalysis.parse("工业4.0/智能制造").recognition(AnsjLibraryLoader.getFilter()).getTerms();
		String str = "";
		for (Term term : terms) {
			System.out.println(term.getName());
			if ("".equals(str)) {
				str = str + term.getName();
			}else {
				str = str + "," + term.getName();
			}
		}
		System.out.println(str);
	}

}
