package com.jt.research.lucene.advance;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
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

public class TestSameword {

	private Version matchVersion = Version.LUCENE_46;
	private SamewordContext samewordContext = new SamewordContext();
	private Analyzer analyzer = new MySamewordAnalyzer(matchVersion, samewordContext);
//	private Analyzer analyzer = new StandardAnalyzer(matchVersion);
	@Test
	public void test01() {
		Directory directory = null;
		IndexWriter indexWriter = null;
		IndexReader indexReader = null;
		IndexWriterConfig config = new IndexWriterConfig(matchVersion, analyzer);
		try {
			directory = new RAMDirectory();
			indexWriter = new IndexWriter(directory, config);

			Document document = new Document();
			document.add(new TextField("content", "or good",Store.YES));

			indexWriter.addDocument(document);

			indexWriter.commit();

			indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			QueryParser parser = new QueryParser(matchVersion, "content", analyzer);
			Query query = parser.parse("excellent");

			TopDocs topDocs = indexSearcher.search(query, 100);
			for(ScoreDoc match:topDocs.scoreDocs) {
				Document matchDoc = indexSearcher.doc(match.doc);
				System.out.println("result: "+matchDoc.get("content"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				directory.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				indexWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				indexReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Test
	public void test02() throws Exception {
		TokenStream ts = analyzer.tokenStream("content", "good");
		CharTermAttribute cta = ts.addAttribute(CharTermAttribute.class);

		ts.reset();
		while(ts.incrementToken()) {
			System.out.println("token:"+ts.reflectAsString(true));
		}
		ts.end();
		ts.close();
	}

}
