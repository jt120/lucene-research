/**
 * @author liuze
 *
 * Feb 11, 2014
 */
package com.jt.research.lucene.index;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.jt.research.lucene.utils.Key;

public class IndexUtil {

	private static Logger logger = LogManager.getLogger(IndexUtil.class);

	private static String indexPath = Key.PATH;
	private static Version version = Key.version;

	private String[] ids = { "1", "2", "3", "4", "5" };
	private String[] names = { "zhangsan", "lisi", "wangwu", "suwukong",
			"guanyu" };
	private String[] emails = { "hao@163.com", "lisi@gmail.com",
			"wangwu@ee.com", "suwukong@sina.com", "guanyu@sanguo.com" };
	private String[] cities = { "new york", "bei jing", "tian jin", "tai yuan",
			" da lian" };
	private String[] contents = {
			"我爱北京天安门",
			"今天你吃了吗",
			"我正在工作",
			"不好意思",
			"北京的雾霾太严重了" };
	private int[] nums = { 120, 200, 300, 340, 500 };
	private static Date[] dates = new Date[5];
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	private static IndexWriter indexWriter = null;
	private static IndexReader indexReader = null;
	private IndexSearcher indexSearcher = null;
	private static Directory directory = null;
	private static IndexWriterConfig config = null;
	static {
		try {
			logger.info("init parameters...");
			directory = FSDirectory.open(new File(indexPath));
			config = new IndexWriterConfig(version, new StandardAnalyzer(
					version));
			indexWriter = new IndexWriter(directory, config);
			setDates();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void setDates() {
		try {
			dates[0] = format.parse("1999-11-21");
			dates[1] = format.parse("2002-2-1");
			dates[2] = format.parse("1987-5-6");
			dates[3] = format.parse("2008-12-21");
			dates[4] = format.parse("2011-3-11");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void index() {
		logger.info("indexing...");
		try {
			int len = ids.length;
			for (int i = 0; i < len; i++) {
				Document doc = new Document();
				doc.add(new StringField("id", ids[i], Store.YES));
				doc.add(new StringField("name", names[i], Store.YES));
				doc.add(new StringField("email", emails[i], Store.YES));
				doc.add(new StringField("city", cities[i], Store.YES));
				doc.add(new TextField("content", contents[i], Store.YES));
				doc.add(new IntField("num", nums[i], Store.YES));
				doc.add(new LongField("date", dates[i].getTime(), Store.YES));
				indexWriter.addDocument(doc);
			}
			indexWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void query() {
		try {
			indexReader = DirectoryReader.open(directory);
			System.out.println("num docs: " + indexReader.numDocs());
			System.out.println("max docs: " + indexReader.maxDoc());
			System.out.println("delete docs: " + indexReader.numDeletedDocs());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void merge() {
		logger.info("merge");
		try {
			indexWriter.forceMerge(1,true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void maybeMerge() {
		logger.info("maybe merge");
		try {
			indexWriter.maybeMerge();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteUnused() {
		try {
			indexWriter.deleteUnusedFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteAll() {
		logger.info("delete all");
		try {
			indexWriter.deleteAll();
			indexWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteByWriter(Term term) {
		logger.info("delete");
		try {
			indexWriter.deleteDocuments(term);
			indexWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void mergeDelete() {
		try {
			indexWriter.forceMergeDeletes();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteBySearch() {
		try {
			indexReader = DirectoryReader.open(directory);
			indexSearcher = new IndexSearcher(indexReader);
			indexSearcher.search(new TermQuery(new Term("id", "1")), 100);
			/*
			 * LUCENE-3606: IndexReader will be made read-only in Lucene 4.0, so
			 * all methods allowing to delete or undelete documents using
			 * IndexReader were deprecated; you should use IndexWriter now.
			 * Consequently IndexReader.commit() and all open(),
			 * openIfChanged(), clone() methods taking readOnly booleans (or
			 * IndexDeletionPolicy instances) were deprecated.
			 * IndexReader.setNorm() is superfluous and was deprecated. If you
			 * have to change per-document boost use CustomScoreQuery. If you
			 * want to dynamically change norms (boost *and* length norm) at
			 * query time, wrap your IndexReader using FilterIndexReader,
			 * overriding FilterIndexReader.norms(). To persist the changes on
			 * disk, copy the FilteredIndexReader to a new index using
			 * IndexWriter.addIndexes(). In Lucene 4.0, SimilarityProvider will
			 * allow you to customize scoring using external norms, too. (Uwe
			 * Schindler, Robert Muir)
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public IndexWriter getIndexWriter() {
		return indexWriter;
	}

	public void close() {
		logger.info("close");
		try {
			// if(directory!=null) directory.close();
			if (indexWriter != null)
				indexWriter.close();
			if (indexReader != null)
				indexReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
