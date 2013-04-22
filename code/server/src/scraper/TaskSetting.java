package scraper;

import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonProperty;

public class TaskSetting {
	public final String name;	
	public final String sql;
	public final Map<String, String> fKeyMap;
	public final ScraperSetting ss;
	
	
	public TaskSetting(	@JsonProperty("name") String name, @JsonProperty("ss")ScraperSetting ss, 
						@JsonProperty("sql") String sql, @JsonProperty("fKeyMap") Map<String, String> fKeyMap) {
		super();
		this.name = name;
		this.sql = sql;
		this.fKeyMap = fKeyMap;
		this.ss = ss;		
	}
}
