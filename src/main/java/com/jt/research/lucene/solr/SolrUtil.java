package com.jt.research.lucene.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;



public class SolrUtil {

	private static String url = "http://localhost:8983/solr/";
	private HttpSolrServer server = null;

	@Before
	public void init() {
		server = new HttpSolrServer(url);
	}

	@Test
	public void index() {
		try {
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id",1);
			doc.addField("title", "我的第一个solr程序");
			doc.addField("content", "Hello Solr");
			server.add(doc);
			server.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void indexList() {
		try {
			List<SolrInputDocument> list = new ArrayList<>();
			SolrInputDocument doc1 = new SolrInputDocument();
			doc1.addField("id",2);
			doc1.addField("title","列表添加");
			doc1.addField("content","内容添加");
			list.add(doc1);
			SolrInputDocument doc2 = new SolrInputDocument();
			doc2.addField("id",3);
			doc2.addField("title","列表添加2");
			doc2.addField("content","内容添加2");
			list.add(doc2);

			server.add(list);
			server.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testBean() {
		try {
			List<Message> list = new ArrayList<>();
			Message m1 = new Message(1,"你好","add Beans1");
			Message m2 = new Message(2,"你好2","add Beans2");
			list.add(m1);
			list.add(m2);
			server.addBeans(list);
			server.commit();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void query() {
		try {
			SolrQuery query = new SolrQuery("*");
			query.setStart(0);
			query.setRows(1000);
			QueryResponse response = server.query(query);

			SolrDocumentList list = response.getResults();
			System.out.println(list.getNumFound());
			for(SolrDocument sd:list) {
				System.out.println(sd.get("title")+":"+sd.get("content"));
			}
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	@Test
	public void queryHighlight() {
		try {
			SolrQuery query = new SolrQuery("你好");
			query.setStart(0);
			query.setRows(1000);
			query.setHighlight(true).setHighlightSimplePre("<span class='search'>")
			.setHighlightSimplePost("</span>");
			query.setParam("hl.fl", "title,content");
			QueryResponse response = server.query(query);

			SolrDocumentList list = response.getResults();
			System.out.println(list.getNumFound());
//			System.out.println(query.getHighlightFields());
			for(SolrDocument sd:list) {
				String id = (String) sd.getFieldValue("id");
				System.out.println("id:"+id);
				System.out.println(response.getHighlighting().get(id).get("title"));
				System.out.println(sd.get("title")+":"+sd.get("content"));
			}
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
