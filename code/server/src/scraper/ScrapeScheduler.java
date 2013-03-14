package scraper;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import database.DbHandler;

public class ScrapeScheduler implements Runnable {
	public ScrapeScheduler () {}
	private final DbHandler db = new DbHandler();
	private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
	private final LinkedBlockingQueue<ScrapeResult> results = new LinkedBlockingQueue<ScrapeResult>();
	
	@Override
	public void run() {
		LinkedList<String> test = new LinkedList<String>();
		test.add("803");
		test.add("804");
		executor.execute(new WebScraper(results, "http://hosteddb.fightmetric.com/fighters/details/", test, "fighters"));
		while(true) {			
			System.out.println("polling: " + results.poll());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		new Thread(new ScrapeScheduler()).run();
	}
}
		//
		//scrape( textFromUrl("http://www.ufc.com/schedule/all/SE"), Settings.getInstance().);
		//scrape future events schedule		
			//check latest event available, compare to future events
				//scrape in the gap
					//add fights, rounds and fighters
		//update events with time
			//mark events that have taken place
		//rescrape events that havn't taken place
			//add fighters, fights		
		//sleep for 8h

		// TODO Auto-generated method stub
		// test method for scraping iteratively

		//			scrapeRangeToDb("fighters", "http://hosteddb.fightmetric.com/fighters/details/", 803, 803);
		//			scrapeRangeToDb("events", "http://hosteddb.fightmetric.com/events/details/", 615, 615);
		//			scrapeOnce("schedule", "http://www.ufc.com/schedule/all/SE");
//		String htmlContent = null;
//		try {
//			htmlContent = util.IO.streamToString(new URL("http://www.ufc.com/schedule/all/SE").openConnection().getInputStream());
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		LinkedList<Pair<String, String>> regexes = new LinkedList<Pair<String, String>>();
//
//		regexes.add(new Pair<String, String>("event title", Settings.getInstance().getSchema().getTable("schedule").getColumn("event title").regex));
//		regexes.add(new Pair<String, String>("event tagline", Settings.getInstance().getSchema().getTable("schedule").getColumn("event tagline").regex));
//		regexes.add(new Pair<String, String>("event simple title", Settings.getInstance().getSchema().getTable("schedule").getColumn("event simple title").regex));
//		regexes.add(new Pair<String, String>("event date", Settings.getInstance().getSchema().getTable("schedule").getColumn("event date").regex));
//		regexes.add(new Pair<String, String>("event time", Settings.getInstance().getSchema().getTable("schedule").getColumn("event time").regex));
//		regexes.add(new Pair<String, String>("timezone", Settings.getInstance().getSchema().getTable("schedule").getColumn("timezone").regex));
//		System.out.println(regexes.getLast().getA() + ": " + regexes.getLast().getB());
//
//		LinkedHashMap<String, List<String>> result = scrape(htmlContent, regexes);
//		Iterator<Entry<String, List<String>>> itrResult = result.entrySet().iterator();
//		while (itrResult.hasNext()) {
//			Entry <String, List<String>> entry = itrResult.next();
//			System.out.print("Key: '" + entry.getKey() + "'");
//			System.out.print(" Values--> ");
//			Iterator<String> itrValues = entry.getValue().iterator();
//			int i = 0;
//			while(itrValues.hasNext()) {
//				System.out.print(" [" + i++ + "] '" + itrValues.next() + "'");				
//			}
//			System.out.println();
//		}
//
//	}
//

//
//	}
