package scraper;

public class ScrapeException extends Exception {
	private static final long serialVersionUID = 1L;
	ScrapeField sf;
	
	public ScrapeException(String message) {
		super(message);
	}
	
	public ScrapeException(String message, Throwable cause, ScrapeField sf) {
		super(message, cause);
		this.sf = sf;
	}

	public ScrapeException(String message, ScrapeField sf) {
		super(message);
		this.sf = sf;
	}

	public ScrapeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ScrapeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ScrapeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
