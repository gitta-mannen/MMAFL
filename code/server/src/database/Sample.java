package database;
import java.util.HashMap;

public class Sample {
	public static void main(String[] args) throws ClassNotFoundException {
		StatsHandler db = new StatsHandler();
		HashMap<String, String> ht  = new HashMap<String, String>();
		ht.put("id", "99");
		ht.put("name", "'ufc event'");
		ht.put("date", "'2011-05-05'");
		ht.put("location", "'gothenburg'");
		ht.put("organization", "'ufc'");
		ht.put("attendance", "'999.000'");
		
		db.update("events", ht);
	}
}