package scraper;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import util.Pair;

public class ScrapeResult {
	private String descriptor;
	private LinkedList<Pair<String, Object>> resultList;

	public ScrapeResult(LinkedList<Pair<String, Object>> resultList, String descriptor) {
		System.out.println("scrapeResult");
		this.resultList = resultList;
		this.descriptor = descriptor;
	}



	public String toString() {
		StringBuilder outString = new StringBuilder();
		Iterator<Pair<String, Object>> itrResult = resultList.iterator();
		while (itrResult.hasNext()) {	
			Pair<String, Object> entry = itrResult.next();
			outString.append("Key: '" + entry.getA() + "'");
			outString.append(" Value: " + entry.getB());
			outString.append('\n');
		}
		return outString.toString();
	}
	
	public String getDescription () {
		return descriptor;
	}
	
	
}
