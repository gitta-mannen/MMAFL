package Scraper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;

import database.Event;
import database.StatsHandler;

public class EventScraper {
	private String eventOrganization;
	private String eventName;
	private String eventLocation;
	private String eventDate;
	private String eventAttendance;

	public EventScraper(URL url) throws MalformedURLException {

		try {
			URLConnection yc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				// Scrape: Organization
				if (inputLine.contains(">ORGANIZATION</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.replaceAll("Events > ", "");
					eventOrganization = inputLine.trim();
					// eventOrganization = "UFC";

				}
				// Scrape: Event Name
				if (inputLine.contains("Events</a>")) {
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.replaceAll("Events > ", "");
					eventName = inputLine.trim();
				}
				// Scrape: Event Location
				if (inputLine.contains(">LOCATION</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					eventLocation = inputLine.trim();
				}
				// Scrape: Event Date
				if (inputLine.contains("DATE</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					eventDate = inputLine.trim();
				}
				// Scrape: event Attendance
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
		// db.update(new Event(1, "UFC the Tuna Sandwich", "2012-02-01",
		// "Hålanda", "PENIS", "50000000"));
		db.update(new Event(3, eventName, eventDate, eventLocation,
				eventOrganization, eventAttendance));
		System.out.println(eventLocation + "\n" + eventOrganization);
		// once for all scrapers
		db.close();
	}

	public String getEvent() {
		return eventOrganization + "\n" + eventName + "\n" + eventLocation
				+ "\n" + eventDate + "\n" + eventAttendance;
	}
}
