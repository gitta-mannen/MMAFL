package scraper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import settings.Constants.SourceType;
import settings.Settings;
import util.IO;
import util.Logger;
import util.Pair;
import util.WebDiskCache;

public class ScraperTask extends Task {		
	private final boolean constSource;	
	private final Scraper[] scrapers;		
	private final WebDiskCache wdc;	
	private final String constPath;
	private final int[] scraperFlags;
	
	private String[] sourcePaths;
	private Object[][] dbSource;
	
	public ScraperTask(String name, Scraper[] scrapers) throws Exception {
		super(name);
		
		this.scrapers = scrapers;										
		String webRoot = System.getProperty("user.dir") + "\\www\\fightmetric";
		String urlDomain = Settings.getNodeText("scrape-tasks:" + name + ":data-source:url-domain")[0];
		constPath = Settings.getNodeText("scrape-tasks:" + name + ":data-source:const-path")[0];
		constSource = Boolean.parseBoolean(Settings.getNodeText("scrape-tasks:" + name + ":data-source:const-source")[0]);
		wdc = new WebDiskCache(urlDomain, webRoot, WebDiskCache.CACHE_MODE_DEBUG);
		
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
					System.out.println("flag for row " + j + " set for scraper "+ scraperName);
				}
			}
		}				
								
		if (!constSource) {			
			String sizeStatement = Settings.getNodeText("scrape-tasks:" + name + ":data-source:source-size-statement")[0];
			String sourceStatement = Settings.getNodeText("scrape-tasks:" + name + ":data-source:source-statement")[0];
			db.addStatement(new Pair<String, String>("get-sources", sourceStatement));
			db.addStatement(new Pair<String, String>("sources-size", sizeStatement));			
		}		
	}

	@Override
	protected void task() throws Exception {
		setSourcPaths();
		
		Object[][] tempResults;
		for (int i = 0; i < sourcePaths.length; i++) {
			String data = wdc.getPage(sourcePaths[i]);
			for (int j = 0; j < scrapers.length; j++) {
				tempResults = scrapers[j].scrape(data);				
				for (int k = 0; k < tempResults.length; k++) {
					//insert foreign key into parameter list
					//checks scraper flags for foreign db columns to insert
					//valid columns are 1 through 31, 0 is always the link source
					//f-key is always inserted in the back
					System.out.println("\t" + Arrays.deepToString(dbSource));
					System.out.println("scraper name " + scrapers[j].getName());
					System.out.println("scraperFlags length " + scraperFlags.length);					
					System.out.println("scraper flag int value " + scraperFlags[j]);
					Object[] results = setFkeys(tempResults[k], dbSource[i], scraperFlags[j]);



					
					//System.out.println("\t" + Arrays.deepToString(results));
//					System.out.println("results inserted: " + results.length + " for scraper " + scrapers[j].getName());
					db.executePs(scrapers[j].getName(), results);
				}
			}
		}		
	}
	
	private Object[] setFkeys(Object[] results, Object[] keySource, int flags) {
		System.out.println("\tresults before f-key: " + Arrays.deepToString(results));
		for (int n = 1; n < 31; n++) {
			//compare to bitmask for column n
			if ((flags >> n & 1) == 1 ) {
				results = util.Array.insert(results, keySource[n], 1);
			}
		}
		System.out.println("\tresults after f-key insertion: " + Arrays.deepToString(results));
		return results;
	}

	private void setSourcPaths() {
		if (constSource) {
			sourcePaths = new String[]{constPath};
		} else {
			dbSource = db.executePs("get-sources", "sources-size");
			sourcePaths = Arrays.copyOf(util.Array.getColumn(dbSource, 0), dbSource.length, String[].class);		
		}
		for (String s : sourcePaths) {
			System.out.println("task source paths loaded: " + s);
		}		
	}
	
}	
	
		

	


