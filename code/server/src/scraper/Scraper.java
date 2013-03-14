package scraper;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.DbHandler;
import settings.Column;
import settings.Settings;
import util.Logger;

// Private constructor, can't be instantiated.
public class Scraper {
	private Scraper() {};		
	// Scrapes the given URL using regex taken from the settings object (which gets it from the xml settings file)
	public static HashMap<String, Object> scrape(URL url, String table) throws MalformedURLException {
		HashMap<String, Object> values = new HashMap<String, Object>();
		try {
			// Opens a stream to the given URL and copies the html content in to a String
			String htmlContent = util.Io.streamToString(url.openConnection().getInputStream());		
			// Iterates over the columns in the given table
			Iterator<Entry<String, Column>> itrColumns = Settings.getInstance().getSchema().getTable(table).getColumns().entrySet().iterator();						
			while (itrColumns.hasNext()) {
				Entry<String, Column> column = itrColumns.next();
				// Checks if a regex entry exists in the xml file for the given column and executes it on the html content.
				if (column.getValue().regex != null && column.getValue().regex.length() > 0) {	
					Pattern pattern = Pattern.compile(column.getValue().regex, Pattern.MULTILINE);
					Matcher matcher = pattern.matcher(htmlContent);
					// The regex matches 3 groups, before the data, the data (e.g. stance), and after the data.
					// The second group (the data) is put in the string 'match'.
					while(matcher.find() && matcher.groupCount() == 3) {	
						String match = matcher.group(2);				
							// The data is formated and converted to the proper type then put in the hashmap with the column name as key.
							if (column.getValue().apptype.equals("date")) {
								values.put(column.getKey(), new SimpleDateFormat("MMM. dd, yyyy").parse(match) ); // parses the date format used on Fightmetric
							} else if (column.getValue().apptype.equals("long") || column.getValue().apptype.equals("double") || column.getValue().apptype.equals("integer")) {
								values.put(column.getKey(), NumberFormat.getInstance(Locale.US).parseObject(match) ); // Parses numbers written in a US locale
							} else if (column.getValue().apptype.equals("string")){
								values.put(column.getKey(), match);
							} else {
								Logger.log("Type not regcognized", true);
							}							
					}
				}
			}
		} catch (Exception e) {
			Logger.log(e.getStackTrace()[0].toString(), true);
		}		

//		System.out.println("name: " 	+ values.get("name"));
//		System.out.println("nickname: " + values.get("nickname"));
//		System.out.println("age: " 		+ values.get("age"));
//		System.out.println("stance: " 	+ values.get("stance"));
//		
//		System.out.println("wins: " 	+ values.get("wins"));
//		System.out.println("losses: " 	+ values.get("losses"));
//		System.out.println("draws: " 	+ values.get("draws"));
//		System.out.println("nc: " 		+ values.get("no contest"));
//		
//		System.out.println("weight: " 	+ values.get("weight"));
//		System.out.println("height: " 	+ values.get("height"));
//		System.out.println("reach: " 	+ values.get("reach"));
//		
//		System.out.println("strikes landed: "		+ values.get("strikes landed"));
//		System.out.println("striking accuracy: "	+ values.get("striking accuracy"));
//		System.out.println("strikes absorbed: "		+ values.get("strikes absorbed"));
//		System.out.println("strike defense: "	 	+ values.get("strike defense"));
//		
//		System.out.println("takedown average: "		+ values.get("takedown average"));
//		System.out.println("takedown defense: " 	+ values.get("takedown defense"));
//		System.out.println("takedown accuracy: "	+ values.get("takedown accuracy"));
//		System.out.println("----------------------------------------------------------------");
//		System.out.println("\n");
		System.out.println("id: " 	+ values.get("id"));
		System.out.println("event name: " 	+ values.get("name"));

		
		return values;
	}
	
	
	
	// Calls the scrape method for a range of <from-to>. Takes the table name and base url as arguments
	public static void scrapeRangeToDb(String table, String baseUrl, int from, int to) throws MalformedURLException {
		DbHandler db = new DbHandler();	
		db.resetTables(true);
		for (int id = from; id <= to; id++) {
			HashMap <String, Object> hm = scrape( new URL(baseUrl + Integer.toString(id)), table);
			hm.put("id", id);
			db.update(table, hm);
		}
		db.close();
	}
	
	// test method for scraping iteratively
	public static void main(String[] args) throws MalformedURLException {
		//scrapeRangeToDb("fighters", "http://hosteddb.fightmetric.com/fighters/details/", 439, 442);
		//scrapeRangeToDb("events", "http://hosteddb.fightmetric.com/events/details/", 100, 105);	
		//scrapeRangeToDb("rounds", "http://hosteddb.fightmetric.com/fights/index/", 100, 105);		
		scrapeRangeToDb("fights", "http://hosteddb.fightmetric.com/fights/index/", 4060, 4060);		
	}
}
