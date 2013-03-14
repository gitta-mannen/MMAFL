package database;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Sample {
	public static void main(String[] args) throws ClassNotFoundException, ParseException {
		DbHandler db = new DbHandler();
		db.resetTables(true);
		
		for (int i = 1; i <= 1; i++) {
			HashMap<String, Object> ht  = new HashMap<String, Object>();
			ht.put("id", i);
			ht.put("name", "Sean O'Haire");
			ht.put("date", new SimpleDateFormat("yyyy-mm-dd").parse("2013-04-05") );
			ht.put("location", "stockholm");
			ht.put("organization", "x force mega");
			ht.put("attendance", 999.000);
			
			db.update("events", ht);
		}
	}
}