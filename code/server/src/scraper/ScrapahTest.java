package scraper;

import java.util.Arrays;

import settings.Settings;
import util.Pair;

import database.DbHandler;

public class ScrapahTest {

	public ScrapahTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//		DbHandler db = new DbHandler();//.buildTables(true);
		new Thread(new TaskChain()).run();
//		
//		String sizeStatement = Settings.getNodeText("scrape-tasks:" + "event-details-fights" + ":data-source:source-size-statement")[0];
//		String sourceStatement = Settings.getNodeText("scrape-tasks:" + "event-details-fights" + ":data-source:source-statement")[0];
//		db.addStatement(new Pair<String, String>("get-sources", sourceStatement));
//		db.addStatement(new Pair<String, String>("sources-size", sizeStatement));	
//		Object[][] dbSource = db.executePs("get-sources", "sources-size");
//		System.out.println(dbSource.length);
//		String[] sources = Arrays.copyOf(util.Array.GetColumnArray(dbSource, 1), dbSource.length, String[].class);
//		System.out.println(Arrays.deepToString(sources));
		
//		String text = util.IO.fileToString("E:\\projekt\\MMAFL\\code\\server\\www\\fm_u_events.htm");
//		Object[][] results = new FMScraper("upcoming-events").scrape(text);
//		for (int i = 0; i < results.length; i++) {
//			System.out.println("<" + i + ">");								
//				System.out.println("\t" + Arrays.deepToString(results[i]));							
//		}
	}
}

//// table index scraper test
//String delimiter = Settings.getNodeText("scrapers:upcoming-events:index-delimiter")[0];
//String tablePattern = Settings.getNodeText("scrapers:upcoming-events:table-patter")[0];
//System.out.println(tablePattern);
//String tableText = Scraper.findFirst(text, tablePattern);
////System.out.println(tableText);
//String[] split = tableText.split(delimiter);


//FMScraper scraper = new FMScraper("upcoming-events");
//String[] processed = new FMScraper("upcoming-events").scrape(text);

//for (String s: processed) {
//	System.out.println("\t\t\t-------------------");
//	System.out.println(s);
//}
