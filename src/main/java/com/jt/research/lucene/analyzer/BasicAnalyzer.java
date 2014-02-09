package com.jt.research.lucene.analyzer;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.jt.research.lucene.utils.AnalyzerUtils;

public class BasicAnalyzer {

	private static Version version = Version.LUCENE_46;

	@Test
	public void test01() throws IOException {
		Analyzer[] analyzers = {new StopAnalyzer(version),
				new SimpleAnalyzer(version),
				new WhitespaceAnalyzer(version),
				new StandardAnalyzer(version)};

		String text = "I send an email to liu at nkliuze@163.com";
		for(Analyzer analyzer:analyzers) {
			String name = analyzer.getClass().getName();
			System.out.println(name+": ");
			AnalyzerUtils.displayTokens(analyzer, text);
		}

	}

}
