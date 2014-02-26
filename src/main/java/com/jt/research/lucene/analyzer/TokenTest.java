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
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.jt.research.lucene.utils.Key;


public class TokenTest {
	
	private Version matchVersion =Key.version;
	
	@Test
	public void test01() throws IOException {
		
		Analyzer analyzer = new MMSegAnalyzer();
		TokenStream ts = null;
		try {
			ts = analyzer.tokenStream("myField", new StringReader("我爱北京天安门"));
//			OffsetAttribute offset = ts.addAttribute(OffsetAttribute.class);
			CharTermAttribute ch = ts.addAttribute(CharTermAttribute.class);
//			ts.reset();中文分词器不能加reset
			while(ts.incrementToken()) {
				System.out.print("["+ch+"]");
//				System.out.println("token: "+ts.reflectAsString(true));
//				System.out.println("start: " + offset.startOffset());
//				System.out.println("end: " + offset.endOffset());
				
			}
			ts.end();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ts.close();
		}
		
	}
}
