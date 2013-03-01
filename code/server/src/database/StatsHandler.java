package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @author Stugatz
 *
 */
public class StatsHandler extends DbHandler {
	private Connection connection = null;

	public StatsHandler () {
		try {
			// load the Sqlite-JDBC driver using the current class loader
			Class.forName("org.sqlite.JDBC");						
			// create a database connection
			this.connection = DriverManager.getConnection("jdbc:sqlite:stats.db");
			
		} catch (ClassNotFoundException e) {
			System.err.println(e);
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println(e);
			System.err.println(e.getMessage());
		}
	}
	
	
	/**
	 * Creates the stats-tables if they don't exist
	 */
	public void resetSchema (HashMap<String[], String> schema) {
		try {	
			Statement statement = connection.createStatement();						
			statement.setQueryTimeout(30);			
			
			statement.execute("drop table if exists schema");
			statement.execute("create table schema ('table' text NOT NULL, 'column' text NOT NULL, 'attribute' text NOT NULL, 'value' text, PRIMARY KEY('table', 'column', 'attribute'))");
			
			statement.execute("BEGIN IMMEDIATE");
			Iterator<Entry<String[], String>> itr = schema.entrySet().iterator();
			while(itr.hasNext()) {
				Entry<String[], String> entry = itr.next();
				String[] key = entry.getKey();
				String value = entry.getValue();

				statement.execute("insert or replace into schema values" + 
						 " ('"+ key[0] + "','" + key[1] + "','" + key[2] + "','" + value + "')" );
			}
			statement.execute("COMMIT");
			
		} catch (SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
			System.err.println(e);
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
		
		//createTables();
	}
	
	// SHould be moved to it's own class
	private void format(String value) {
		
	}
	
	public void update (String table, HashMap<String, String> columnValuePair) {
		try {
			// create statement and set timeout to 30 sec.
			Statement statement = connection.createStatement();						
			statement.setQueryTimeout(30);		
			
			StringBuilder vals = new StringBuilder("(");
			StringBuilder cols = new StringBuilder("(");
			Iterator<Entry<String, String>> itr = columnValuePair.entrySet().iterator();
						
			while(itr.hasNext()) {
				Entry<String, String> entry = itr.next();				
				
				String dbtype = Settings.getInstance().getSchema().get(table + "-" + entry.getKey() + "-" + "dbtype");
				String formatter = Settings.getInstance().getSchema().get(table + "-" + entry.getKey() + "-" + "formatter");
				String dbname = Settings.getInstance().getSchema().get(table + "-" + entry.getKey() + "-" + "dbname");
								
				cols.append(dbname);
				
				if (formatter != null) {
					//formatter logic
					String temp = ("'" + entry.getValue().replace("'", "''") + "'");
					System.out.println(temp);
					vals.append(temp);
				} else {
					if (dbtype.equals("text")) {
						vals.append("'" + entry.getValue() + "'");
					} else if (dbtype.equals("integer")) {
						vals.append(entry.getValue());
					} else {
						vals.append("'" + entry.getValue() + "'");
					}
				}
				
				if (itr.hasNext()) {
					cols.append(",");
					vals.append(",");
				} else {
					cols.append(")");
					vals.append(")");
				}
			}
			
			statement.executeUpdate("insert or replace into " + table + " " +
					cols.toString() + " VALUES " + vals.toString());						
		
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e);
			e.printStackTrace();
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
