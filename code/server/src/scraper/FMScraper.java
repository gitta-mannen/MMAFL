package scraper;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import settings.Settings;
import settings.Constants.AppType;
import util.Logger;

public class FMScraper extends Scraper {
	private final String delimiter;
	private final String tablePattern;
	
	public FMScraper (String name) {
		super(name);
		delimiter = Settings.getNodeText("scrapers:" + name + ":index-delimiter")[0];
		tablePattern = Settings.getNodeText("scrapers:" + name + ":table-patter")[0];
	}
	
	@Override
	protected String[] preProcess(String text) {
		return Scraper.findFirst(text, tablePattern).split(delimiter);
	}
		
	/**
	 * Parses a string and converts it to the appropriate Object.
	 * @param s - String to be converted.
	 * @param type - Enumerator indicating the type.
	 * @return - Object created by parsing or null if type isn't recognized.
	 * @throws ParseException
	 */
	protected Object stringToObject (String s, AppType type) throws ParseException {
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

	protected Object[] stringToObject(String[] strings, AppType[] types) throws ParseException {		
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

//
//class RootScraperTask extends FMScraperTask implements Task {
//	String pageContent;
//	Object[][] results;	
//	int resultRow = 0;
//	
//	public RootScraperTask(String taskName) throws MalformedURLException, IOException {
//		super(taskName);		
//	}
//
//	@Override
//	public void doTask() throws FileNotFoundException, ParseException  {		
//		String[] splitContent = Scraper.findFirst(pageContent, Settings.getNodeText("scrapers:" + taskName + ":table-regex")[0]).split(delimiter);
//		LinkedList<Object[]> tempResults = new LinkedList<Object[]>();
//		for (int i = 1; i < splitContent.length; i++) {
//			String[] matches = Scraper.scrapeFirst(splitContent[i], regexes);
//			Object[] converted = FMScraper.stringToObject(matches, types);
//			tempResults.add(converted);			
//		}		
//		
//		results = new Object[tempResults.size()][];
//		for (int i = 0; i < tempResults.size(); i++) {
//			results[i] = tempResults.get(i);
//		}
//	}	
//}
//
//class LinkedScraperTask extends FMScraperTask {	
//	String prefixUrl;
//	public LinkedScraperTask(String taskA, String taskB) {
//		super(taskName);
//		prefixUrl = Settings.getNodeText("scrapers:" + taskName + ":url-prefix")[0];
//	}
//	String[] splitContent = Scraper.findFirst(pageContent, Settings.getNodeText("scrapers:" + taskName + ":table-regex")[0]).split(delimiter);
//	LinkedList<Object[]> tempResults = new LinkedList<Object[]>();
//	for (int i = 1; i < splitContent.length; i++) {
//		String[] matches = Scraper.scrapeFirst(splitContent[i], regexes);
//		Object[] converted = FMScraper.stringToObject(matches, types);
//		tempResults.add(converted);			
//	}		
//	
//	results = new Object[tempResults.size()][];
//	for (int i = 0; i < tempResults.size(); i++) {
//		results[i] = tempResults.get(i);
//	}
//	
//	@Override
//	public boolean hasResult() {
//		return resultRow < results.length ? true : false;
//	}
//
//	@Override
//	public Object[] getResult() {
//		return results[resultRow++];
//	}
//
//	@Override
//	public String getName() {
//		return scraperName;
//	}
//
//	@Override
//	public Pair<String, String> getUpdateStatement() {
//		return new Pair<String, String> (Settings.getNodeText("scrapers:" + scraperName + ":update-statement:name")[0],
//				Settings.getNodeText("scrapers:" + scraperName + ":update-statement:statement")[0]);
//	}
//
//	@Override
//	public String getSourceType() {
//		return Settings.getNodeText("scrapers:" + scraperName + ":source-type")[0];
//	}
//
//	@Override
//	public String getSource() {
//		return Settings.getNodeText("scrapers:" + scraperName + ":source-" + getSourceType())[0]; 
//	}
//
//	@Override
//	public void setSource(String pageContent) {
//		this.pageContent = pageContent;  
//	}
//}	
//	
////}
//
////class IndexScraperTask extends FMScraperTask{
////	public IndexScraperTask(String taskName) {
////		super(taskName);
////		
////	}
////	@Override
////	public void doTask() throws MalformedURLException, IOException, Exception {
////		
////		
////		db.setAutoCommit(false);
////		for (int i = 0; i < keyUrl.length; i++) {
//////			String htmlContent = Scraper.textFromUrl(prefixUrl + keyUrl[i][1]);
////			String htmlContent = util.IO.fileToString(Settings.getNodeText("scrapers:" + taskName + ":source-file")[0]);	
////			String[] matches = Scraper.scrapeFirst(htmlContent, regexes);
////			Object[] converted = FMScraper.stringToObject(matches, types);
////			
////			//insert foreign key into parameter list
////			Object[] tempParam = new Object[converted.length + 1];	
////			tempParam[0] = keyUrl[i][0];	//first pos
////			System.arraycopy(converted, 0, tempParam, 1, converted.length);
////			db.executePs("insert", tempParam);
////		}
////		db.setAutoCommit(true);
////		db.close();
////	}	
////}
//
////class DetailsScraperTask extends FMScraperTask{
////	String prefixUrl;
////	
////	public DetailsScraperTask(String taskName) {
////		super(taskName);
////		prefixUrl = Settings.getNodeText("scrapers:" + taskName + ":url-prefix")[0];
////	}
////	@Override
////	public void doTask() throws MalformedURLException, IOException, Exception {
////		Object[][] keyUrl = db.executePs("select-dirty", "rowcount");
////		
////		db.setAutoCommit(false);
////		for (int i = 0; i < keyUrl.length; i++) {
//////			String htmlContent = Scraper.textFromUrl(prefixUrl + keyUrl[i][1]);
////			String htmlContent = util.IO.fileToString(Settings.getNodeText("scrapers:" + taskName + ":source-file")[0]);	
////			String[] matches = Scraper.scrapeFirst(htmlContent, regexes);
////			Object[] converted = FMScraper.stringToObject(matches, types);
////			
////			//insert foreign key and updated flag into parameter list
////			Object[] tempParam = new Object[converted.length + 2];	
////			tempParam[tempParam.length-2] = new Integer(1);	// seccond last pos
////			tempParam[tempParam.length-1] = keyUrl[i][0];	//last pos
////			System.arraycopy(converted, 0, tempParam, 0, converted.length);
////			db.executePs("update", tempParam);
////		}
////		db.setAutoCommit(true);
////		db.close();
////	}	
////	
////	
////}
//
////class TableComparator implements Task {
////	public TableComparator(String taskName) {
////		super(taskName);
////	}
////
////	@Override
////	public void doTask() throws MalformedURLException, IOException, Exception {
////		db.executePs("insert-difference");
////		db.executePs("clear");
////		db.close();
////	}	
////	
////	
//}
