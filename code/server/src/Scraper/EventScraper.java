package Scraper;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventScraper {
	private String organization;
	private String eventName;
	private String eventLocation;
	private String eventDate;
	private ArrayList<String> fightcardArray;

	public EventScraper(URL url) throws MalformedURLException {

		fightcardArray = new ArrayList<String>();
		try {
			URLConnection yc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			String fighterName = "";
			while ((inputLine = in.readLine()) != null) {
				//Scrape: Organization
				if (inputLine.contains(">ORGANIZATION</td>")) {
					inputLine = in.readLine();
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					inputLine = inputLine.replaceAll("Events > ", "");
					//organization = inputLine;
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
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getFightcardArray() {
		// Make arrayList to stringArray
		String[] fighterArrayToString = fightcardArray.toArray(new String[fightcardArray.size()]);

		// Put every fighter as elements by split at divederToken
		List<String> temp1 = new ArrayList<String>();
		List<String> temp2 = new ArrayList<String>();
		String dividerToken = ".vs";
		for (int i = 0; i < fighterArrayToString.length; i++) {
			temp1 = Arrays.asList(fighterArrayToString[i].split(dividerToken));
			temp2.addAll(temp1);
		}

		return  fightcardArray;
	}

	public String getFightcard() {
		String fightcard = "";
		String tempString = "";
		int i = 0;
		for (String fighter : fightcardArray) {
			if (i % 2 == 0) {
				fightcard += (fighter + " vs. ");
				i++;
			} else {
				fightcard += (tempString + (fighter) + " \n\n");
				i++;
			}
		}
		if( fightcard.isEmpty() )
		{
			return "FightcardArray seems to be empty.";
		}
		return  getEventName() + "\n" + getEventLocation() + "\n" + getEventDate() + "\n\n" + fightcard;
	}
	
	public String getOrganization() {
		return organization;
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

}
