package scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import database.DbHandler;
import settings.Constants.AppType;
import settings.Settings;
import util.Pair;

public abstract class FMScraperTask {
	protected AppType[] types;
	protected String[] regexes;
	protected DbHandler db;
	protected Pair<String, String>[] pStatements;
	protected String taskName;
	
	public FMScraperTask(String taskName) {
		this.taskName = taskName;
		initFields();
		db = new DbHandler(pStatements);		
	}
	
	private void initFields () {
		regexes = Settings.getNodeText("scrapers:" + taskName + ":scrape-field:regex");
		String[] temp = Settings.getNodeText("scrapers:" + taskName + ":scrape-field:apptype");
		types = new AppType[temp.length];
		
		for (int i = 0; i < temp.length; i++) {
			types[i] = Enum.valueOf(AppType.class, temp[i].toUpperCase());
		}
		
		if (regexes.length != types.length) {
			throw new IndexOutOfBoundsException("Each field has to have a type.");
		}	
		
		String[] psNames = Settings.getNodeText("scrapers:" + taskName + ":prepared-statement:name");
		String[] pS = Settings.getNodeText("scrapers:" + taskName + ":prepared-statement:statement");
		pStatements = Pair.merge(psNames, pS);			
	}

	public void doTask () throws MalformedURLException, IOException, Exception {		
		System.out.println("abstract do shouldn't be called");
	}
}
