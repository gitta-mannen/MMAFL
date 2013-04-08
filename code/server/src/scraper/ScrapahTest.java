package scraper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import settings.Constants;
import settings.Settings;
import sun.misc.Cleaner;
import util.Pair;
import util.WebDiskCache;
import database.DbHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;

import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CleanerTransformations;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagTransformation;
import org.htmlcleaner.XPatherException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

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
//		testXpath();
//		testParser();
//		testTidy();
//		testCleaner();
//		testJson();
		testXscraper();
		
//		String[] regexes = Settings.getNodeText("scrapers:" + "fighters-index" + ":scrape-field:regex");
	}
	
	private static void testXscraper() throws FileNotFoundException, Exception {
//		File file = (new File("E:\\projekt\\MMAFL\\code\\server\\www\\hosteddb.fightmetric.com\\fighters\\details\\497.htm"));
		File file = (new File("E:\\projekt\\MMAFL\\code\\server\\www\\hosteddb.fightmetric.com\\events\\index\\date\\asc\\1\\all.htm"));
		HashMap<String, Object[]> hm = HtmlScraper.scrape(util.WebDiskCache.fileToString(file), new File("event-index.json"));
		
		if (hm != null)
		for (Entry<String, Object[]> entry : hm.entrySet()) {
			System.out.print(entry.getKey() + " : ");
			System.out.println(Arrays.deepToString(entry.getValue()));
		}
	}

	private static void testJson() {
		User user = new User();
		ObjectMapper mapper = new ObjectMapper();
	 
		try {
	 
			// convert user object to json string, and save to a file
//			HashMap<String, ScrapeField> hm = new HashMap<String, ScrapeField>();
//			
//			hm.put("test a", new ScrapeField("a", "\\d", "//*", "String"));
//			hm.put("test b", new ScrapeField("b", "\\d", "//*", "String"));
//			hm.put("test c", new ScrapeField("c", "\\d", "//*", "String"));
//			
//			mapper.writeValue(new File("test.json"), new ScraperSetting("foo", hm));
			ScraperSetting user2 = mapper.readValue(new File("test.json"), ScraperSetting.class);
			
			// display to console
			System.out.println(mapper.writeValueAsString(user2));
//			System.out.println(user2);
	 
		} catch (JsonGenerationException e) {	 
			e.printStackTrace();
	 
		} catch (JsonMappingException e) {	 
			e.printStackTrace();
	 
		} catch (IOException e) {	 
			e.printStackTrace();
	 
		}
		
	}

	private static void testCleaner() throws IOException, XPatherException {
		CleanerProperties props = new CleanerProperties();
		 
		// set some properties to non-default values
		props.setTranslateSpecialEntities(true);
		props.setTransResCharsToNCR(true);
		props.setOmitComments(true);
		props.setOmitDoctypeDeclaration(true);

		
		HtmlCleaner cleaner = new HtmlCleaner(props);
		
		TagNode tagNode = cleaner.clean(new File("E:\\projekt\\MMAFL\\code\\server\\www\\hosteddb.fightmetric.com\\fighters\\details\\1.htm")
			);
		

		Object[] myNodes = tagNode.evaluateXPath("//table[@class='record_info']/tbody/tr[2]/td[text()>0]/text()");
		System.out.println(Arrays.deepToString(myNodes));
		
		//writes the xml to file
		new PrettyXmlSerializer(props).writeToFile(
				tagNode, "test.xml", "utf-8"
			);
		
	}

	private static void testTidy() throws Exception {
		Tidy tidy = new Tidy();
		tidy.setMakeClean(true);
		tidy.setXmlOut( true);
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);
		tidy.setForceOutput(true);
		tidy.setPrintBodyOnly(false);
		
		FileOutputStream fileOutputStream = new FileOutputStream("outXHTML.xml");   
		File file = (new File("E:\\projekt\\MMAFL\\code\\server\\www\\hosteddb.fightmetric.com\\fighters\\details\\1.htm"));
		Document doc = tidy.parseDOM( new FileInputStream(file), fileOutputStream);
		
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

	}
}

class User {
	 
	private int age = 29;
	private String name = "mkyong";
	private List<String> messages = new ArrayList<String>() {
		{
			add("msg 1");
			add("msg 2");
			add("msg 3");
		}
	};
 
	//getter and setter methods
 
	@Override
	public String toString() {
		return "User [age=" + age + ", name=" + name + ", " +
				"messages=" + messages + "]";
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
}

