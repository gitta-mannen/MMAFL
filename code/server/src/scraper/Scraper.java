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

public class Scraper {
	private Scraper() {};		
	public static HashMap<String, Object> scrape(URL url, String table) throws MalformedURLException {
		HashMap<String, Object> values = new HashMap<String, Object>();
		try {
			String htmlContent = util.Io.streamToString(url.openConnection().getInputStream());			
			Iterator<Entry<String, Column>> itrColumns = Settings.getInstance().getSchema().getTable(table).getColumns().entrySet().iterator();						
			while (itrColumns.hasNext()) {
				Entry<String, Column> column = itrColumns.next();
				if (column.getValue().regex != null && column.getValue().regex.length() > 0) {	
					Pattern pattern = Pattern.compile(column.getValue().regex, Pattern.MULTILINE);
					Matcher matcher = pattern.matcher(htmlContent);						
					while(matcher.find() && matcher.groupCount() == 3) {	
						String match = matcher.group(2);						
							if (column.getValue().apptype.equals("date")) {
								values.put(column.getKey(), new SimpleDateFormat("MMM. dd, yyyy").parse(match) );
							} else if (column.getValue().apptype.equals("long") || column.getValue().apptype.equals("double") || column.getValue().apptype.equals("integer")) {
								values.put(column.getKey(), NumberFormat.getInstance(Locale.US).parseObject(match) );
							} else if (column.getValue().apptype.equals("string")){
								values.put(column.getKey(), match);
							} else {
								Logger.log("Type not regocgnized", true);
							}							
					}
				}
			}
		} catch (Exception e) {
			Logger.log(e.getStackTrace()[0].toString(), true);
		}		
		return values;
	}		
	
	public static void scrapeRangeToDb(String table, String baseUrl, int from, int to) throws MalformedURLException {
		DbHandler db = new DbHandler();									
		for (int id = from; id <= to; id++) {
			HashMap <String, Object> hm = scrape( new URL(baseUrl + Integer.toString(id)), table);
			hm.put("id", id);
			db.update(table, hm);
		}
		db.close();
	}

	public static void main(String[] args) throws MalformedURLException {
		scrapeRangeToDb("fighters", "http://hosteddb.fightmetric.com/fighters/details/", 3, 4);
		scrapeRangeToDb("events", "http://hosteddb.fightmetric.com/events/details/", 1, 2);		
	}
}
