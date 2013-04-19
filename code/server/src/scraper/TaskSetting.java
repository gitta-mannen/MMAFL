package scraper;

import java.util.Set;

public class TaskSetting {
	public final String name ;	
	public final String sql;
	public final Set<String> fKeys;
	public final ScraperSetting ss;
	public boolean passResolvedDoc;
	
	public TaskSetting(String name, ScraperSetting ss, String sql, 
						Set<String> fKeys, boolean passResolvedDoc) {
		super();
		this.name = name;
		this.sql = sql;
		this.fKeys = fKeys;
		this.ss = ss;
		this.passResolvedDoc = passResolvedDoc;
	}
}
