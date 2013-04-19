package scraper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.w3c.dom.Document;

import database.DbHandler;

import settings.Constants;
import settings.Settings;
import util.WebDiskCache;



public class TaskChain {
	URI host;
	WebDiskCache wdc;
	DbHandler db;
	ObjectMapper mapper;
	
	public TaskChain() {
		 try {
			host = new URI ("http", "hosteddb.fightmetric.com", "", null);
			 wdc = new WebDiskCache(host, Constants.WEB_ROOT, WebDiskCache.NEVER_EXPIRES);
			 mapper = new ObjectMapper();
			 db = new DbHandler();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	private ScraperTask inflateTask(String scraperSetFile, String taskSetFile) {
		ScraperSetting ss = mapper.readValue(new File(scraperSetFile), ScraperSetting.class);
		TaskSetting ts = mapper.readValue(new File(taskSetFile), TaskSetting.class);
		return new ScraperTask(ss, ts, wdc, db);	
	}
	
	private ScraperTask inflateTask(String settingsFname, String sqlXpath, String[] fKeys) throws Exception {
		try {
			ScraperSetting ss = mapper.readValue(new File(settingsFname), ScraperSetting.class);
			String sql = util.XML.getPathText(sqlXpath, Settings.getSettings())[0];
			LinkedList<String> keys = new LinkedList<String>(Arrays.asList(fKeys));
			return null;//new ScraperTask(ss, sql, keys, wdc, db);	
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		throw new Exception("Task init failed.");
	}
	
	private LinkedHashMap<String, Object> getMap(String[] keys, Object[] values) {
		if (keys.length != values.length) {
			throw new IndexOutOfBoundsException();
		}
		
		LinkedHashMap<String, Object> hm = new LinkedHashMap<String, Object>(keys.length);		
		for (int i = 0; i < keys.length; i++) {
			hm.put(keys[i], values[i]);
		}
		
		return hm;
	}
	
	public void run() {
		try {												
			ScraperTask upcomingIndex = inflateTask("events-index.json", "//database/prepared-statements/insert-events-index/text()", new String[]{"completed"});			
			ScraperTask completedIndex = inflateTask("events-index.json", "//database/prepared-statements/insert-events-index/text()", new String[]{"completed"});			
			ScraperTask completedDetails = inflateTask("c-event-details.json", "//database/prepared-statements/c-event-insert/text()", new String[]{"id"});
			ScraperTask completedFightsIndex = inflateTask("completed-fights-index.json", "//database/prepared-statements/completed-fights-index/text()", new String[]{"event_id"});
			ScraperTask fightDetails = inflateTask("fight-details.json", "//database/prepared-statements/update-fight-detials/text()", new String[]{"id"});
			completedFightsIndex.chainTask(fightDetails);
			
			db.setAutoCommit(false);
			//tmp events
			upcomingIndex.doTask("/events/index/date/asc/0/all", getMap(new String[]{"completed"}, new Object[]{0}));
			completedIndex.doTask("/events/index/date/asc/1/all", getMap(new String[]{"completed"}, new Object[]{1}));			
			// compare events
			String sqlCompare = util.XML.getPathText("//compare-events/prepared-statement/text()", Settings.getSettings())[0];
			String stName = db.addStatement(sqlCompare);
			db.executePs(stName);
			db.setAutoCommit(true);	
			
			//get event sources links
			String sqlDetails = util.XML.getPathText("//database/prepared-statements/get-completed-events/text()",Settings.getSettings())[0];
			stName = db.addStatement(sqlDetails);						
			
			//
			Object[][] events = db.executePquery(stName);
			db.setAutoCommit(false);
			HashMap<String, Object> fk = new HashMap<String, Object>();					
			for (Object[] event : events) {
				fk.put("id", event[1]);
				completedDetails.doTask(event[0].toString(), fk);
				completedFightsIndex.doTask(event[0].toString(), getMap(new String[]{"event_id"}, new Object[]{event[1]}));
			}
			db.setAutoCommit(true);
			
			
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {			
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ScrapeException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
