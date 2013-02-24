package Scraper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;

import database.Event;
import database.StatsHandler;

public class IterativeEventScraper {
	private String eventOrganization;
	private String eventName;
	private String eventLocation;
	private String eventDate;
	private String eventAttendance;
	private int Id;
	private StatsHandler db = new StatsHandler();

	public IterativeEventScraper(URL url, int Id) throws MalformedURLException {
		try {
			this.Id = Id;
			URLConnection yc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			
			while ((inputLine = in.readLine()) != null) {
				//Scrape: Organization
				if (inputLine.contains(">ORGANIZATION</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.replaceAll("Events > ", "");
					eventOrganization = inputLine.trim();
					//eventOrganization = "UFC";
					
				}
				//Scrape: Event Name
				if (inputLine.contains("Events</a>")) {
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.replaceAll("Events > ", "");
					eventName = inputLine.trim();
				}
				//Scrape: Event Location
				if (inputLine.contains(">LOCATION</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					eventLocation = inputLine.trim();
				}
				//Scrape: Event Date
				if (inputLine.contains("DATE</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					eventDate = inputLine.trim();
				}
				//Scrape: event Attendance
				if (inputLine.contains(">ATTENDANCE</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.replaceAll("Events > ", "");
					eventAttendance = inputLine.trim();
				}
			}
			in.close();
			eventToDb();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
    public void eventToDb() {
    	// used once for all scrapers
		StatsHandler db = new StatsHandler();
		// once per record (i.e once per event)
		db.update(new Event(Id, eventName, eventDate, eventLocation, eventOrganization, eventAttendance));
		System.out.println("Id: " + Id + " Event: " + eventName);
		System.out.println("Done");
    }
    public void closeEventToDb() {
    	db.close();
    	System.out.println("\n" + "Done writing to DB");
    }
	public String getEvent() {
		return  eventOrganization + "\n" + eventName + "\n" + eventLocation + "\n" + eventDate + "\n" + eventAttendance;
	}
}
