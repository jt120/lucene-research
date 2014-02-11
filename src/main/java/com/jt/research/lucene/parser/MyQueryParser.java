/**
 * @author liuze
 *
 * Feb 11, 2014
 */
package com.jt.research.lucene.parser;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.util.Version;

public class MyQueryParser extends QueryParser {

	public MyQueryParser(Version matchVersion, String f, Analyzer a) {
		super(matchVersion, f, a);
	}

	@Override
	public Resolution getDateResolution(String fieldName) {
		return super.getDateResolution(fieldName);
	}

	@Override
	protected org.apache.lucene.search.Query getRangeQuery(String field,
			String part1, String part2, boolean startInclusive,
			boolean endInclusive) throws ParseException {
		String dateType = "yyyy-MM-dd";
		try {
			if(field.equals("date")) {
				Pattern pattern = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}");
				if(pattern.matcher(part1).matches()&&pattern.matcher(part2).matches()) {
					SimpleDateFormat format = new SimpleDateFormat(dateType);
					long start = format.parse(part1).getTime();
					long end = format.parse(part2).getTime();
					return NumericRangeQuery.newLongRange(field, start, end, startInclusive, endInclusive);
				}
			}
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			System.out.println("请使用"+dateType+"格式检索");
		}
		return super.getRangeQuery(field, part1, part2, startInclusive, endInclusive);
	}

}
