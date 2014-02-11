/**
 * @author liuze
 *
 * Feb 11, 2014
 */
package com.jt.research.lucene.utils;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class TestFileUtil {
	
	@Test
	public void test01() {
		File file =  new File("D:\\share\\develop\\lucene\\lucene-4.6.1\\docs");
		List<String> lists = FileUtil.listFile(file);
		for(String s:lists) {
			System.out.println(s);
		}
		System.out.println(lists.size());
	}

}
