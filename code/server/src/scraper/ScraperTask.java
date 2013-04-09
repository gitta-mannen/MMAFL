package scraper;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import util.WebDiskCache;

import database.DbHandler;

public class ScraperTask {
	DbHandler db;	
	SourceData sd;
	WebDiskCache wdc;
	Set<ScrapeDataAdapter> sda;

	public ScraperTask(File sourceData, Set<File> scrapeAdapters) {				
		try {
			//inflate settings
			ObjectMapper mapper = new ObjectMapper();
			for (File file : scrapeAdapters) {
				sda.add(mapper.readValue(file, ScrapeDataAdapter.class));
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean doTask() {

		return true;
	}

}
