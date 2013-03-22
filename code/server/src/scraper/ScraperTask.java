package scraper;

import java.sql.SQLException;
import java.util.Arrays;

import settings.Constants.SourceType;
import settings.Settings;
import util.Pair;

public class ScraperTask extends Task {		
	private final SourceType sourceType;
	private final String source;
	
	private final Scraper[] scrapers;
	private final boolean hasForeignKey;
	private String[] sources;
	private String[] fKeys;	
	
	public ScraperTask(String name, Scraper[] scrapers) throws SQLException {
		super(name);
		sourceType = Enum.valueOf(SourceType.class, Settings.getNodeText("scrape-tasks:" + name + ":data-source:source-type")[0].toUpperCase());
		source = Settings.getNodeText("scrape-tasks:" + name + ":data-source:source-" + sourceType.toString())[0];	
		hasForeignKey = Boolean.parseBoolean(Settings.getNodeText("scrape-tasks:" + name + ":data-source:has-f-key")[0]);
		this.scrapers = scrapers;
	}

	@Override
	protected void preTask() throws Exception {
		for (int i = 0; i < scrapers.length; i++) {
			String updateStatement = Settings.getNodeText("scrape-tasks:" + name + ":update-statements:" + scrapers[i].getName())[0];
			db.addStatement(new Pair<String, String>(scrapers[i].getName(), updateStatement));
		}
		
		switch (sourceType) {
		case URL:
			sources = source.split(",");
			break;
		case FILE:
			sources = source.split(",");
			break;
		case DB:
			//haxxa *****
			break;
		default:
			throw new Exception("source type not recognized");
		}
		
		db.setAutoCommit(false);
	}

	private void prepareStatements() throws SQLException {		
	}

	@Override
	protected void postTask() throws SQLException {
		db.setAutoCommit(true);
		db.close();
		
	}

	@Override
	protected void task() throws Exception {
		Object[][] tempResults;
		for (int i = 0; i < sources.length; i++) {
			for (int j = 0; j < scrapers.length; j++) {
				tempResults = scrapers[j].scrape(getData(sources[i]));
				
				for (int k = 0; k < tempResults.length; k++) {
////				//insert foreign key into parameter list
////				Object[] tempParam = new Object[converted.length + 1];	
////				tempParam[0] = keyUrl[i][0];	//first pos
////				System.arraycopy(converted, 0, tempParam, 1, converted.length);
////				db.executePs("insert", tempParam);			
					System.out.println("\t" + Arrays.deepToString(tempResults[k]));		
					db.executePs(scrapers[j].getName(), tempResults[k]);
				}
			}
		}
		
	}

	private String getData(String source) throws Exception {		
		switch (sourceType) {
		case URL:
			return util.Text.textFromUrl(source);
		case FILE:
			return util.IO.fileToString(source);
		case DB:
			//haxxa *****
			return null;
		default:
			throw new Exception("source type not recognized");
		}
	}
	
}	
	
		

	

