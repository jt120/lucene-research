/**
 * @author liuze
 *
 * Feb 11, 2014
 */
package com.jt.research.lucene.tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
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
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;

import com.jt.research.lucene.index.IndexUtil;
import com.jt.research.lucene.utils.FileUtil;

public class TikaIndex {
	private static Logger logger = LogManager.getLogger(IndexUtil.class);

	private static String indexPath = "d:/test/lucene/tika/index";
	private static Version version = Version.LUCENE_46;

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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void index(String filePath) {
		logger.info("indexing...");
		try {
			List<String> lists = FileUtil.listFile(new File(filePath));
			for(String path:lists) {
				File f = new File(path);
				Document document = new Document();
				document.add(new StringField("fileName",f.getName(),Store.YES));
				document.add(new StringField("filePath",path,Store.YES));
				document.add(new LongField("fileSize",f.getTotalSpace(),Store.YES));
				document.add(new TextField("fileContent", new Tika().parse(f)));
				indexWriter.addDocument(document);
			}
			indexWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String fileToTxt(File file) {
		Tika tika = new Tika();
		tika.setMaxStringLength(Integer.MAX_VALUE);
		Metadata metadata = new Metadata();
		metadata.set(TikaCoreProperties.CREATOR, "空号");
		metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
		String s = null;
		try {
			s = tika.parseToString(new FileInputStream(file),metadata);
			for(String name:metadata.names()) {
				System.out.println(name+":"+metadata.get(name));
			}
		} catch (IOException | TikaException e) {
			e.printStackTrace();
		}
		return s;
	}

	public void query() {
		try {
			System.out.println("num docs: " + indexWriter.numDocs());
			System.out.println("max docs: " + indexWriter.maxDoc());
		} catch (Exception e) {
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
