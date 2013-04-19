package scraper;

import java.util.Stack;

import util.Logger;
import database.DbHandler;

public class OldTaskChain implements Runnable {
	Stack<OldTask> tasks = new Stack<OldTask>();
	DbHandler db = new DbHandler();
	
	public OldTaskChain() throws Exception {
		// runs bottom-up				
		tasks.push(new OldScraperTask("fighter-details", new Scraper[]{new FMScraper("fighter-details")}));
		tasks.push(new OldDataTask("compare-fighters"));		
		tasks.push(new OldScraperTask("fighters-index", new Scraper[]{new FMScraper("fighters-index")}));		
		tasks.push(new OldScraperTask("fight-details-rounds", new Scraper[]{new FMScraper("fight-details"), new FMScraper("rounds-winner"), new FMScraper("rounds-looser")}));
		tasks.push(new OldScraperTask("event-details-fights", new Scraper[]{new FMScraper("event-details"), new FMScraper("fights")})); 
		tasks.push(new OldDataTask("compare-events"));	
		tasks.push(new OldScraperTask("completed-events", new Scraper[]{new FMScraper("completed-events")}));
		tasks.push(new OldScraperTask("upcoming-events", new Scraper[]{new FMScraper("upcoming-events")}));		
	}

	@Override
	public void run() {
		OldTask curTask; 
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
