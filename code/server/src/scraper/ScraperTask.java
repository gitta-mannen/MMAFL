package scraper;

import settings.Settings;
import util.Pair;

public class ScraperTask extends Task {		
	private final String sourceType;
	private final String source;
	
	public ScraperTask(String name, Pair<String, Scraper>[] scrapers) {
		super(name);
		sourceType = Settings.getNodeText("scrape-tasks:" + name + ":data-source:source-type")[0];
		source = Settings.getNodeText("scrape-tasks:" + name + ":data-source:source-" + sourceType)[0];
	}
}	
	
		

	

