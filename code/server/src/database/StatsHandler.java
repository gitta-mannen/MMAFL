package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import settings.Column;
import settings.Schema;
import settings.Settings;
import settings.Table;
import util.Logger;
import util.Pair;

/**
 * Handles the database connection. Each thread should have it's own instance.
 * 
 * @author Stugatz
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
			Logger.log(e.getMessage(), true);
		} catch (Exception e) {
			Logger.log(e.getMessage(), true);
		}
	}
	

	/**
	 * Creates the tables based on the schema in the settings Object
	 * 
	 * @param dropExisting indicates if existing tables should be rebuilt.
	 */
	public void resetTables (boolean dropExisting) {
		try {	
			Statement statement = connection.createStatement();						
			statement.setQueryTimeout(30);			
			
			Schema schema = Settings.getInstance().getSchema();
			
			statement.executeUpdate("BEGIN");
			Iterator<Entry<String, Table>> tItr = schema.getTables().entrySet().iterator();			
			while(tItr.hasNext()) {
				Entry<String, Table> tableEntry = tItr.next();
				if (dropExisting) {
					statement.executeUpdate("DROP TABLE IF EXISTS " + tableEntry.getKey());
				}
				
				Iterator<Entry<String, Column>> cItr = tableEntry.getValue().getColumns().entrySet().iterator();
				StringBuilder columnDef = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableEntry.getKey() + " (");				
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
			Logger.log(e.getMessage(), true);
		}			
	}
	
	/**
	 * Takes a table name and return the tables contents, as well as column names
	 * 
	 * @param table name of the table
	 * @return A Pair where the first element is an String array
	 * 	containing the column names. The second element is an
	 *  Array of Object Arrays holding the data.
	 *  Return null if query return an empty record.
	 *   
	 */
	public Pair<String[], Object[][]> getTable(String table) {	
		Object[][] resultArray = null;
		String[] headerArray = null;
		
		try {	
			Statement statement = connection.createStatement();						
			statement.setQueryTimeout(30);						
			
			//get row count, Sqlite result sets aren't rewindable
			ResultSet rs = statement.executeQuery("SELECT Count(*) AS rowCount FROM " + table);
		    int rowCount = rs.getInt("rowCount"); 
		    
		    // get data
		    rs = statement.executeQuery("SELECT * FROM " + table);
		    
			//get column count
			ResultSetMetaData meta = rs.getMetaData();			
			int colCount = meta.getColumnCount();			
		    
		    //create array of proper size for column names and data
			resultArray = new Object[rowCount][colCount];
			headerArray = new String[colCount];
			
			// Fill column names
			for (int col = 0; col < colCount; col++) {
				headerArray[col] = meta.getColumnName(col + 1);
			}					
			
			//add data 				
			int row = 0;
			while (rs.next()) {
				for(int col = 0; col < colCount; col++) {
					resultArray[row][col] = rs.getString(col + 1);
				}
				row++;
			}							
			
		} catch (Exception e) {
			Logger.log(e.toString(), true);
		} 
		
	    if (headerArray != null)  {
	    	return new Pair<String[], Object[][]>(headerArray, resultArray);
	    } else {
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
			Logger.log(e.getMessage(), true);
		}
							
	}
}
