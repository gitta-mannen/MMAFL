package Scraper;
import java.net.MalformedURLException;
import java.net.URL;

import Scraper.IterativeEventScraper;

public class IterativeUrlFeeder {
	
	final String fighterUrl = "http://hosteddb.fightmetric.com/fighters/details/";
	final String eventUrl = "http://hosteddb.fightmetric.com/events/details/";
	private int start, stop;
	private String type;

	public void setInterval(String type, int start, int stop) {
		this.type = type;
		this.start = start;
		this.stop = stop;
	}
	
	public void setFightersToDb() {
		for( int i = start; i <= stop; i++ ) {
			try {
				IterativeFighterScraper fies = new IterativeFighterScraper(new URL((fighterUrl+i)), i);
				//System.out.println(eventUrl+1);
				if(i == stop) {fies.closeEventToDb();}
			} catch (MalformedURLException e) {
				System.out.println("Invalid URL, might be caused by the fighter-interval.");
				e.printStackTrace();
			}
		}
		
	}
	public void setEventsToDb() {
		for( int i = start; i <= stop; i++ ) {
			try {
				IterativeEventScraper eies = new IterativeEventScraper(new URL((eventUrl+i)), i);
				//System.out.println(eventUrl+1);
				if(i == stop) {eies.closeEventToDb();}
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
