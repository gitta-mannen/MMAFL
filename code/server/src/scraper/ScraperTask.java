package scraper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import settings.Constants;
import settings.Settings;
import util.Logger;
import util.Pair;
import util.WebDiskCache;

public class ScraperTask extends Task {		
	private final boolean isConstSource;	
	private final Scraper[] scrapers;		
	private final WebDiskCache wdc;	
	private final String constPath;
	private final int[] scraperFlags;
	
	private String[] sourcePaths;
	private Object[][] dbSource;
	
	public ScraperTask(String name, Scraper[] scrapers) throws Exception {
		super(name);
		
		this.scrapers = scrapers;												
		URI host = new URI ("http", Settings.getNodeText("scrape-tasks:" + name + ":data-source:host")[0], "", null);
		
		constPath = Settings.getNodeText("scrape-tasks:" + name + ":data-source:const-path")[0];
		isConstSource = Boolean.parseBoolean(Settings.getNodeText("scrape-tasks:" + name + ":data-source:const-source")[0]);
		
		wdc = new WebDiskCache(host, Constants.WEB_ROOT, WebDiskCache.CACHE_MODE_DEBUG);
		
		scraperFlags = new int[scrapers.length];
		compileStatements();
	}
	
	
	@Override
	protected void compileStatements() throws Exception {		
		for (int i = 0; i < scrapers.length; i++) {
			String scraperName = scrapers[i].getName();
			String updateStatement = Settings.getNodeText("scrape-tasks:" + name + ":scrapers:" + scraperName + ":statement:")[0];			
			db.addStatement(new Pair<String, String>(scraperName, updateStatement));			
			
			for (String s : Settings.getNodeText("scrape-tasks:" + name + ":scrapers:" + scraperName + ":f-key")){				
				scraperFlags[i] |= 1 << (Integer.valueOf(s));
			}
			
			for (int j = 0; j < 15; j++) {
				if ((scraperFlags[i] >> j & 1) == 1 ) {
					Logger.log("flag for row " + j + " set for scraper "+ scraperName, false);
				}
			}
		}				
								
		if (!isConstSource) {			
			String sizeStatement = Settings.getNodeText("scrape-tasks:" + name + ":data-source:source-size-statement")[0];
			String sourceStatement = Settings.getNodeText("scrape-tasks:" + name + ":data-source:source-statement")[0];
			db.addStatement(new Pair<String, String>("get-sources", sourceStatement));
			db.addStatement(new Pair<String, String>("sources-size", sizeStatement));			
		}		
	}

	@Override
	protected void task()   {		
		setSourcPaths();		
		Object[][] tempResults;
		
		for (int i = 0; i < sourcePaths.length; i++) {
			String data = "";
			try {
				data = wdc.getPage(new URI("http", "", sourcePaths[i], null));
			} catch (URISyntaxException e) {
				Logger.log(e.getMessage() + " for path '" + sourcePaths[i] + "'",true);
			} catch (Exception e) {
				Logger.log(e.getMessage(), true);
			}
			for (int j = 0; j < scrapers.length; j++) {
				tempResults = scrapers[j].scrape(data);			
				Logger.log("Scraper " + scrapers[j].getName() + " inserting " + tempResults.length + " records", false);
				for (int k = 0; k < tempResults.length; k++) {					
					if (isConstSource) {
						db.executePs(scrapers[j].getName(), tempResults[k]);
					} else {
						Object[] results = setFkeys(tempResults[k], dbSource[i], scraperFlags[j]);
						db.executePs(scrapers[j].getName(), results);
					}
				}
			}
		}		
	}
	
	private Object[] setFkeys(Object[] results, Object[] keySource, int flags) {
		for (int n = 1; n < 31; n++) {	//compare to bitmask for column n	
			if ((flags >> n & 1) == 1 ) { 
				results = util.Array.insert(results, keySource[n], 1);
			}
		}
		return results;
	}

	private void setSourcPaths() {
		if (isConstSource) {
			sourcePaths = new String[]{constPath};
		} else {
			dbSource = db.executePs("get-sources", "sources-size");
			sourcePaths = Arrays.copyOf(util.Array.getColumn(dbSource, 0), dbSource.length, String[].class);		
		}
	}	
}	
	
		

	


