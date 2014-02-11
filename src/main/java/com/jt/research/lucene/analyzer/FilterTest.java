/**
 * @author liuze
 *
 * Feb 10, 2014
 */
package com.jt.research.lucene.analyzer;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.jt.research.lucene.attribute.PartOfSpeechAttribute;

public class FilterTest {
	
	@Test
	public void test01() throws IOException {
			    // text to tokenize
	    final String text = "This is a demo of the TokenStream API";
	    
	    Version matchVersion = Version.LUCENE_46; // Substitute desired Lucene version for XY
	    MyAnalyzerFilter analyzer = new MyAnalyzerFilter(matchVersion);
	    TokenStream stream = analyzer.tokenStream("field", new StringReader(text));
	    
	    // get the CharTermAttribute from the TokenStream
	    CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
	    PartOfSpeechAttribute posAtt = stream.addAttribute(PartOfSpeechAttribute.class);
	    try {
	      stream.reset();
	    
	      // print all tokens until stream is exhausted
	      while (stream.incrementToken()) {
//	        System.out.println(termAtt.toString());
	        System.out.println(termAtt.toString() + ": " + posAtt.getPartOfSpeech());
	      }
	    
	      stream.end();
	    } finally {
	      stream.close();
	    }
	}
}
