package Scraper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;
import java.util.HashMap;

import database.Event;
import database.StatsHandler;

public class EventScraper {
	private String eventOrganization;
	private String eventName;
	private String eventLocation;
	private String eventDate;
	private String eventAttendance;
	private String table = "events";
	private String id = "1";
	private StatsHandler db = new StatsHandler();
	private HashMap<String, String> values = new HashMap<String, String>();

	public EventScraper(URL url) throws MalformedURLException {
			values.put("id", id);
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
					values.put("organization", eventOrganization);
					

				}
				// Scrape: Event Name
				if (inputLine.contains("Events</a>")) {
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.replaceAll("Events > ", "");
					eventName = inputLine.trim();
					values.put("name", eventName);
				}
				// Scrape: Event Location
				if (inputLine.contains(">LOCATION</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					eventLocation = inputLine.trim();
					values.put("location", eventLocation);
				}
				// Scrape: Event Date
				if (inputLine.contains("DATE</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					eventDate = inputLine.trim();
					values.put("date", eventDate);
				}
				// Scrape: event Attendance
				if (inputLine.contains(">ATTENDANCE</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.replaceAll("Events > ", "");
					eventAttendance = inputLine.trim();
					values.put("attendance", eventAttendance);
				}
			}
			in.close();
			db.update(table, values);
			db.close();
			System.out.println(values);
			//eventToDb();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void eventToDb() {
		// used once for all scrapers
		//StatsHandler db = new StatsHandler();
		/*db.update(new Event(3, eventName, eventDate, eventLocation,
				eventOrganization, eventAttendance));
		System.out.println(eventLocation + "\n" + eventOrganization);
		// once for all scrapers
		db.close();*/
	}

	public String getEvent() {
		return eventOrganization + "\n" + eventName + "\n" + eventLocation
				+ "\n" + eventDate + "\n" + eventAttendance;
	}
}
