package database;

public class Example {
	public static void main(String[] args) {
		// used once for all scrapers
		StatsHandler db = new StatsHandler();
		// once per record (i.e once per event)
		db.update(new Event(4, "UFC the Tuna Sandwich", "2012-02-02", "vegas", "UFC", "50000000"));
		// once for all scrapers
		db.close();
	}
}
