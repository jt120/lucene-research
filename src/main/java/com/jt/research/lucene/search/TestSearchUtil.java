/**
 * @author liuze
 *
 * Feb 11, 2014
 */
package com.jt.research.lucene.search;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jt.research.lucene.index.IndexUtil;

public class TestSearchUtil {
	
	SearchUtil searchUtil = null;
	IndexUtil indexUtil = null;
	private static String indexPath = "d:/test/lucene/index";
	@Before
	public void init() {
		searchUtil = new SearchUtil(indexPath);
		indexUtil = new IndexUtil();
	}
	
	@Test
	public void testTermQuery() {
		searchUtil.termQuery("id", "1");
	}
	
	@Test
	public void index() {
		indexUtil.deleteAll();
		indexUtil.index();
	}
	
	@Test
	public void testDeleteQuery() {
		
		indexUtil.deleteByWriter(new Term("id", "1"));
		searchUtil.getReader(indexUtil.getIndexWriter());
		searchUtil.termQuery("id", "1");
	}
	
	@Test
	public void testBooleanQuery() {
		Term term1 = new Term("id","1");
		Term term2 = new Term("id","2");
		searchUtil.booleanQuery(BooleanClause.Occur.SHOULD, term1,term2);
	}
	
	@Test
	public void testWildQuery() {
		searchUtil.wildcardQuery("name", "w*");
	}
	
	@Test
	public void testPhraseQuery() {
		searchUtil.phraseQuery("city", "new york");
	}
	
	@Test
	public void testFuzzyQuery() {
		searchUtil.fuzzyQuery("name", "zhangsen");
	}
	
	@Test
	public void testRange() {
		searchUtil.numRangeQuery(100, 200);
	}
	
	@Test
	public void testMatchAll() {
		searchUtil.matchAll();
	}
	
	@Test
	public void testQueryParse() {
		searchUtil.queryParse("name:zhang*");
	}
	
	@Test
	public void testMyQueryParse() {
		searchUtil.myParser("date:[1999-1-1 TO 2008-1-1]");
	}
	
	@Test
	public void testFilterQuery() {
		searchUtil.filterQuery();
	}
	
	@After
	public void destroy() {
		searchUtil.close();
		indexUtil.close();
	}

}
