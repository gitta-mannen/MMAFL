package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import util.Logger;

/**
 * @author Stugatz
 *
 */
public class StatsHandler {
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
			
			Schema schema = Settings.getInstance().getSchema();
			
			statement.executeUpdate("BEGIN");
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
			
			
			statement.execute("COMMIT");
		} catch (Exception e) {
			Logger.log(e.getMessage(), true);
		}
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
				
				String dbtype = Settings.getInstance().getSchema().getTable(table).getColumn(entry.getKey()).dbtype;
				String formatter = Settings.getInstance().getSchema().getTable(table).getColumn(entry.getKey()).formatter;
				String dbname = Settings.getInstance().getSchema().getTable(table).getColumn(entry.getKey()).dbname;
				
				if (dbtype == null) {
					Logger.log("Reference to table: " + table + " column: " + entry.getKey() + " not found in schema", true);
				} else {				
					cols.append(dbname);
					//System.out.println(dbtype + " " + formatter + " " + dbname);
					
					if (formatter != null) {
						//formatter logic
						String temp = ("'" + entry.getValue().replace("'", "") + "'");
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
	
	public Object[][] getTable(String table) {	
		try {
			Statement statement = connection.createStatement();						
			statement.setQueryTimeout(30);		
			
			ResultSet result = statement.executeQuery("SELECT * FROM " + table);
			
			
			ResultSetMetaData meta = result.getMetaData();			
			int count = meta.getColumnCount();
			Object[][] resultArray = new Object[1][count];
			
			for (int i = 0; i < count; i++) {
				resultArray[0][i] = meta.getColumnName(i+1);
			}					
			
			return resultArray;
			
		} catch (Exception e) {
			Logger.log(e.getMessage(), true);
			return null;
		} 

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
