package scraper;

import java.sql.SQLException;
import java.util.HashMap;

import util.WebDiskCache;
import database.DbHandler;

public class RootTask {	
	private ScraperTask st;
	WebDiskCache wdc;
	DbHandler db;	
	
	public RootTask(ScraperTask st, WebDiskCache wdc, DbHandler db) {
		super();
		this.st = st;
		this.wdc = wdc;
		this.db = db;
	}

	public ScraperTask chainTask(ScraperTask st) {
		this.st = st;
		return st;
	}
	
	public void doTask(String table, String sourceField, String fKeyField, String condition) throws SQLException, TaskException {
		String query = String.format("SELECT %s, %s FROM %s WHERE %s", sourceField, fKeyField, table, condition);
		String stName = db.addStatement(query);
		Object[][] results = db.executePquery(stName);
		db.setAutoCommit(false);
		HashMap<String, Object> fk = new HashMap<String, Object>();
		
		for (Object[] result : results) {
			fk.put(fKeyField, result[1]);
			st.doTask(result[0].toString(), fk);
			db.commit();
		}
	}
}
