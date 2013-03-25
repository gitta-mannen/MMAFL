package settings;

public final class Constants {
	// Can't be instantiated
	private Constants(){}
	
	public enum AppType {
	    STRING, DOUBLE, INTEGER, LONG, DATE, OBJECT, TIME, HTML
	}
	
	public enum DbType {
		TEXT("text"), REAL("real"), INTEGER("integer"), BLOB("blob");
		
	   private final String stringValue;
	   private DbType(final String s) { stringValue = s; }
	   public String toString() { return stringValue; }
	}
	
	public enum SourceType {
	    DB("db"), CONST("const");
	    
	   private final String stringValue;
	   private SourceType(final String s) { stringValue = s; }
	   public String toString() { return stringValue; }
	}
}
