/**
 * @author liuze
 *
 * Feb 11, 2014
 */
package com.jt.research.lucene.tika;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.jt.research.lucene.search.SearchUtil;

public class TestTikaIndex {
	private SearchUtil searchUtil = null;
	private TikaIndex tikaIndex = null;
	private static String indexPath = "d:/test/lucene/tika/index";
	private static String filePath = "e:\\develop\\lucene\\lucene-4.6.1\\docs\\index.html";
	@Before
	public void init() {
		searchUtil = new SearchUtil(indexPath);
		tikaIndex = new TikaIndex();
	}

	@Test
	public void index() {
		tikaIndex.index(filePath);
	}

	@Test
	public void reIndex() {
		tikaIndex.deleteAll();
		tikaIndex.index(filePath);
	}

	@Test
	public void query() {
		tikaIndex.query();
	}

	@Test
	public void testSearch() {
		searchUtil.termQueryFile("fileContent", "High-level");
		searchUtil.termQueryFile("fileName", "ChineseAnalyzer.html");
	}

	@Test
	public void testTika() {
		String s = tikaIndex.fileToTxt(new File("e:/develop/lucene/lucene-4.6.1/docs/analyzers-common/org/apache/lucene/analysis/cjk/package-tree.html"));
		System.out.println(s);
	}

	@Test
	public void testMatchAll() {
		searchUtil.matchAllFile();
	}
}
