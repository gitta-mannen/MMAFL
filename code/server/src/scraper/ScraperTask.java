package scraper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import database.DbHandler;

import util.Pair;
import util.WebDiskCache;

public class ScraperTask {
	public static final int CHAIN_SAME = 1;
	public static final int CHAIN_LINK = 2;
	public static final int CHAIN_RESOLVED = 4;
	private WebDiskCache wdc;
	private DbHandler db;
	private TaskSetting ts;
	private Map<ScraperTask, Integer> chainedTasks = new HashMap<ScraperTask, Integer>(1); 
	private DocumentScraper ds;
//	public List<String> fKeyLables;
	String stName;
//	List <ScraperTask> chainedTasks = new LinkedList<ScraperTask>();
	//String sql, List<String> fKeyLables
	
	public ScraperTask(TaskSetting ts, WebDiskCache wdc, DbHandler db) throws ScrapeException, SQLException {
		this.wdc = wdc;
		this.db = db;
		this.ts = ts;			
		this.ds = new DocumentScraper(ts.ss);
		this.stName = compileSql(ts.sql);
	}
	
	public void doTask(String path, Map<String, Object> fKeys) throws TaskException {		
		Document doc = resolvePage(path);
		doTask(doc, fKeys);
	}
	
	private String compileSql(String sql) throws SQLException {
		LinkedList<String> cols = new LinkedList<String>(ts.ss.fields.keySet());
		cols.addAll(ts.fKeyMap.values());
		String colsList = cols.toString().replaceAll("[\\[\\]]", "");
		String paramsList =  new String(new char[cols.size()]).replace("\0", "?").replaceAll("\\?(?!$)", "?, ");
		return db.addStatement(String.format(ts.sql, colsList, paramsList));
	}
	
	private Document resolvePage(String pathString) throws TaskException {
		
		URI path;
		try {
			path = new URI ("http", "", pathString, null);
		} catch (URISyntaxException e) {
			throw new TaskException(e);
		}
		return resolvePage(path);
	}
	private Document resolvePage(URI path) throws TaskException {		
		String page;
		try {
			page = wdc.getPage(path);
			return util.XML.getCleanDOM(page);
		} catch (URISyntaxException | IOException | ParserConfigurationException e) {
			throw new TaskException(e);
		}
		
	}
		
	public void doTask(Document doc, Map<String, Object> fKeys) throws TaskException {
		LinkedHashMap<String, Object[]> hm;
		try {
			hm = ds.scrape(doc);
		} catch (ScrapeException e) {
			throw new TaskException(e);
		}
		
		Object[] temp = hm.values().iterator().next();		
		if (temp == null) {
			return;
		}
		
		int hmLength = temp.length;
		for (Map.Entry<String, Object> entry : fKeys.entrySet()) {
		Object[] values = new Object[hmLength];
			Arrays.fill(values, entry.getValue());
			hm.put(entry.getKey(), values);
		}
		
		Object[][] results = hm.values().toArray(new Object[hm.values().size()][]);
		results = util.Array.transpose(results, hmLength);
		
		for (int rowIndex = 0; rowIndex < hmLength; rowIndex++) {			
			System.out.println(Arrays.deepToString(results[rowIndex]));
			db.executePs(stName, results[rowIndex]);
			 
			if (chainedTasks != null) {
				for (Map.Entry<ScraperTask, Integer> task : chainedTasks.entrySet()) {
					doChained(hm, task.getKey(), rowIndex, task.getValue(), doc);
				}
			}			
		}
	}	
	
	private void doChained(Map<String, Object[]> source, ScraperTask task, int rowIndex, int chainType, Document doc) throws TaskException {
		Map<String, Object> chainedFkeys = getFkeyValues(source, task.getFKeyMap(), rowIndex);				
		if (chainType == CHAIN_RESOLVED) {
			String docPath = source.get("scrape_branch")[rowIndex].toString();
			task.doTask(resolvePage(docPath), chainedFkeys );
		} else if (chainType == CHAIN_LINK) {				
			String docPath = source.get("scrape_branch")[rowIndex].toString();
			task.doTask(docPath, chainedFkeys );
		} else if (chainType == CHAIN_SAME) {
			task.doTask(doc, chainedFkeys );
		}
	}
	
	public Map<String, Object> getFkeyValues (Map<String, Object[]> source, Map<String, String> fKeyMap, int rowIndex) {
		Map<String, Object> keys = new HashMap<String, Object>();
		for (Map.Entry<String, String> keyMap : fKeyMap.entrySet()) {
			keys.put(keyMap.getValue(), source.get(keyMap.getKey())[rowIndex]);
		}
		return keys;
	}

	public void chainTask(ScraperTask task, int chainType) {
		chainedTasks.put(task, chainType);
	}

	public Map<String, String> getFKeyMap() {
		return ts.fKeyMap;
	}
}
