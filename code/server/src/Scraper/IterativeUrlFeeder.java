package Scraper;
import java.net.MalformedURLException;
import java.net.URL;

import Scraper.IterativeEventScraper;

public class IterativeUrlFeeder {
	
	final String fighterUrl = "http://hosteddb.fightmetric.com/fighters/details/";
	final String eventUrl = "http://hosteddb.fightmetric.com/events/details/";
	//int fighterUrlOffset = 1518;
	//int eventUrlOffset = 121;
	private int start, stop;
	private String type;

	public void setInterval(String type, int start, int stop) {
		this.type = type;
		this.start = start;
		this.stop = stop;
	}
	
	public void setFightersToDb() {
		//IterativeEventScraper eventScraper = new IterativeEventScraper();
	}
	public void setEventsToDb() {
		for( int i = start; i <= stop; i++ ) {
			try {
				IterativeEventScraper ies = new IterativeEventScraper(new URL((eventUrl+i)), i);
				//System.out.println(eventUrl+1);
				if(i == stop) {ies.closeEventToDb();}
			} catch (MalformedURLException e) {
				System.out.println("Invalid URL, might be caused by the event-interval.");
				e.printStackTrace();
			}
		}
		
	}
	
	public String getInterval() {
		return (type + " " + start + " - " + stop + " will be pushed into the DB.");
	}

}
