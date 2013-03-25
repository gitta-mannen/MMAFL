package settings;

import java.io.File;
import java.net.URI;

public final class Constants {
	public static final URI APP_ROOT, WEB_ROOT;
	// Can't be instantiated
	private Constants(){}
	
	static {
		APP_ROOT = new File(System.getProperty("user.dir")).toURI();
		WEB_ROOT = new File(System.getProperty("user.dir") + "\\www").toURI();
	}
	
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
