/**
 * @author liuze
 *
 * Feb 11, 2014
 */
package com.jt.research.lucene.search;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FieldValueFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.jt.research.lucene.parser.MyQueryParser;
import com.jt.research.lucene.utils.DirectoryUtil;

public class SearchUtil {
	private static String indexPath = "d:/test/lucene/index";
	private final static Version matchVersion = Version.LUCENE_46;
	private static Analyzer analyzer = null;
	IndexReader indexReader = null;
	IndexSearcher indexSearcher = null;
	Directory directory = null;
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	private DirectoryUtil directoryUtil = new DirectoryUtil();
	public SearchUtil(String path) {
		try {
			directoryUtil.setPath(path);
			directory = directoryUtil.getDirectory();
			indexReader = DirectoryReader.open(directory);
			indexSearcher = new IndexSearcher(indexReader);
			analyzer = new StandardAnalyzer(matchVersion);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 实现近实时搜索
	 * @param indexWriter
	 */
	public void getReader(IndexWriter indexWriter) {
		try {
			IndexReader reader2 = DirectoryReader.open(indexWriter, true);
			if(reader2!=null) {
				indexReader = reader2;
				indexSearcher = new IndexSearcher(indexReader);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void termQuery(String field, String value) {
		Query query = new TermQuery(new Term(field, value));
		easySearch(query);
	}

	public void booleanQuery(BooleanClause.Occur o, Term... value) {
		BooleanQuery bQuery = new BooleanQuery();
		for (Term term : value) {
			Query query = new TermQuery(term);
			bQuery.add(query, o);
		}
		easySearch(bQuery);
	}

	public void wildcardQuery(String field, String value) {
		Query query = new WildcardQuery(new Term(field, value));
		easySearch(query);
	}

	public void phraseQuery(String field, String value) {
		PhraseQuery query = new PhraseQuery();
		query.add(new Term(field, value));
		easySearch(query);
	}

	public void fuzzyQuery(String field, String value) {
		FuzzyQuery query = new FuzzyQuery(new Term(field, value));
		easySearch(query);
	}
	
	public void numRangeQuery(int start, int end) {
		 Query query = NumericRangeQuery.newIntRange("num", start, end, true, true);
		 easySearch(query);
	}
	
	public void matchAll() {
		Query query = new MatchAllDocsQuery();
		easySearch(query);
	}
	
	public void matchAllFile() {
		Query query = new MatchAllDocsQuery();
		easySearchFile(query);
	}
	
	public void filterQuery() {
		Query query = new MatchAllDocsQuery();
		Filter filter = new FieldValueFilter("ids",true);
		easySearch(query, filter);
	}
	
	public void queryParse(String input) {
		QueryParser queryParser = new QueryParser(matchVersion, "f", analyzer);
		try {
			Query query = queryParser.parse(input);
			easySearch(query);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void myParser(String input) {
		QueryParser queryParser = new MyQueryParser(matchVersion, "date", analyzer);
		try {
			Query query = queryParser.parse(input);
			easySearch(query);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private TopDocs easySearch(Query query, Filter filter) {
		try {
			TopDocs topDocs = indexSearcher.search(query, filter, 100);
			int total = topDocs.totalHits;
			System.out.println("total found " + total + " result(s)");
			smartPrint(topDocs, indexSearcher);
			return topDocs;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private TopDocs easySearch(Query query) {
		try {
			TopDocs topDocs = indexSearcher.search(query, 100);
			int total = topDocs.totalHits;
			System.out.println("total found " + total + " result(s)");
			smartPrint(topDocs, indexSearcher);
			return topDocs;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	private TopDocs easySearchFile(Query query) {
		try {
			TopDocs topDocs = indexSearcher.search(query, 100);
			int total = topDocs.totalHits;
			System.out.println("total found " + total + " result(s)");
			smartPrintFile(topDocs, indexSearcher);
			return topDocs;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void smartPrintFile(TopDocs topDocs, IndexSearcher indexSearcher) {
		try {
			for (ScoreDoc sDoc : topDocs.scoreDocs) {
				Document doc = indexSearcher.doc(sDoc.doc);
				System.out.println("[" + doc.get("fileName") + "]fileSize:"
						+ doc.get("fileSize") + ", filePath:" + doc.get("filePath")
						+ ", score:" + sDoc.score);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void smartPrint(TopDocs topDocs, IndexSearcher indexSearcher) {
		try {
			for (ScoreDoc sDoc : topDocs.scoreDocs) {
				Document doc = indexSearcher.doc(sDoc.doc);
				System.out.println("[id-" + doc.get("id") + "]name:"
						+ doc.get("name") + ", email:" + doc.get("email")
						+ ", city:" + doc.get("city")+  ", time:"+ parseDate(doc.get("date"))
						+ ", num:"+doc.get("num") + ", score:" + sDoc.score);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String parseDate(String l) {
		long num = Long.parseLong(l);
		Date date = new Date(num);
		String result = format.format(date);
		return result;
	}
	
	public void close() {
		try {
			if(directory!=null) directory.close();
			if(indexReader!=null) indexReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
