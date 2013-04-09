package scraper;

import java.util.HashMap;
import java.util.Set;

public class ScrapeDataAdapter {
	private HashMap<String, Object[]> data;
	private final Set<String> fKeys;
	private final String statement;
	private final DocumentScraper scraper;
	
	public ScrapeDataAdapter(HashMap<String, Object[]> data, Set<String> fKeys,
			String statement, DocumentScraper scraper) {
		super();
		this.data = data;
		this.fKeys = fKeys;
		this.statement = statement;
		this.scraper = scraper;
	}
}
