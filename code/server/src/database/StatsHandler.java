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
	public void resetTables () {
		try {	
			Statement statement = connection.createStatement();						
			statement.setQueryTimeout(30);			
			
			Schema schema = Settings.getInstance().schema();
			
			//statement.executeUpdate("BEGIN");
			Iterator<Entry<String, Table>> tItr = schema.getTables().entrySet().iterator();			
			while(tItr.hasNext()) {
				Entry<String, Table> tableEntry = tItr.next();
				statement.executeUpdate("drop table if exists " + tableEntry.getKey());
				
				Iterator<Entry<String, Column>> cItr = tableEntry.getValue().getColumns().entrySet().iterator();
				StringBuilder columnDef = new StringBuilder("CREATE TABLE " + tableEntry.getKey() + " (");				
				while(cItr.hasNext()) {
					Entry<String, Column> columnEntry = cItr.next();
					columnDef.append("'" + columnEntry.getValue().dbname + "'" + " " + columnEntry.getValue().dbtype);
					if(cItr.hasNext()) {						
						columnDef.append(", ");
					}
				}
				columnDef.append(")");
				//System.out.println(columnDef.toString());
				statement.execute(columnDef.toString());
			}
			
			
			//statement.execute("COMMIT");
		} catch (Exception e) {
			Logger.log(e.getMessage(), true);
		}
	}
//			//statement.execute("drop table if exists schema");
//			//statement.execute("create table schema ('table' text NOT NULL, 'column' text NOT NULL, 'attribute' text NOT NULL, 'value' text, PRIMARY KEY('table', 'column', 'attribute'))");
//			
//			statement.execute("BEGIN IMMEDIATE");
//			Iterator<Entry<String, String>> itr = Settings.getInstance().schema().entrySet().iterator();
//			while(itr.hasNext()) {
//				Entry<String, String> entry = itr.next();
//				String[] key = entry.getKey().split("-");
//				String value = entry.getValue();
//
//				statement.execute("CREATE TABLE IF NOT EXISTS " + key[0]);
//			}
//			statement.execute("COMMIT");
//			
//		} catch (SQLException e) {
//			System.err.println(e);
//		} catch (Exception e) {
//			System.err.println(e);
//		}			
//	}
	
	
	/*
	public void resetTables () {
		try {				
			Statement statement = connection.createStatement();						
			statement.setQueryTimeout(30);			
	

						
		} catch (SQLException e) {
			System.err.println(e);
			System.err.println(e.getMessage());
		}	
		
		//createTables();
	}
	*/
	
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
				
				//System.out.println(table + " " + entry.getKey());
				
				String dbtype = Settings.getInstance().schema().getTable(table).getColumn(entry.getKey()).dbtype;
				String formatter = Settings.getInstance().schema().getTable(table).getColumn(entry.getKey()).formatter;
				String dbname = Settings.getInstance().schema().getTable(table).getColumn(entry.getKey()).dbname;
				
				if (dbtype == null) {
					Logger.log("Reference to table: " + table + " column: " + entry.getKey() + " not found in schema", true);
				} else {				
					cols.append(dbname);
					//System.out.println(dbtype + " " + formatter + " " + dbname);
					
					if (formatter != null) {
						//formatter logic
						String temp = ("'" + entry.getValue().replace("'", "''") + "'");
						temp = ("'" + entry.getValue().replace(",", ".") + "'");
						//System.out.println(temp);
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
			}
			//System.out.println("insert or replace into " + table + " " +
			//		cols.toString() + " VALUES " + vals.toString());
			
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
