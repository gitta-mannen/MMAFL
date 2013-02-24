package Scraper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;

import database.Event;
import database.StatsHandler;

public class IterativeEventScraper {
	
	private int id;
	private String eventOrganization;
	private String eventName;
	private String eventLocation;
	private String eventDate;
	private String eventAttendance;
	
	private StatsHandler db = new StatsHandler();

	public IterativeEventScraper(URL url, int Id) throws MalformedURLException {
		try {
			this.id = Id;
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
					inputLine = inputLine.trim();
					eventDate = dateConverter(inputLine);
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
	public String dateConverter(String date) {
		String year = date.substring(date.length()-5).trim();
		String month = date.substring(0,4).trim();
		String day = 0 + date.substring(month.length()+1,month.length()+year.length()-2);
		
		/*if(day.contains(",")) {
			day = 0 + day.substring(0,1);
			day = day.trim();
		}*/
		
        switch (month) {
            case "Jan.":  month = "01";
                     break;
            case "Feb.":  month = "02";
                     break;
            case "Mar.":  month = "03";
                     break;
            case "Apr.":  month = "04";
                     break;
            case "May.":  month = "05";
                     break;
            case "Jun.":  month = "06";
                     break;
            case "Jul.":  month = "07";
                     break;
            case "Aug.":  month = "08";
                     break;
            case "Sep.":  month = "09";
                     break;
            case "Oct.":  month = "10";
                     break;
            case "Nov.":  month = "11";
                     break;
            case "Dec.":  month = "12";
                     break;
            default: month = "Invalid month";
                     break;
        }
        return year+"-"+month+"-"+day;
	}
    public void eventToDb() {
    	// used once for all scrapers
		StatsHandler db = new StatsHandler();
		// once per record (i.e once per event)
		db.update(new Event(id, eventName, eventDate, eventLocation, eventOrganization, eventAttendance));
		System.out.println("Event: " + id + " - " + eventName);
    }
    public void closeEventToDb() {
    	db.close();
    	System.out.println("---------------------------\nDone.");
    }
	public String getEvent() {
		return  eventOrganization + "\n" + eventName + "\n" + eventLocation + "\n" + eventDate + "\n" + eventAttendance;
	}
}
