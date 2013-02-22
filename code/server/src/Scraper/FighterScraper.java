package Scraper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
//import java.util.ArrayList;

public class FighterScraper {
	
	String fighter;

	public FighterScraper(URL url) throws MalformedURLException { {
		//fightcardArray = new ArrayList<String>();
		try {
			URLConnection yc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			String fighterName = "";
			while ((inputLine = in.readLine()) != null) {
			
				//Scrape: Fighter Names
				if (inputLine.contains("/fighters/details/")
						&& (fighterName.contains(""))) {
					inputLine = inputLine.replaceAll("\\<.*?>", "");
					inputLine = inputLine.replaceAll("\\(.*?\\)", "");
					inputLine = inputLine.replaceAll("  ", " ");
					//fightcardArray.add(inputLine);
					
				}
			

			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
}
