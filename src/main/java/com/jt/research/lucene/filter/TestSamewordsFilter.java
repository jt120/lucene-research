/**
 * @author liuze
 *
 * Feb 11, 2014
 */
package com.jt.research.lucene.filter;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.jt.research.lucene.analyzer.SamewordsAnaylzer;
import com.jt.research.lucene.utils.SamewordsContext;
import com.jt.research.lucene.utils.SamewordsContextImpl;

public class TestSamewordsFilter {
	
	final private Version matchVersion = Version.LUCENE_46;
	private SamewordsContext samewordsContext = new SamewordsContextImpl();
	private Analyzer analyzer = new SamewordsAnaylzer(matchVersion, samewordsContext);
	
	@Test
	public void test01() {
		Directory directory = new RAMDirectory();
		IndexWriter indexWriter = null;
		IndexReader indexReader = null;
		IndexSearcher indexSearcher = null;
		IndexWriterConfig config = new IndexWriterConfig(matchVersion,analyzer);
		try {
			indexWriter = new IndexWriter(directory, config);
			Document document = new Document();
			document.add(new TextField("content","good",Store.YES));
			indexWriter.addDocument(document);
			indexWriter.commit();
			
			indexReader = DirectoryReader.open(directory);
			indexSearcher = new IndexSearcher(indexReader);
			
			QueryParser queryParser = new QueryParser(matchVersion,"content",analyzer);
			Query query = queryParser.parse("dd");
			
			TopDocs topDocs = indexSearcher.search(query, 100);
			for(ScoreDoc sDoc:topDocs.scoreDocs) {
				Document rDoc = indexSearcher.doc(sDoc.doc);
				System.out.println("result: "+rDoc.get("content"));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			try {
				if(indexWriter!=null) indexWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(indexReader!=null) indexReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(directory!=null) directory.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
