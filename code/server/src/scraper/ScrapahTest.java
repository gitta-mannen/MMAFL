package scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import settings.Settings;

public class ScrapahTest {

	public ScrapahTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException, IOException {	
		new Thread(new FMScraper()).run();
		
		String delimRegex = Settings.getNodeText("scrapers:upcoming-events:index-delimiter")[0];
		String tableRegex = Settings.getNodeText("scrapers:upcoming-events:table-regex")[0];
		String[] regexes = Settings.getNodeText("scrapers:upcoming-events:scrape-field:regex");
		String page = util.IO.fileToString("E:\\projekt\\MMAFL\\code\\server\\www\\fm_u_events.htm");
		String table= Scraper.findFirst(page, tableRegex);
//
//		String[] splitTable = table.split(delimRegex);
//		for (int i = 0; i < splitTable.length; i++) {
//			System.out.println("<" + i + "> ---------------------------------------------------->");
//			System.out.println(splitTable[i]);
//		}
//		
////		for (int i = 0; i < regexes.length; i++) {
////			System.out.println(regexes[i]);
////		}
//		
//		String[] results = Scraper.scrapeFirst(splitTable[1], regexes);
//		for (int i = 0; i < results.length; i++) {
//			System.out.println(results[i]);
//		}

	}
	
	

}
