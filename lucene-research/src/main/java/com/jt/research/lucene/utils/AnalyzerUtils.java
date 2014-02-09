package com.jt.research.lucene.utils;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;

public class AnalyzerUtils {

	public static void displayTokens(Analyzer analyzer, String text) throws IOException {
		displayTokens(analyzer.tokenStream("content", new StringReader(text)));
	}

	private static void displayTokens(TokenStream tokenStream) throws IOException {
		OffsetAttribute off = tokenStream.addAttribute(OffsetAttribute.class);
		CharTermAttribute term = tokenStream.addAttribute(CharTermAttribute.class);
		PositionLengthAttribute length = tokenStream.addAttribute(PositionLengthAttribute.class);
		PositionIncrementAttribute increment = tokenStream.addAttribute(PositionIncrementAttribute.class);
		try {
			tokenStream.reset();
			while(tokenStream.incrementToken()) {
				System.out.print("["+term.toString()+"]"+increment.getPositionIncrement());
				//System.out.print("["+off.startOffset()+"]");
			}
			System.out.println();
			tokenStream.end();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			tokenStream.close();
		}

	}

}
