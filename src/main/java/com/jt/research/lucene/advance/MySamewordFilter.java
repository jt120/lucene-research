package com.jt.research.lucene.advance;

import java.io.IOException;
import java.util.Stack;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.util.FilteringTokenFilter;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.Version;

public class MySamewordFilter extends FilteringTokenFilter {

	private Stack<String> words = new Stack<>();
	private PositionIncrementAttribute postionAttribute = null;
	private CharTermAttribute charTermAttribute = null;
	private AttributeSource.State current = null;
	private SamewordContext samewordContext = null;

	public MySamewordFilter(Version version, TokenStream in, SamewordContext samewordContext) {
		super(version,in);
		this.postionAttribute = this.addAttribute(PositionIncrementAttribute.class);
		this.charTermAttribute = this.addAttribute(CharTermAttribute.class);
		this.samewordContext = samewordContext;
	}

	@Override
	protected boolean accept() throws IOException {
		if(addWords(charTermAttribute.toString())) {
			current = captureState();

		}
		if(words.size()>0) {
			restoreState(current);
			charTermAttribute.setEmpty();
			charTermAttribute.append(words.pop());
			postionAttribute.setPositionIncrement(0);
			return true;
		}

		if(!this.input.incrementToken()) {
			return false;
		}

		return true;
	}

	public boolean addWords(String word) {
		String[] values = samewordContext.getSamewords(word);
		if(values!=null) {
			for(String value:values) {
				words.push(value);
			}
			return true;
		}
		return false;
	}



}
