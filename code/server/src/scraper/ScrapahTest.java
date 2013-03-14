package scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.LinkedList;

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
		Settings.getInstance();
		LinkedList<String> result;
		result = util.Text.findAll(Scraper.textFromUrl("http://hosteddb.fightmetric.com/fights/index/4218"), 
				Settings.getSchema().getTable("fights").getColumn("finish method").regex);
		
		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}

}
