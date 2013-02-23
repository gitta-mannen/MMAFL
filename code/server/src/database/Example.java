package database;

import java.util.ArrayList;

public class Example {
	public static void main(String[] args) {
		StatsHandler db = new StatsHandler();
		
		//uppdatera databasen
		db.update(new Event(7, "UFC the Tuna Sandwich", "2012-02-02", "tgregertg", "UFC", "50000000"));
		
		//hämta innehållet i databasen till en arraylist av events
		ArrayList<Event> events = db.get(new Event());		
		
		for (Event event : events) {
			System.out.println(event.toSqlString());
		}
		
		db.close();	
	}
}
