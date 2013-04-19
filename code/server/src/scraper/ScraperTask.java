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
	private WebDiskCache wdc;
	private DbHandler db;
	private ScraperSetting ss;
	private TaskSetting ts;
	private ScraperTask chainedTask; 
	private DocumentScraper ds;
//	public List<String> fKeyLables;
	String stName;
//	List <ScraperTask> chainedTasks = new LinkedList<ScraperTask>();
	//String sql, List<String> fKeyLables
	
	public ScraperTask(ScraperSetting ss, TaskSetting ts, WebDiskCache wdc, DbHandler db) throws ScrapeException, SQLException {
		this.wdc = wdc;
		this.db = db;
		this.ss = ss;
		this.ts = ts;			
		this.ds = new DocumentScraper(ss);
		this.stName = compileSql(ts.sql);
	}
	
	public void doTask(String path, Map<String, Object> fKeys) throws SQLException, ScrapeException, URISyntaxException, IOException, ParserConfigurationException {		
		Document doc = resolvePage(path);
		doTask(doc, fKeys);
	}
	
	private String compileSql(String sql) throws SQLException {
		LinkedList<String> cols = new LinkedList<String>(ss.fields.keySet());
		cols.addAll(ts.fKeys);
		String colsList = cols.toString().replaceAll("[\\[\\]]", "");
		String paramsList =  new String(new char[cols.size()]).replace("\0", "?").replaceAll("\\?(?!$)", "?, ");
		return db.addStatement(String.format(ts.sql, colsList, paramsList));
	}
	
	private Document resolvePage(String pathString) throws ParserConfigurationException, URISyntaxException, IOException {		
		URI path = new URI ("http", "", pathString, null);
		return resolvePage(path);
	}
	private Document resolvePage(URI path) throws ParserConfigurationException, URISyntaxException, IOException {		
		String page = wdc.getPage(path);
		return util.XML.getCleanDOM(page);
	}
		
	public void doTask(Document doc, Map<String, Object> fKeys) throws SQLException, ScrapeException, ParserConfigurationException, URISyntaxException, IOException {
		LinkedHashMap<String, Object[]> hm = ds.scrape(doc);
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
			
			if (chainedTask != null) {
				String docPath = hm.get("scrape_branch")[rowIndex].toString();
				Map<String, Object> chainedFkeys = getColsInRow(hm, chainedTask.getFKeyLables(), rowIndex);
				if (ts.passResolvedDoc) {
					chainedTask.doTask(resolvePage(docPath), chainedFkeys );
				} else {				
					chainedTask.doTask(docPath, chainedFkeys );
				}
			}
			
		}
	}	
		
	public Map<String, Object> getColsInRow (Map<String, Object[]> source, Set<String> cols, int rowIndex) {
		Map<String, Object> target = new HashMap<String, Object>();
		for (String key : cols) {
			target.put(key, source.get(key)[rowIndex]);
		}
		return target;
	}

	public void chainTask(ScraperTask task) {
		chainedTask = task;
	}

	public Set<String> getFKeyLables() {
		return ts.fKeys;
	}
}
