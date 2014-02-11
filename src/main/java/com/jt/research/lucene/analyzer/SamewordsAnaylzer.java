/**
 * @author liuze
 *
 * Feb 11, 2014
 */
package com.jt.research.lucene.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.util.Version;

import com.jt.research.lucene.filter.SamewordsFilter;
import com.jt.research.lucene.utils.SamewordsContext;

public class SamewordsAnaylzer extends Analyzer {
	
	private Version matchVersion;
	private SamewordsContext sctx; 
	
	public SamewordsAnaylzer(Version matchVersion, SamewordsContext sctx) {
		this.matchVersion = matchVersion;
		this.sctx = sctx;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		Tokenizer source = new WhitespaceTokenizer(matchVersion, reader);
		TokenStream result = new SamewordsFilter(matchVersion, source, sctx);
		return new TokenStreamComponents(source, result);
	}

}
