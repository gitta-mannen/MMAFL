package scraper;

import java.sql.SQLException;
import java.text.ParseException;

import database.DbHandler;

public abstract class Task {
	protected String name;
	protected final DbHandler db;
	
	public Task (String name) {
		this.name = name;
		db = new DbHandler();
	}
	
	protected String getName () {
		return name;
	}
	
	protected void doTask() throws Exception {
		preTask();
		task();
		postTask();
	}
	
	protected abstract void preTask() throws SQLException, Exception;
	protected abstract void postTask() throws SQLException;
	protected abstract void task() throws Exception;
	
}
