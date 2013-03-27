package scraper;

import java.util.Stack;

import util.Logger;
import database.DbHandler;

public class TaskChain implements Runnable {
	Stack<Task> tasks = new Stack<Task>();
	DbHandler db = new DbHandler();
	
	public TaskChain() throws Exception {
		// runs bottom-up				
		tasks.push(new ScraperTask("fighter-details", new Scraper[]{new FMScraper("fighter-details")}));
		tasks.push(new DataTask("compare-fighters"));		
		tasks.push(new ScraperTask("fighters-index", new Scraper[]{new FMScraper("fighters-index")}));		
		tasks.push(new ScraperTask("fight-details-rounds", new Scraper[]{new FMScraper("fight-details"), new FMScraper("rounds-winner"), new FMScraper("rounds-looser")}));
		tasks.push(new ScraperTask("event-details-fights", new Scraper[]{new FMScraper("event-details"), new FMScraper("fights")})); 
		tasks.push(new DataTask("compare-events"));	
		tasks.push(new ScraperTask("completed-events", new Scraper[]{new FMScraper("completed-events")}));
		tasks.push(new ScraperTask("upcoming-events", new Scraper[]{new FMScraper("upcoming-events")}));		
	}

	@Override
	public void run() {
		Task curTask; 
		Logger.log("Started popping tasks", true);		
		try {
			while (!tasks.isEmpty()) {
				curTask = tasks.pop();
				Logger.log("popping stack element: " + curTask.getName(), true);
				curTask.doTask();
			}
		} catch (Exception e) {
			Logger.log(e, true);
		}
		Logger.log("Task chain completed", true);
	}
}
