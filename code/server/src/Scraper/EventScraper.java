package Scraper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;

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
				//Scrape: Organization
				if (inputLine.contains(">ORGANIZATION</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.replaceAll("Events > ", "");
					eventOrganization = inputLine;
				}
				//Scrape: Event Name
				if (inputLine.contains("Events</a>")) {
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.replaceAll("Events > ", "");
					eventName = inputLine;
				}
				//Scrape: Event Location
				if (inputLine.contains(">LOCATION</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					eventLocation = inputLine;
				}
				//Scrape: Event Date
				if (inputLine.contains("DATE</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					eventDate = inputLine;
				}
				//Scrape: event Attendance
				if (inputLine.contains(">ATTENDANCE</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					//inputLine = inputLine.replaceAll("Events > ", "");
					eventAttendance = inputLine;
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getEvent() {
		return  getOrganization() + "\n" + getEventName() + "\n" + getEventLocation() + "\n" + getEventDate() + "\n" + getAttendance();
	}
	
	public String getOrganization() {
		return eventOrganization;
	}
	public String getEventName() {
		return eventName;
	}
	public String getEventLocation() {
		return eventLocation;
	}
	public String getEventDate() {
		return eventDate;
	}
	public String getAttendance() {
		return eventAttendance;
	}

}
