package scraper;

public class RootTask {	
	private ScraperTask st;
	
	public RootTask() {
		// TODO Auto-generated constructor stub
	}
	
	public ScraperTask chainTask(ScraperTask st) {
		this.st = st;
		return st;
	}
	
	public void doTask() {
		
	}
}
