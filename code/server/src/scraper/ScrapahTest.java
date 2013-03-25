package scraper;

import java.util.Arrays;
import settings.Settings;
import util.Pair;
import util.WebDiskCache;
import database.DbHandler;

public class ScrapahTest {

	public ScrapahTest() {
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception { 
//		new DbHandler().buildTables(true);
		new Thread(new TaskChain()).run();
//		testScraper();
//		testRegex();	
//		testFlags();	
//		testTask();	
//		tesFiles();	

	}
	public static void tesFiles() throws Exception {
		WebDiskCache wb = new WebDiskCache("http://hosteddb.fightmetric.com/", System.getProperty("user.dir") + "\\www\\fightmetric");
		String page = wb.getPage("/events/index/date/asc/1/all");
		System.out.println(page.length());
	}
	
	public static void testTask() throws Exception {
		ScraperTask task = new ScraperTask("completed-events", new Scraper[]{new FMScraper("completed-events")});

	}
	
	public static void testFlags() {
		int rows[] = {0};
		int rowFlag = 0;
		for (int i = 0; i < rows.length; i++) {
			rowFlag |= 1 << (rows[i]);
			System.out.println(rowFlag);
		}		
		
		
		
		for (int i = 0; i < 15; i++) {
			if ((rowFlag >> i & 1) == 1 ) {
				System.out.println("flag for row " + i + " set");
			}
		}
	}
	
	public static void testScraper() throws Exception {
//		String text = util.IO.fileToString("E:\\projekt\\MMAFL\\code\\server\\www\\fm_c_fight.htm");
//		Object[][] results = new FMScraper("fight-details").scrape(text);
//		System.out.println("\t" + Arrays.deepToString(results));							

	}
	public static void testStatement() throws Exception {
		DbHandler db = new DbHandler();
		String sizeStatement = Settings.getNodeText("scrape-tasks:" + "event-details-fights" + ":data-source:source-size-statement")[0];
		String sourceStatement = Settings.getNodeText("scrape-tasks:" + "event-details-fights" + ":data-source:source-statement")[0];
		db.addStatement(new Pair<String, String>("get-sources", sourceStatement));
		db.addStatement(new Pair<String, String>("sources-size", sizeStatement));	
		Object[][] dbSource = db.executePs("get-sources", "sources-size");
		System.out.println(dbSource.length);
		String[] sources = Arrays.copyOf(util.Array.getColumn(dbSource, 1), dbSource.length, String[].class);
		System.out.println(Arrays.deepToString(sources));
	}
	
	public static void testRegex() throws Exception {		
//		String text = util.IO.fileToString("E:\\projekt\\MMAFL\\code\\server\\www\\fm_c_fight.htm");
//		String delimiter = Settings.getNodeText("scrapers:fight-details:pre-process:index-delimiter")[0];
//		String tablePattern = Settings.getNodeText("scrapers:fight-details:pre-process:extract")[0];
////		System.out.println(tablePattern);
//		String tableText = Scraper.findFirst(text, tablePattern);
////		System.out.println(tableText);
//		String[] split = tableText.split(delimiter);
//		System.out.println("\t" + Arrays.deepToString(split));
		
	}
}

