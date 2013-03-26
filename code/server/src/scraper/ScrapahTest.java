package scraper;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import settings.Constants;
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
//		new Thread(new TaskChain()).run();
		testScraper();
//		testRegex();	
//		testFlags();	
//		testTask();	
//		tesFiles();	
//		testURI();
	}
	
	public static void testURI() throws Exception {
		System.out.println(new File (new URI("file:///E:/projekt/MMAFL/code/server/www/fightmetric/")).isDirectory());
		System.out.println(new File (new URI("file","","/E:/projekt/MMAFL/code/server/www/fightmetric/", null)).getAbsolutePath());
		System.out.println(new URI("http", "", "/events/index/date/asc/1/all", null).getPath());
		File file = new File(new URI("http", "hosteddb.fightmetric.com", "", null).getHost());
		System.out.println(file.getAbsolutePath());
//		URI uri = file.toURI();
//		System.out.println("scheme: " + uri.getScheme());
//		System.out.println("auth: " + uri.getAuthority());
//		System.out.println("fragment: " + uri.getFragment());
//		System.out.println("host: " + uri.getHost());
//		System.out.println("path: " + uri.getPath());
//		System.out.println("userinfo: " + uri.getUserInfo());
		
		URI host = new URI("http", "hosteddb.fightmetric.com", "", null);
		URI path = new URI("/events/index/date/asc/1/all");
		URI resolved = new URI("http", host.getHost(), path.getPath(), null);
		System.out.println(host.resolve(path.getPath()).resolve(".htm"));
	}
		
	
	public static void tesFiles() throws Exception {
		WebDiskCache wb = new WebDiskCache(new URI("http", "docs.oracle.com", "", null), Constants.WEB_ROOT);
		String page = wb.getPage(new URI("http", "", "/javase/6/docs/api/java/net/URL.html", null));
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
		String text = WebDiskCache.fileToString(new File("E:\\projekt\\MMAFL\\code\\server\\www\\hosteddb.fightmetric.com\\fights\\index\\49.htm"));
		Object[][] results = new FMScraper("rounds-looser").scrape(text);
		System.out.println("\t" + Arrays.deepToString(results));							
		//(?:ROUND .*?<tr>.*?</tr>)(.*?)(<tr>.*?</tr>)
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
		String text = WebDiskCache.fileToString(new File("E:\\projekt\\MMAFL\\code\\server\\www\\test.htm"));
//		String delimiter = Settings.getNodeText("scrapers:fight-details:pre-process:index-delimiter")[0];
//		String tablePattern = Settings.getNodeText("scrapers:fight-details:pre-process:extract")[0];
//		System.out.println(tablePattern);
//		String tableText = Scraper.findFirst(text, tablePattern);
//		System.out.println(tableText);
//		String[] split = tableText.split(delimiter);
//		System.out.println("\t" + Arrays.deepToString(split));
		String regex = "<br\\s/>|<(\\w*)[^>]*>(.*?)</\\1>";
		String regex2 = "^\\s+[:]|\\s";
		text = text.replaceAll(regex, "");
		System.out.println(text);//.replaceAll("^\\s+?[:]\\s+?|\\s+?$", replacement));
		text = text.replaceAll(regex2, "");
		System.out.println(text);
	}
}

