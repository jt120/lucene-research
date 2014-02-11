/**
 * @author liuze
 *
 * Feb 11, 2014
 */
package com.jt.research.lucene.utils;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class DirectoryUtil {
	
	private String path;
	private Directory directory;
	
	public DirectoryUtil() {
		
	}
	
	public DirectoryUtil(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Directory getDirectory() {
		try {
			directory = FSDirectory.open(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return directory;
	}
	public void setDirectory(Directory directory) {
		this.directory = directory;
	}
	
	

}
