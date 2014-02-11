/**
 * @author liuze
 *
 * Feb 11, 2014
 */
package com.jt.research.lucene.utils;

import java.util.HashMap;
import java.util.Map;

public class SamewordsContextImpl implements SamewordsContext {


	
	Map<String,String[]> samewords = new HashMap<>();
	
	public SamewordsContextImpl() {
		samewords.put("good", new String[]{"fine","wonderfull"});
	}
	
	@Override
	public String[] getSamewords(String word) {
		return samewords.get(word);
	}

}
