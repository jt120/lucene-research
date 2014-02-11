/**
 * @author liuze
 *
 * Feb 10, 2014
 */
package com.jt.research.lucene.analyzer;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Version;
import org.junit.Test;


public class TokenTest {
	
	private Version matchVersion = Version.LUCENE_46;
	
	@Test
	public void test01() throws IOException {
		
		Analyzer analyzer = new StandardAnalyzer(matchVersion);
		TokenStream ts = null;
		try {
			ts = analyzer.tokenStream("myField", new StringReader("I want to bug a mac book"));
			OffsetAttribute offset = ts.addAttribute(OffsetAttribute.class);
			ts.reset();
			while(ts.incrementToken()) {
				System.out.println("token: "+ts.reflectAsString(true));
				System.out.println("start: " + offset.startOffset());
				System.out.println("end: " + offset.endOffset());
			}
			ts.end();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ts.close();
		}
		
	}
}
