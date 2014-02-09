package com.jt.research.lucene.analyzer;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.jt.research.lucene.utils.AnalyzerUtils;

public class ChinesAnalyzer {
	private static Version version = Version.LUCENE_46;

	@Test
	public void test01() throws IOException {
		Analyzer[] analyzers = {new StandardAnalyzer(version),
				new CJKAnalyzer(version),
				new SmartChineseAnalyzer(version)};

		String text = "我爱北京天安门";
		for(Analyzer analyzer:analyzers) {
			String name = analyzer.getClass().getName();
			System.out.println(name+": ");
			AnalyzerUtils.displayTokens(analyzer, text);
		}
	}
}
