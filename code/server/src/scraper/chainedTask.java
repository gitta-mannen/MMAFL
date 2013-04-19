package scraper;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import database.DbHandler;

public abstract class chainedTask {
	protected DbHandler db;
	protected chainedTask sibling;
	
	public chainedTask() {
		// TODO Auto-generated constructor stub
	}

	public abstract boolean doTask();

	public abstract boolean doTask(URI[] uri);

	public boolean doTask(Document doc, Map<String, Object> fKeys) {
		// TODO Auto-generated method stub
		return false;
	}
}
