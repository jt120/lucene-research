/**
 * @author liuze
 *
 * Feb 11, 2014
 */
package com.jt.research.lucene.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	
	private static List<String> fileNames = new ArrayList<>();
	
	public static List<String> listFile(File file) {
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(File f:files) {
				listFile(f);
			}
		} else {
			fileNames.add(file.getAbsolutePath());
		}
		return fileNames;
	}
	
	public static List<String> getFileNames() {
		return fileNames;
	}
}
