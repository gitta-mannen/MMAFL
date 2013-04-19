package scraper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SourceData {
	public final boolean staticSource;
	public final String constSource;
	public final String statement;
	public final String sizeStatement;
	public final String host;
	
	public boolean hasNext = false; 
	public Map<String, String>[] sources;
	private int count = 0;
	
	public SourceData(boolean staticSource, String constSource,
			String statement, String sizeStatement, String host) {
		super();
		this.staticSource = staticSource;
		this.constSource = constSource;
		this.statement = statement;
		this.sizeStatement = sizeStatement;
		this.host = host;
	}	

	public String[] nextSource() {
		return null;
	}
	
}
