package com.prediction.crawler;
import java.util.List;


public class Page {
	String symbol;
	int page;
	int count;
	int maxPage;
	List<Topic> list;
	@Override
	public String toString() {
		return "Page [count=" + count + ", list=" + list + ", maxPage="
				+ maxPage + ", page=" + page + ", symbol=" + symbol + "]";
	}
	
	
}
