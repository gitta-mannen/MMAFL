package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author Stugatz
 *
 */
public class StatsHandler extends DbHandler {
	private Connection connection = null;

	public StatsHandler () {
		try {
			// load the sqlite-JDBC driver using the current class loader
			Class.forName("org.sqlite.JDBC");						
			// create a database connection
			this.connection = DriverManager.getConnection("jdbc:sqlite:stats.db");

			createTables();
			
		} catch (ClassNotFoundException e) {
			System.err.println(e);
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println(e);
			System.err.println(e.getMessage());
		}
	}
	
	
	/**
	 * Creates the stats tables if they don't exist
	 */
	private void createTables () {
		try {				
			Statement statement = connection.createStatement();						
			statement.setQueryTimeout(30);			
	
			statement.executeUpdate("create table if not exists fighters (id integer primary key, age integer, str_acc integer, str_def integer, td_acc integer, td_def integer, w string, l string, d string, nc string, name string, nickname string, height string, weight string, reach string, stance string, slpm double, sapm double, td_avg double, sub_avg double)");
			statement.executeUpdate("create table if not exists events (id integer primary key, name string, date string, location string, organization string, attendance string)");
			statement.executeUpdate("create table if not exists fights (id integer primary key)");
			statement.executeUpdate("create table if not exists rounds (id integer primary key)");
		} catch (SQLException e) {
			System.err.println(e);
			System.err.println(e.getMessage());
		}			
	}
	
	
	/**
	 * Deletes all entries from all tables, creates them if they don't exist
	 */
	public void resetTables () {
		try {				
			Statement statement = connection.createStatement();						
			statement.setQueryTimeout(30);			
	
			statement.executeUpdate("drop table if exists fighters");
			statement.executeUpdate("drop table if exists events");
			statement.executeUpdate("drop table if exists fights");
			statement.executeUpdate("drop table if exists rounds");
						
		} catch (SQLException e) {
			System.err.println(e);
			System.err.println(e.getMessage());
		}	
		
		createTables();
	}
	
	public void update (Fighter fighter) {
		try {
		// create statement and set timeout to 30 sec.
		Statement statement = connection.createStatement();						
		statement.setQueryTimeout(30);				
		statement.executeUpdate("insert or replace into events values" + fighter.toSqlString());
		
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e);
			System.err.println(e.getMessage());
		}			
	}
	
	public void update (Fight fight) {
		throw new UnsupportedOperationException("not yet implmented for fights");
	}
	
	public void update (Event event) {
		try {
		// create statement and set timeout to 30 sec.
		Statement statement = connection.createStatement();						
		statement.setQueryTimeout(30);				
		statement.executeUpdate("insert or replace into events values" + event.toSqlString());
		
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e);
			System.err.println(e.getMessage());
		}			
	}
	
	public ArrayList<Fighter> get (Fighter fighter) {
		ArrayList<Fighter> fightersResult = new ArrayList<Fighter>(); 
		
		try {
			
			Statement statement = connection.createStatement();						
			statement.setQueryTimeout(30);				
			ResultSet rs = statement.executeQuery("select * from fighters");
						
			while (rs.next()) {
				// read the result set
				fightersResult.add(new Fighter(rs.getInt("id"), rs.getInt("age"), rs.getInt("str_acc"), rs.getInt("str_def"),
									rs.getInt("td_acc"), rs.getInt("td_def"), rs.getInt("w"), rs.getInt("l"), rs.getInt("d"),
									rs.getInt("nc"), rs.getString("name"), rs.getString("nickname"), rs.getString("height"),
									rs.getString("weight"), rs.getString("reach"), rs.getString("stance"), rs.getDouble("slpm"), 
									rs.getDouble("sapm"), rs.getDouble("td_avg"), rs.getDouble("sub_avg")));				
			}
					/*int id, int age, int str_acc, int str_def, int td_acc,
	int td_def, int w, int l, int d, int nc, String name,
	String nickname, String height, String weight, String reach,
	String stance, double slpm, double sapm, double td_avg,
	double sub_avg*/
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return fightersResult;
	}
	
	public ArrayList<Fight> get (Fight fight) {
		throw new UnsupportedOperationException("not yet implmented for fights");
	}
	
	public ArrayList<Event> get (Event event) {
		ArrayList<Event> eventsResult = new ArrayList<Event>(); 
		
		try {
			
			Statement statement = connection.createStatement();						
			statement.setQueryTimeout(30);				
			ResultSet rs = statement.executeQuery("select * from events");
						
			while (rs.next()) {
				// read the result set
				eventsResult.add(new Event(rs.getInt("id"), rs.getString("name"), rs.getString("date"), rs.getString("location"),
									rs.getString("organization"), rs.getString("attendance")));				
			}
						
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return eventsResult;
	}
	

		

	
	public void close () {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			// connection close failed.
			System.err.println(e);
			System.err.println(e.getMessage());
		}
							
	}
}
