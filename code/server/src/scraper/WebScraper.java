package scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

import settings.Constants.AppType;
import settings.Settings;
import util.Logger;
import util.Pair;

public class WebScraper extends Scraper implements Runnable {
	private LinkedBlockingQueue<ScrapeResult> results;
	private String baseUrl;
	private List<String> urlIterators;
	private String descriptor;	
	private List<Pair<String, String>> keyRegexPairs;
	
	public WebScraper(LinkedBlockingQueue<ScrapeResult> results, String baseUrl,
			List<String> urlIterators, String descriptor) {
		
		super();
		this.results = results;
		this.baseUrl = baseUrl;
		this.urlIterators = urlIterators;
		this.descriptor = descriptor;
	}

	@Override
	public void run() {		
		String html = null;
		Iterator<String> urlIterator = urlIterators.iterator();
		while (urlIterator.hasNext()) {
			try {
				html = textFromUrl(baseUrl + urlIterator.next());	
				System.out.println("add: " + results.add( getScraperResult(scrape(html, keyRegexPairs)) ) );								
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * Parses a string and converts it to the appropriate Object.
	 * @param s - String to be converted.
	 * @param type - Enumerator indicating the type.
	 * @return - Object created by parsing or null if type isn't recognized.
	 * @throws ParseException
	 */
	public static Object stringToObject (String s, AppType type) throws ParseException {
		 switch (type) {
	     case DATE:
	    	 return (new SimpleDateFormat("MMM. dd, yyyy").parse(s));
		case LONG: case DOUBLE: case INTEGER:
	         return (NumberFormat.getInstance(Locale.US).parseObject(s));
		case STRING:
	         return s;
		default:
	    	 Logger.log("Type not recognized", true);
	    	 return null;
		 }
	}
	
	public ScrapeResult getScraperResult(LinkedHashMap<String, List<String>> scrapeResult) throws ParseException {
		System.out.println("get result");
		LinkedList<Pair<String, Object>> resultList = new LinkedList<Pair<String, Object>>();
		Iterator<Entry<String, List<String>>> hmpItr = scrapeResult.entrySet().iterator();
		while(hmpItr.hasNext()) {
			Entry<String, List<String>> entry = hmpItr.next();
			String key = entry.getKey();
			//iterate over the list of value objects
			Iterator<String> objItr = entry.getValue().iterator();
			while (objItr.hasNext()) {
				resultList.add(new Pair<String, Object>(key, stringToObject(objItr.next(), Settings.getTable(descriptor).getColumn(key).apptype) ));
			}					
		}
		return new ScrapeResult(resultList, descriptor);
	}

}
