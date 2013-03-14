package scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import util.Pair;

public class WebScraper extends Scraper implements Runnable {
	private LinkedBlockingQueue <LinkedHashMap<String, List<String>>> results;
	private String baseUrl;
	private List<String> urlIterators;
	private String descriptor;	
	private List<Pair<String, String>> keyRegexPairs;
	
	public WebScraper(LinkedBlockingQueue <LinkedHashMap<String, List<String>>> results, String baseUrl,
			List<String> urlIterators, String descriptor) {
		
		super();
		this.results = results;
		this.baseUrl = baseUrl;
		this.urlIterators = urlIterators;
		this.descriptor = descriptor;
	}

	@Override
	public void run() {		
		System.out.println("webscraper running");
		String html = null;
		Iterator<String> urlIterator = urlIterators.iterator();
		while (urlIterator.hasNext()) {
			try {
				html = textFromUrl(baseUrl + urlIterator.next());	
				System.out.println(results.offer(scrape(html, keyRegexPairs)) );				
				System.out.println("adding results");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
