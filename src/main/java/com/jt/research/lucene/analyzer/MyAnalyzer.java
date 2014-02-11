/**
 * @author liuze
 *
 * Feb 10, 2014
 */
package com.jt.research.lucene.analyzer;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.util.Version;

public class MyAnalyzer extends StopwordAnalyzerBase {
	public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;
	public static final CharArraySet STOP_WORDS_SET = StopAnalyzer.ENGLISH_STOP_WORDS_SET; 
	private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;
	
	public MyAnalyzer(Version matchVersion, CharArraySet stopWords) {
	    super(matchVersion, stopWords);
	  }
	
	protected MyAnalyzer(Version matchVersion) {
		this(matchVersion, STOP_WORDS_SET);
	}
	public int getMaxTokenLength() {
	    return maxTokenLength;
	}
	public void setMaxTokenLength(int length) {
	    maxTokenLength = length;
	}
	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
	    final StandardTokenizer src = new StandardTokenizer(matchVersion, reader);
	    src.setMaxTokenLength(maxTokenLength);
	    TokenStream tok = new StandardFilter(matchVersion, src);
	    tok = new LowerCaseFilter(matchVersion, tok);
	    tok = new StopFilter(matchVersion, tok, stopwords);
	    return new TokenStreamComponents(src, tok) {
	      @Override
	      protected void setReader(final Reader reader) throws IOException {
	        src.setMaxTokenLength(MyAnalyzer.this.maxTokenLength);
	        super.setReader(reader);
	      }
	    };
	}

	@Override
	public int getPositionIncrementGap(String fieldName) {
		return 10;
	}
	
}

