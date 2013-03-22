package scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Arrays;

import settings.Settings;

public class ScrapahTest {

	public ScrapahTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws MalformedURLException, IOException, ParseException {	
//		new Thread(new ScrapeChain()).run();

		String text = util.IO.fileToString("E:\\projekt\\MMAFL\\code\\server\\www\\fm_u_events.htm");
		Object[][] results = new FMScraper("upcoming-events").scrape(text);
		for (int i = 0; i < results.length; i++) {
			System.out.println("<" + i + ">");								
				System.out.println("\t" + Arrays.deepToString(results[i]));							
		}
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
