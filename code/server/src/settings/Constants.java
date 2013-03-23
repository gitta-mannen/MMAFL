package settings;

public abstract class Constants {

	public enum AppType {
	    STRING, DOUBLE, INTEGER, LONG, DATE, OBJECT
	}
	
	public enum DbType {
		TEXT("text"), REAL("real"), INTEGER("integer"), BLOB("blob");
		
	   private final String stringValue;
	   private DbType(final String s) { stringValue = s; }
	   public String toString() { return stringValue; }
	}
	
	public enum SourceType {
	    CONST_URL("const_url"), DB_URL("db_url"), DB_DEBUG("db_debug"), FILE("file");
	    
	   private final String stringValue;
	   private SourceType(final String s) { stringValue = s; }
	   public String toString() { return stringValue; }
	}
}
