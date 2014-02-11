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
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

public class EasySearch {
	
	private static String indexPath = "d:/test/lucene/index";
	private static Version version = Version.LUCENE_46;
	
	@Test
	public void test01() {
		IndexSearcher indexSearcher = null;
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexReader reader = DirectoryReader.open(dir);
			indexSearcher = new IndexSearcher(reader);
			
			Query query = new TermQuery(new Term("id","1"));
			TopDocs topDocs = indexSearcher.search(query, 100);
			int total = topDocs.totalHits;
			System.out.println("total found "+total+" result(s)");
			for(ScoreDoc sDoc:topDocs.scoreDocs) {
				Document doc = indexSearcher.doc(sDoc.doc);
				System.out.println("id: "+doc.get("id")+" name: "+doc.get("name")+" email: "+doc.get("email")+" score: "+sDoc.score);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
		}
		
		
	}

}
