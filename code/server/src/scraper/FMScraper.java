package scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Stack;

import com.sun.jmx.snmp.tasks.TaskServer;

import settings.Constants.AppType;
import settings.Settings;
import util.Logger;

public class FMScraper implements Runnable {
	Stack<FMScraperTask> tasks = new Stack<FMScraperTask>();

	public FMScraper() throws MalformedURLException, IOException {
		tasks.push(new DetailsScraperTask("event-details"));	
		tasks.push(new TableComparator("compare-events"));	
		tasks.push(new RootScraperTask("completed-events"));
		tasks.push(new RootScraperTask("upcoming-events"));
			
	}

	@Override
	public void run() {
		while (!tasks.isEmpty())
			try {
				System.out.println("popping stack element: " + tasks.peek().taskName);
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
	String delimiter, url;	
	public RootScraperTask(String taskName) throws MalformedURLException, IOException {
		super(taskName);		
		delimiter = Settings.getNodeText("scrapers:" + taskName + ":index-delimiter")[0];
		url = Settings.getNodeText("scrapers:" + taskName + ":source-url")[0];
	}

	@Override
	public void doTask() throws Exception {
//		htmlContent = Scraper.textFromUrl(url); // testing from file
		String htmlContent = util.IO.fileToString(Settings.getNodeText("scrapers:" + taskName + ":source-file")[0]);		
		String[] splitContent = Scraper.findFirst(htmlContent, Settings.getNodeText("scrapers:" + taskName + ":table-regex")[0]).split(delimiter);
		
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
	String prefixUrl;
	
	public DetailsScraperTask(String taskName) {
		super(taskName);
		prefixUrl = Settings.getNodeText("scrapers:" + taskName + ":url-prefix")[0];
	}
	@Override
	public void doTask() throws MalformedURLException, IOException, Exception {
		Object[][] keyUrl = db.executePs("select-dirty", "rowcount");
		
		db.setAutoCommit(false);
		for (int i = 0; i < keyUrl.length; i++) {
//			String htmlContent = Scraper.textFromUrl(prefixUrl + keyUrl[i][1]);
			String htmlContent = util.IO.fileToString(Settings.getNodeText("scrapers:" + taskName + ":source-file")[0]);	
			String[] matches = Scraper.scrapeFirst(htmlContent, regexes);
			Object[] converted = FMScraper.stringToObject(matches, types);
			
			//insert foreign key and updated flag into parameter list
			Object[] tempParam = new Object[converted.length + 2];	
			tempParam[tempParam.length-2] = new Integer(1);	// seccond last pos
			tempParam[tempParam.length-1] = keyUrl[i][0];	//last pos
			System.arraycopy(converted, 0, tempParam, 0, converted.length);
			db.executePs("update", tempParam);
		}
		db.setAutoCommit(true);
	}	
	
	
}

class TableComparator extends FMScraperTask{
	public TableComparator(String taskName) {
		super(taskName);
	}

	@Override
	public void doTask() throws MalformedURLException, IOException, Exception {
		db.executePs("insert-difference");
		db.executePs("clear");
	}	
	
	
}