package scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedList;

import settings.Settings;
import settings.Constants.AppType;
import util.Text;

public class ScrapahTest {

	public ScrapahTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException, IOException {
		Settings.getInstance();
		LinkedList<String> result;
		result = util.Text.findAll(Scraper.textFromUrl("http://hosteddb.fightmetric.com/fights/index/4220"), 
				Settings.getSchema().getTable("fights").getColumn("round format").regex);
		
		Iterator iterator = result.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}
//	public static void main(String[] args) throws ParseException {
//		System.out.println(util.Text.stringToObject ("1:16", AppType.TIME));
//	}

}
