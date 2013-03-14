package scraper;

import java.util.List;

public class ScraperTask {
	private String decriptor;
	private List<String> iterator;
	private String baseUrl;	
	
	public ScraperTask() {
		// TODO Auto-generated constructor stub
	}

	public ScraperTask(String decriptor, List<String> iterator, String baseUrl) {
		super();
		this.decriptor = decriptor;
		this.iterator = iterator;
		this.baseUrl = baseUrl;
	}

	public String getDecriptor() {
		return decriptor;
	}

	public void setDecriptor(String decriptor) {
		this.decriptor = decriptor;
	}

	public List<String> getIterator() {
		return iterator;
	}

	public void setIterator(List<String> iterator) {
		this.iterator = iterator;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

}
