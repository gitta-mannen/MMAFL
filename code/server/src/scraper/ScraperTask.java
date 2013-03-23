package scraper;

import java.util.Arrays;

import settings.Constants.SourceType;
import settings.Settings;
import util.Pair;

public class ScraperTask extends Task {		
	private SourceType sourceType;	
	private final Scraper[] scrapers;
	private Pair<Boolean, Integer>[] fKeysSettings;
	private String[] sources;
	private Object[] fKeys;
	private String urlDomain;
	private int fKeyIndex;
	
	public ScraperTask(String name, Scraper[] scrapers) throws Exception {
		super(name);
		this.scrapers = scrapers;									
		fKeyIndex = Integer.valueOf(Settings.getNodeText("scrape-tasks:" + name + ":data-source:f-key-index")[0]);
		urlDomain = Settings.getNodeText("scrape-tasks:" + name + ":data-source:url-domain")[0];
		sourceType = Enum.valueOf(SourceType.class, Settings.getNodeText("scrape-tasks:" + name + ":data-source:source-type")[0].toUpperCase());
		compileStatements();
	}
	
	
	@Override
	protected void compileStatements() throws Exception {
		Boolean[] hasKey = new Boolean[scrapers.length];
		Integer[] keyInsert= new Integer[scrapers.length];
		for (int i = 0; i < scrapers.length; i++) {
			String scraperName = scrapers[i].getName();
			String updateStatement = Settings.getNodeText("scrape-tasks:" + name + ":scrapers:" + scraperName + ":statement:")[0];			
			db.addStatement(new Pair<String, String>(scraperName, updateStatement));						
			hasKey[i] = Boolean.parseBoolean(Settings.getNodeText("scrape-tasks:" + name + ":scrapers:" + scraperName + ":f-key:")[0]);
			keyInsert[i] = Integer.valueOf(Settings.getNodeText("scrape-tasks:" + name + ":scrapers:" + scraperName + ":f-key-insert:")[0]);
		}		
		
		fKeysSettings = Pair.merge(hasKey, keyInsert);
						
		switch (sourceType) {
		case DB_DEBUG : case DB_URL:				
			String sizeStatement = Settings.getNodeText("scrape-tasks:" + name + ":data-source:source-size-statement")[0];
			String sourceStatement = Settings.getNodeText("scrape-tasks:" + name + ":data-source:source-statement")[0];
			db.addStatement(new Pair<String, String>("get-sources", sourceStatement));
			db.addStatement(new Pair<String, String>("sources-size", sizeStatement));			
			break;
		default:
			break;
		}		
	}

	@Override
	protected void task() throws Exception {
		getSources();
		
		Object[][] tempResults;
		for (int i = 0; i < sources.length; i++) {
			String data = getData(sources[i]);
			for (int j = 0; j < scrapers.length; j++) {
				tempResults = scrapers[j].scrape(data);
				
				for (int k = 0; k < tempResults.length; k++) {
					//insert foreign key into parameter list
					Object[] results;
					if (fKeysSettings[j].getA()) {
						results = util.Array.insert(tempResults[k], fKeys[i], fKeysSettings[j].getB());
					} else {
						results = tempResults[k];
					}

					System.out.println("\t" + Arrays.deepToString(results));		
					db.executePs(scrapers[j].getName(), results);
				}
			}
		}
		
	}

	private void getSources() {
		switch (sourceType) {
		case CONST_URL:
			sources = Settings.getNodeText("scrape-tasks:" + name + ":data-source:url-constant");
				break;
		case FILE:
			sources = Settings.getNodeText("scrape-tasks:" + name + ":data-source:file-uri");
			break;
		case DB_DEBUG: case DB_URL:
			Object[][] dbSource = db.executePs("get-sources", "sources-size");
			fKeys = util.Array.getColumn(dbSource, fKeyIndex);
			
			if (sourceType == SourceType.DB_DEBUG) {
				sources = Settings.getNodeText("scrape-tasks:" + name + ":data-source:file-uri");
			} else {
				sources = Arrays.copyOf(util.Array.getColumn(dbSource, 1), dbSource.length, String[].class);
			}
			break;
		}
		
	}

	private String getData(String source) throws Exception {		
		switch (sourceType) {
		case CONST_URL :
			return util.Text.textFromUrl(source);
		case FILE:
			return util.IO.fileToString(source);
		case DB_URL:			
			return util.Text.textFromUrl(urlDomain + source);
		case DB_DEBUG: 
			return util.IO.fileToString(source); 
		default:
			throw new Exception("source type not recognized");
		}
	}
	
}	
	
		

	

