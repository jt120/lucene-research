/**
 * @author liuze
 *
 * Feb 10, 2014
 */
package com.jt.research.lucene.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.util.Version;

import com.jt.research.lucene.filter.PartOfSpeechTaggingFilter;

public class MyAnalyzerFilter extends Analyzer {

	  private Version matchVersion;
	  
	  public MyAnalyzerFilter(Version matchVersion) {
	    this.matchVersion = matchVersion;
	  }

	/*  @Override
	  protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		  final Tokenizer source = new WhitespaceTokenizer(matchVersion, reader);
		    TokenStream result = new LengthFilter(matchVersion, source, 3, Integer.MAX_VALUE);
		    return new TokenStreamComponents(source, result);
	    //return new TokenStreamComponents(new WhitespaceTokenizer(matchVersion, reader));
	  }*/
	  
	  @Override
	  protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
	    final Tokenizer source = new WhitespaceTokenizer(matchVersion, reader);
	    TokenStream result = new LengthFilter(matchVersion, source, 3, Integer.MAX_VALUE);
	    result = new PartOfSpeechTaggingFilter(result);
	    return new TokenStreamComponents(source, result);
	  }

	}
