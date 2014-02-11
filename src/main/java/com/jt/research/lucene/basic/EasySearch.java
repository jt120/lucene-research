/**
 * @author liuze
 *
 * Feb 8, 2014
 */
package com.jt.research.lucene.basic;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EasySearch {
	
	private static String indexPath = "d:/test/lucene/index";
	private static Version version = Version.LUCENE_46;
	IndexReader reader = null;
	IndexSearcher indexSearcher = null;
	Directory directory = null;
	
	@Before
	public void setUp() {
		
		try {
			directory = FSDirectory.open(new File(indexPath));
			reader = DirectoryReader.open(directory);
			indexSearcher = new IndexSearcher(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	@Test
	public void testTermQuery() {
		
		try {
			Query query = new TermQuery(new Term("id","1"));
			TopDocs topDocs = indexSearcher.search(query, 100);
			int total = topDocs.totalHits;
			System.out.println("total found "+total+" result(s)");
			smartPrint(topDocs,indexSearcher);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	@Test
	public void testBooleanQuery() throws IOException {
		
		BooleanQuery bQuery = new BooleanQuery();
		Query q1 = new TermQuery(new Term("id","1"));
		Query q2 = new TermQuery(new Term("id","2"));
		
		bQuery.add(q1, BooleanClause.Occur.SHOULD);
		bQuery.add(q2, BooleanClause.Occur.SHOULD);
		
		easySearch(bQuery);
		
	}
	
	@Test
	public void testWildcardQuery() throws IOException {
		Query query = new WildcardQuery(new Term("name","z*"));
		easySearch(query);
	}
	
	@Test
	public void testPhraseQuery() {
		PhraseQuery query = new PhraseQuery();
		query.add(new Term("city","new york"));
		easySearch(query);
	}
	
	
	private void easySearch(Query query){
		try {
			TopDocs topDocs = indexSearcher.search(query, 100);
			int total = topDocs.totalHits;
			System.out.println("total found "+total+" result(s)");
			smartPrint(topDocs,indexSearcher);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void smartPrint(TopDocs topDocs, IndexSearcher indexSearcher) throws IOException {
		for(ScoreDoc sDoc:topDocs.scoreDocs) {
			Document doc = indexSearcher.doc(sDoc.doc);
			System.out.println("id: "+doc.get("id")+" name: "+doc.get("name")+" email: "+doc.get("email")+" score: "+sDoc.score);
		}
	}
	
	@After
	public void tearDown() {
		try {
			if(reader!=null) reader.close();
			if(directory!=null) directory.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
