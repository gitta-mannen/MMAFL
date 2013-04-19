package scraper;

public class ScrapeException extends Exception {
	private static final long serialVersionUID = 1L;
	ScrapeField sf;

	@Override
	public String getMessage() {
		if (sf != null) {
			return "Exception while parsing field " + sf.name + ":\n" +
					super.getMessage();
		} else {
			return super.getMessage();
		}
	}
	
	public ScrapeException(String message) {
		super(message);
	}

	public ScrapeException(String message, ScrapeField sf, String[] fieldTextNodes) {
		super(message);
	}

	public ScrapeException(Throwable cause, ScrapeField sf, String[] fieldTextNodes) {
		super(cause);
	}
	
	public ScrapeException(Throwable cause, ScrapeField sf) {
		super(cause);
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
	}

	public ScrapeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ScrapeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
