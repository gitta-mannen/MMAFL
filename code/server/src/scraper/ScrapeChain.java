package scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Stack;

import util.Logger;
import util.Pair;

import database.DbHandler;

public class ScrapeChain implements Runnable {
	Stack<Task> tasks = new Stack<Task>();
	DbHandler db = new DbHandler();
	
	public ScrapeChain() throws MalformedURLException, IOException {	
		Pair<String, Scraper>[] scrapers = Pair.merge(new String[]{"completed-events"}, new Scraper[]{new FMScraper("completed-events")});		
		tasks.push(new ScraperTask("completed-events", scrapers));		
	}

	@Override
	public void run() {
		Task curTask; 
		while (!tasks.isEmpty())
			try {				
				curTask = tasks.pop();
				System.out.println("popping stack element: " + curTask.getName());
				curTask.doTask();
			} catch (Exception e) {
				Logger.log(e, true);
			}
	}
}
