package com.jt.research.lucene.advance;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.codecs.lucene3x.Lucene3xCodec;
import org.apache.lucene.util.Version;

public class MySamewordAnalyzer extends Analyzer {

	private Version matchVersion;
	private SamewordContext samewordContext;

	public MySamewordAnalyzer(Version matchVersion, SamewordContext samewordContext) {
		this.matchVersion = matchVersion;
		this.samewordContext = samewordContext;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		final Tokenizer source = new WhitespaceTokenizer(matchVersion, reader);
		TokenStream result = new MySamewordFilter(matchVersion,source,samewordContext);
		return new TokenStreamComponents(source, result);
	}

	public SamewordContext getSamewordContext() {
		return samewordContext;
	}


}
