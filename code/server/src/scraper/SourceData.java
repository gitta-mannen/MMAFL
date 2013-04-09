package scraper;

public class SourceData {
	public final boolean staticSource;
	public final String constSource;
	public final String statement;
	public final String sizeStatement;
	public final String host;
	
	public SourceData(boolean staticSource, String constSource,
			String statement, String sizeStatement, String host) {
		super();
		this.staticSource = staticSource;
		this.constSource = constSource;
		this.statement = statement;
		this.sizeStatement = sizeStatement;
		this.host = host;
	}	

}
