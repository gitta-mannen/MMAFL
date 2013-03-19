package scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Stack;

import settings.Constants.AppType;
import settings.Settings;
import util.Logger;

public class FMScraper implements Runnable {
	Stack<FMScraperTask> tasks = new Stack<FMScraperTask>();

	public FMScraper() throws MalformedURLException, IOException {		
		tasks.push(new RootScraperTask("completed-events"));
		tasks.push(new RootScraperTask("upcoming-events"));		
	}

	@Override
	public void run() {
		while (!tasks.isEmpty())
			try {
				System.out.println("popping stack");
				tasks.pop().doTask();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}	
	}

	/**
	 * Parses a string and converts it to the appropriate Object.
	 * @param s - String to be converted.
	 * @param type - Enumerator indicating the type.
	 * @return - Object created by parsing or null if type isn't recognized.
	 * @throws ParseException
	 */
	public static Object stringToObject (String s, AppType type) throws ParseException {
		switch (type) {
		case DATE:
			return (new SimpleDateFormat("MMM. dd, yyyy").parse(s));
		case LONG: case DOUBLE: case INTEGER:
			return (NumberFormat.getInstance(Locale.US).parseObject(s));
		case STRING:
			return s;
		default:
			Logger.log("Type not recognized", true);
			return null;
		}
	}

	public static Object[] stringToObject(String[] strings, AppType[] types) throws ParseException {		
		if (strings.length != types.length) { 
			throw new IndexOutOfBoundsException("strings length doesn't match types length."); 
		}
		
		Object[] results = new Object[strings.length];
		for (int i = 0; i < strings.length; i++) {
			results[i] = stringToObject(strings[i], types[i]);
		}
		return results;
	}
}

class RootScraperTask extends FMScraperTask {
	String updateString, delimiter, htmlContent, url;
	String[] splitContent;
	public RootScraperTask(String taskName) throws MalformedURLException, IOException {
		super(taskName);		
		delimiter = Settings.getNodeText("scrapers:" + taskName + ":index-delimiter")[0];
		url = Settings.getNodeText("scrapers:" + taskName + ":source-url")[0];
	}

	@Override
	public void doTask() throws Exception {
//		htmlContent = Scraper.textFromUrl(url); // testing from file
		htmlContent = util.IO.fileToString(Settings.getNodeText("scrapers:" + taskName + ":source-file")[0]);		
		splitContent = Scraper.findFirst(htmlContent, Settings.getNodeText("scrapers:" + taskName + ":table-regex")[0]).split(delimiter);
		
		db.setAutoCommit(false);
		for (int i = 1; i < splitContent.length; i++) {
			String[] matches = Scraper.scrapeFirst(splitContent[i], regexes);
			Object[] converted = FMScraper.stringToObject(matches, types);
			db.executePs("insert", converted);
		}
		db.setAutoCommit(true);
	}

}

class LinkedIndexScraperTask extends FMScraperTask {
	String updateString, childTask;
	public LinkedIndexScraperTask(String taskName, Stack<FMScraperTask> tasks) {
		super(taskName);

	}

}

class IndexScraperTask extends FMScraperTask{
	String updateString, delimiter;

	public IndexScraperTask(String taskName, String url, String foreignKey) {
		super(taskName);


	}

}

class DetailsScraperTask extends FMScraperTask{
	String updateString;
	public DetailsScraperTask(String taskName, String url, String foreignKey) {
		super(taskName);

	}	
}

class DbComparator extends FMScraperTask{
	// ** fult att ge den en referens till stacken, gör om, gör rätt **
	public DbComparator(String taskName, Stack<FMScraperTask> tasks) {
		super(taskName);
		// TODO Auto-generated constructor stub
	}	
}