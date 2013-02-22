package database;

public class Example {
	public static void main(String[] args) {
		// used once for all scrapers
		StatsHandler db = new StatsHandler();
		// once per record (i.e once per event)
		db.update(new Event(1, "UFC the Tuna Sandwich", "2012-02-01", "H�landa", "UFC", "50000000"));
		// once for all scrapers
		db.close();
	}
}