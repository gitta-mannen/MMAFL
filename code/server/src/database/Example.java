package database;

public class Example {
	public static void main(String[] args) {
		StatsHandler db = new StatsHandler();		
		db.update(new Event(1, "UFC the Tuna Sandwich", "2012-02-01", "Hålanda", "UFC", "50000000"));
		db.close();
	}
}
