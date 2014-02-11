/**
 * @author liuze
 *
 * Feb 11, 2014
 */
package com.jt.research.lucene.filter;

import java.io.IOException;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.Version;

import com.jt.research.lucene.utils.SamewordsContext;

public class SamewordsFilter extends TokenFilter {
	
	private SamewordsContext samewordsContext;
	private CharTermAttribute charTermAttribute;
	private PositionIncrementAttribute  positionIncrementAttribute;
	private AttributeSource.State current;
	private Stack<String> samewords;
	

	public SamewordsFilter(Version version, TokenStream input, SamewordsContext samewordsContext) {
		super(input);
		this.samewordsContext = samewordsContext;
		charTermAttribute = this.addAttribute(CharTermAttribute.class);
		positionIncrementAttribute = this.addAttribute(PositionIncrementAttribute.class);
		samewords = new Stack<>();
	}

	@Override
	public boolean incrementToken() throws IOException {
		
		if(samewords.size()>0) {
			String w = samewords.pop();
			restoreState(current);
			charTermAttribute.setEmpty();
			charTermAttribute.append(w);
			positionIncrementAttribute.setPositionIncrement(0);
			return true;
		}
		
		if(!this.input.incrementToken()) return false;
		
		if(addWords(charTermAttribute.toString())) {
			current = captureState();
		}
		
		return true;
	}
	
	public boolean addWords(String name) {
		
		String[] words = samewordsContext.getSamewords(name);
		
		if(words!=null) {
			for(String s:words) {
				samewords.push(s);
			}
			return true;
		}
		return false;
	}
	
	

}
