package scraper;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Arrays;
import settings.Constants;
import settings.Settings;
import util.Pair;
import util.WebDiskCache;
import database.DbHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;

import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

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
//		testScraper();
//		testRegex();	
//		testFlags();	
//		testTask();	
//		tesFiles();	
//		testURI();
		testXpath();
//		testParser();
		
//		String[] regexes = Settings.getNodeText("scrapers:" + "fighters-index" + ":scrape-field:regex");
	}
	
	public static void testXpath() throws Exception {
		File file = (new File("E:\\projekt\\MMAFL\\code\\server\\www\\hosteddb.fightmetric.com\\fighters\\details\\1.htm"));
		Document doc  = util.XML.parseXML(file);
		String[] result = util.XML.getPathText("//table[@class='record_info']/tr[1]/td/text()", doc);
		System.out.println(Arrays.deepToString(result));
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
		new ScraperTask("event-details-fights", new Scraper[]{new FMScraper("event-details"), new FMScraper("fights")}).doTask();
//		new ScraperTask("fighters-index", new Scraper[]{new FMScraper("fighters-index")}).doTask();
//		new DataTask("compare-fighters").doTask();
//		new ScraperTask("fighter-details", new Scraper[]{new FMScraper("fighter-details")}).doTask();
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
		String text = WebDiskCache.fileToString(new File("E:\\projekt\\MMAFL\\code\\server\\www\\hosteddb.fightmetric.com\\fighters\\details\\2052.htm"));
		Object[][] results = new FMScraper("fighter-details").scrape(text);
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
		String text = WebDiskCache.fileToString(new File("E:\\projekt\\MMAFL\\code\\server\\www\\hosteddb.fightmetric.com\\fighters\\details\\1.htm"));
//		String delimiter = Settings.getNodeText("scrapers:fight-details:pre-process:index-delimiter")[0];
//		String tablePattern = Settings.getNodeText("scrapers:fight-details:pre-process:extract")[0];
//		System.out.println(tablePattern);
//		String tableText = Scraper.findFirst(text, tablePattern);
//		System.out.println(tableText);
//		String[] split = tableText.split(delimiter);
//		System.out.println("\t" + Arrays.deepToString(split));
		
//		String regex = "<br\\s/>|<(\\w*)[^>]*>(.*?)</\\1>";
//		String regex2 = "^\\s+[:]|\\s";	
//		text = text.replaceAll(regex, "");
//		System.out.println(text);
//		text = text.replaceAll(regex2, "");
//		System.out.println(text);
		
		String regex = Settings.getNodeText("scrapers:fighter-details:pre-process:extract")[0];
//		Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL).matcher(text);	
//		matcher.find();
//		System.out.println(matcher.group("data"));
		
		String result2 = Scraper.findNamedGroups(text, regex);
		System.out.println(result2);
	}
	
	public static void testParser() throws FileNotFoundException {
		String text = WebDiskCache.fileToString(new File("E:\\projekt\\MMAFL\\code\\server\\www\\hosteddb.fightmetric.com\\fighters\\details\\1.htm"));
		HtmlParser.parse(text);
	}
}

