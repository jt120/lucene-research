package com.jt.research.lucene.advance;

import java.util.HashMap;
import java.util.Map;

public class SamewordContext {

	Map<String,String[]> words = new HashMap<>();

	public SamewordContext() {
		words.put("good", new String[]{"fine","excellent"});
		words.put("smart", new String[]{"beauty","handsome"});
	}


	public String[] getSamewords(String key) {
		return words.get(key);
	}

}
