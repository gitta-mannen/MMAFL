package Scraper;

public class FighterScraper {
	
	String fighter;

	public FighterScraper() {
		//Scrape: Fighter Names
		if (inputLine.contains("/fighters/details/")
				&& (fighterName.contains(""))) {
			inputLine = inputLine.replaceAll("\\<.*?>", "");
			inputLine = inputLine.replaceAll("\\(.*?\\)", "");
			inputLine = inputLine.replaceAll("  ", " ");
			fightcardArray.add(inputLine);
		}
	}

}
