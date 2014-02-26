/**
 * @author liuze
 *
 * Feb 11, 2014
 */
package com.jt.research.lucene.index;

import org.apache.lucene.index.Term;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestIndexUtil {
	
	IndexUtil indexUtil = null;
	
	@Before
	public void init() {
		indexUtil = new IndexUtil();
	}
	
	@Test
	public void testIndex() {
		indexUtil.deleteAll();
		indexUtil.index();
		indexUtil.query();
	}
	
	@Test
	public void testQuery() {
		indexUtil.query();
	}
	
	@Test
	public void testForceMerge() {
		indexUtil.query();
		indexUtil.merge();
		indexUtil.query();
	}
	
	@Test
	public void testMaybeMerge() {
		indexUtil.query();
		indexUtil.maybeMerge();
		indexUtil.query();
	}
	
	@Test
	public void testDeleteAll() {
		indexUtil.query();
		indexUtil.deleteAll();
		indexUtil.query();
	}
	
	@Test
	public void testDelete() {
		indexUtil.query();
		indexUtil.deleteByWriter(new Term("id", "1"));
		indexUtil.query();
	}
	
	@Test
	public void testDeleteUnused() {
		indexUtil.query();
		indexUtil.deleteUnused();
		indexUtil.query();
	}
	
	@Test
	public void testMergeDelete() {
		indexUtil.query();
		indexUtil.mergeDelete();
		indexUtil.query();
	}
	
	@After
	public void destory() {
		indexUtil.close();
	}

}
