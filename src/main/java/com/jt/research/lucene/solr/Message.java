package com.jt.research.lucene.solr;

import org.apache.solr.client.solrj.beans.Field;

public class Message {

	private int id;
	private String title;
	private String content;

	public Message() {}


	public Message(int id, String title, String content) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
	}


	public int getId() {
		return id;
	}
	@Field
	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	@Field
	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}
	@Field
	public void setContent(String content) {
		this.content = content;
	}
}
