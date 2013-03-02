package database;
import java.util.HashMap;

public class Sample {
	public static void main(String[] args) throws ClassNotFoundException {
		StatsHandler db = new StatsHandler();
		db.resetTables();
		
		for (int i = 1; i <= 10; i++) {
			HashMap<String, String> ht  = new HashMap<String, String>();
			ht.put("id", String.valueOf(i));
			ht.put("name", "ufc event");
			ht.put("date", "2011-05-05");
			ht.put("location", "stockholm");
			ht.put("organization", "strikeforce");
			ht.put("attendance", "999,000");
			
			db.update("events", ht);
		}
	}
}