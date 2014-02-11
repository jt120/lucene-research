/**
 * @author liuze
 *
 * Feb 10, 2014
 */
package com.jt.research.lucene.analyzer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

public class IncrementTest {
	private static Version matchVersion = Version.LUCENE_46;
	
	
	@Test
	public void test01() {
		IndexWriter indexWriter = null;
		IndexReader indexReader = null;
		Analyzer analyzer = new StandardAnalyzer(matchVersion);
//		Analyzer analyzer = new MyAnalyzer(matchVersion);
		try {
			Directory dir = new RAMDirectory();
			IndexWriterConfig config = new IndexWriterConfig(matchVersion, analyzer);
			indexWriter = new IndexWriter(dir, config);
			
			Document doc = new Document();
			doc.add(new TextField("f", "first ends", Store.YES));
			doc.add(new TextField("f", "starts two", Store.YES));
			indexWriter.addDocument(doc);
			indexWriter.commit();
			
			indexReader = DirectoryReader.open(dir);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			QueryParser queryParser = new QueryParser(matchVersion, "a", analyzer);
			Query phraseQuery = queryParser.createPhraseQuery("f", "ends starts");
//			Query query = new TermQuery(new Term("f","starts two"));
			TopDocs topDocs = indexSearcher.search(phraseQuery, 100);
			
			for(ScoreDoc match:topDocs.scoreDocs) {
				Document mDoc = indexSearcher.doc(match.doc);
				System.out.println("result:"+mDoc.get("f"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(indexWriter!=null)
				indexWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(indexReader!=null)
				indexReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	

	@Test
	public void test02() {
		IndexWriter indexWriter = null;
		IndexReader indexReader = null;
		Analyzer analyzer = new StandardAnalyzer(matchVersion);
		try {
			Directory dir = new RAMDirectory();
			IndexWriterConfig config = new IndexWriterConfig(matchVersion, analyzer);
			indexWriter = new IndexWriter(dir, config);
			
			Document doc = new Document();
			doc.add(new TextField("f", "blue is the sky", Store.YES));
			indexWriter.addDocument(doc);
			indexWriter.commit();
			
			indexReader = DirectoryReader.open(dir);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			QueryParser queryParser = new QueryParser(matchVersion, "f", analyzer);
			Query query = queryParser.createPhraseQuery("f", "blue sky");
			TopDocs topDocs = indexSearcher.search(query, 100);
			
			for(ScoreDoc match:topDocs.scoreDocs) {
				Document mDoc = indexSearcher.doc(match.doc);
				System.out.println("result:"+mDoc.get("f"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(indexWriter!=null)
				indexWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(indexReader!=null)
				indexReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
		
		
	}
}
