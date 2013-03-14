package scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.DbHandler;
import settings.*;
import util.*;


abstract class Scraper {
	public Scraper() {}		
	// Scrapes the given URL using regex taken from the settings object (which gets it from the xml settings file)
	public static HashMap<String, Object> scrapeTable(URL url, String table) {
		HashMap<String, Object> values = new HashMap<String, Object>();
		try {
			// Opens a stream to the given URL and copies the html content in to a String
			String htmlContent = util.IO.streamToString(url.openConnection().getInputStream());		
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
								Logger.log("Type not recognized", true);
							}							
					}
				}
			}
		} catch (Exception e) {
			Logger.log(e.getStackTrace()[0].toString(), true);
		}		
		return values;
	}		
	
	
	public static LinkedHashMap<String, String> scrapeFirst(String text, List<Pair<String, String>> keyRegexList) {
		LinkedHashMap<String, String> resultMap = new LinkedHashMap<String, String>(keyRegexList.size());		
		Iterator <Pair<String, String>> itrKeyVal = keyRegexList.iterator();
		while (itrKeyVal.hasNext()) {
			Pair<String, String> entry = itrKeyVal.next();
			resultMap.put(entry.getA(), scraper.Scraper.findFirst(text, entry.getB()) );			
		}
		
		return resultMap;
	}
	
	public static LinkedHashMap<String, List<String>> scrape(String text, List<Pair<String, String>> keyRegexList) {
		System.out.println("scrape");
		LinkedHashMap<String, List<String>> resultMap = new LinkedHashMap<String, List<String>>();		
		Iterator <Pair<String, String>> itrKeyVal = keyRegexList.iterator();
		System.out.println("scrape while");
		while (itrKeyVal.hasNext()) {			
			Pair<String, String> entry = itrKeyVal.next();
			System.out.println("scrape " + entry.getA() + " " + entry.getB());
			resultMap.put(entry.getA(), scraper.Scraper.findAll(text, entry.getB()) );			
		}
		
		System.out.println("scrape return ");
		return resultMap;
	}
	
	// Calls the scrape method for an id range of <from-to>. Also takes the table name and base URL as arguments
	public static void scrapeRangeToDb(String table, String baseUrl, int from, int to) throws MalformedURLException {
		DbHandler db = new DbHandler();									
		for (int id = from; id <= to; id++) {
			HashMap <String, Object> hm = scrapeTable( new URL(baseUrl + Integer.toString(id)), table);
			hm.put("id", id);
			db.update(table, hm);
		}
		db.close();
	}
	
	public static void scrapeOnce(String table, String baseUrl) throws MalformedURLException {
		DbHandler db = new DbHandler();	
		db.resetTables(true);
			db.update(table, scrapeTable( new URL(baseUrl), table));
		db.close();
	}
	
	public static String textFromUrl (String url) throws MalformedURLException, IOException {
		return util.IO.streamToString( (new URL(url)).openConnection().getInputStream());
	}
	/**
	 * Finds the first match for a given regex in the given string.
	 * Assumes the regex has three groups and extracts the middle one.
	 * @param text
	 * @param regex
	 * @param group
	 * @return
	 */
	public static String findFirst (String text, String regex) {
		Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE).matcher(text);
		matcher.find();
		return matcher.group(2);								
	}
	/**
	 * Finds all matches for the given regex in the given string.
	 * Assumes the regex has three groups and extracts the middle one.
	 * @param text
	 * @param regex
	 * @param group
	 * @return
	 */
	public static LinkedList<String> findAll (String text, String regex) {
		LinkedList<String> list = new LinkedList<String>();
		Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE).matcher(text);
		while(matcher.find() && matcher.groupCount() == 3) {	
			list.add(matcher.group(2));								
		}
		
		return list;
	}
	

}
