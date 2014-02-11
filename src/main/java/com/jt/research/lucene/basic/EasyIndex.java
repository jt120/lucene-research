/**
 * @author liuze
 *
 * Feb 8, 2014
 */
package com.jt.research.lucene.basic;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

public class EasyIndex {
	
	private static String indexPath = "d:/test/lucene/index";
	private static Version version = Version.LUCENE_46;
	
	private String[] ids = {"1","2","3","4","5"};
	private String[] names = {"zhangsan","lisi","wangwu","suwukong","guanyu"};
	private String[] emails = {"hao@163.com","lisi@gmail.com","wangwu@ee.com","suwukong@sina.com","guanyu@sanguo.com"};
	
	@Test
	public void test01() {
		IndexWriter indexWriter = null;
		try {
			Directory dir = FSDirectory.open(new File(indexPath));
			IndexWriterConfig config = new IndexWriterConfig(version, new StandardAnalyzer(version));
			indexWriter = new IndexWriter(dir, config);
			
			int len = ids.length;
			for(int i=0;i<len;i++) {
				Document doc = new Document();
				doc.add(new StringField("id", ids[i], Store.YES));
				doc.add(new StringField("name", names[i], Store.YES));
				doc.add(new StringField("email", emails[i], Store.YES));
				indexWriter.addDocument(doc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(indexWriter!=null)
				try {
					indexWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
				
		
		
	}
	

}
