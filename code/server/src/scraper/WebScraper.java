package scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

import settings.Settings;
import settings.Table;
import util.Pair;

public class WebScraper extends Scraper implements Runnable {
	private LinkedBlockingQueue<ScrapeResult> results;
	private Table table;
	
	public WebScraper(LinkedBlockingQueue<ScrapeResult> results, Table table) {		
		super();
		this.results = results;
		this.table = table;
	}

	@Override
	public void run() {		

		
//		String html = null;
//		Iterator<String> urlIterator = urlIterators.iterator();
//		while (urlIterator.hasNext()) {
//			try {
//				html = textFromUrl(baseUrl + urlIterator.next());	
//				System.out.println("add: " + results.add( getScraperResult(scrape(html, keyRegexPairs)) ) );								
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
	}
//	
//	private void scrapeTable () {
//		if (table.isIndex()) {
//			html = textFromUrl(baseUrl + urlIterator.next());
//			scrapeIndexTable();
//		} else {
//			html = textFromUrl(baseUrl + urlIterator.next());
//			scrapeDetailsTable();
//		}
//	}
	
	private void scrapeIndexTable() {
		if (table.getParent() == null) {
			scrapeRoot();
		} else {
			scrapeIndex();
		}
	}
	
	private void scrapeIndex() {
		// TODO Auto-generated method stub
		
	}

	private void scrapeRoot() {
		// TODO Auto-generated method stub
		
	}

	private void scrapeDetailsTable() {
		
	}
	
//	public ScrapeResult getScraperResult(LinkedHashMap<String, List<String>> scrapeResult) throws ParseException {
//		System.out.println("get result");
//		LinkedList<Pair<String, Object>> resultList = new LinkedList<Pair<String, Object>>();
//		Iterator<Entry<String, List<String>>> hmpItr = scrapeResult.entrySet().iterator();
//		while(hmpItr.hasNext()) {
//			Entry<String, List<String>> entry = hmpItr.next();
//			String key = entry.getKey();
//			//iterate over the list of value objects
//			Iterator<String> objItr = entry.getValue().iterator();
//			while (objItr.hasNext()) {
//				resultList.add(new Pair<String, Object>(key, stringToObject(objItr.next(), Settings.getTable(descriptor).getColumn(key).apptype) ));
//			}					
//		}
//		return new ScrapeResult(resultList, descriptor);
//	}

}
