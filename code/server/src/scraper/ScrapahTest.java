package scraper;

import java.io.IOException;
import java.net.MalformedURLException;

import settings.Settings;
import database.DbHandler;

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
//		System.out.println(Settings.getNodeMap(new String[]{"database", "table", "column"}).get("id").get("dbtype"));
//		String[] temp = Settings.getNodeText("scrapers:completed-events:scrape-field:apptype");
//		for (int i = 0; i < temp.length; i++)
//			System.out.println(temp[i]);
//		DbHandler.buildTables(true);
//		DbHandler db = new DbHandler();
//		db.executePrepared("test-insert", "events", new Object[]{new Integer(2), new String("test event 1")});
//		db.executePrepared("select-not-updated", "events", null);
//		LinkedList<String> result;
//		result = scraper.Scraper.findAll(Scraper.textFromUrl("http://hosteddb.fightmetric.com/fights/index/4218"), 
//				Settings.getSchema().getTable("fights").getColumn("finish method").regex);
//		
//		Iterator iterator = result.iterator();
//		while (iterator.hasNext()) {
//			System.out.println(iterator.next());
//		}
	}
	
	

}
