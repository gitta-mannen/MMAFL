package scraper;

import java.sql.SQLException;
import database.DbHandler;

public abstract class Task {
	protected final String name;
	protected final DbHandler db;
	
	public Task (String name) throws SQLException, Exception {
		this.name = name;
		db = new DbHandler();		
	}

	protected String getName () {
		return name;
	}
	
	protected void doTask() throws Exception {		
		db.setAutoCommit(false);
		task();
		db.setAutoCommit(true);
		db.close();
	}
	
	protected abstract void compileStatements() throws SQLException, Exception;	
	protected abstract void task() throws Exception;
}
