package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import settings.Column;
import settings.Constants.DbType;
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
public class DbHandler {
	private Connection connection = null;
	private final HashMap<String, PreparedStatement> ps = new HashMap<String, PreparedStatement>();
	private ResultSet rs = null;
	
	static {
		// create tables on first use
		buildTables(false);
	}
	
	public DbHandler (Pair<String, String>[] pStatement) {
		try {
			// load the Sqlite-JDBC driver using the current class loader
			Class.forName("org.sqlite.JDBC");						
			// create a database connection
			this.connection = DriverManager.getConnection("jdbc:sqlite:stats.db");

			// get prepared statements from settings
			for (int i = 0; i < pStatement.length; i++) {
				System.out.println("prepared statement added: " + pStatement[i]);
				ps.put(pStatement[i].getA(), connection.prepareStatement(pStatement[i].getB()) );
			}
					
		} catch (Exception e) {
			Logger.log(e, true);
		}
	}
	
	public DbHandler () {
		try {

			Class.forName("org.sqlite.JDBC");						
			this.connection = DriverManager.getConnection("jdbc:sqlite:stats.db");				
		} catch (Exception e) {
			Logger.log(e, true);
		}
	}
	
	public static synchronized void buildTables(boolean replace) {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:stats.db");
			Statement statement = con.createStatement();
			statement.setQueryTimeout(10);
			
			if (replace) {
				Iterator<String> tables = Settings.getTables();
				while (tables.hasNext()) {
					statement.execute("DROP TABLE IF EXISTS " + tables.next());
				}
			}
			
			Iterator<String> tableDefs = Settings.getTableDefs();
			while (tableDefs.hasNext()) {
				String s = tableDefs.next();
				System.out.println(s);
				statement.execute(s);
			}
			con.close();
			
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setAutoCommit(boolean auto) throws SQLException {
		connection.setAutoCommit(auto);
	}
	
	public void executePs (String stName, String table, Object[] params) {
		try {
			PreparedStatement s = ps.get(stName + ":" + table);
			if (s == null) {
				throw new Exception("prepared statement " + stName + ":" + table + " not found.");
			}
			
			if (params != null) {
				if (s.getParameterMetaData().getParameterCount() == params.length) {
					for (int i = 1; i <= params.length; i++) {
						s.setObject(i, params[i-1]);
					}					
				} else {
					throw new Exception("Wrong number of params, " + params.length);
				}
			}
			
			s.execute();
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	/**
	 * Creates the tables based on the schema in the settings Object
	 * 
	 * @param dropExisting indicates if existing tables should be rebuilt.
	 */
//	public void resetTables (boolean dropExisting) {
//		try {	
//			Statement statement = connection.createStatement();						
//			statement.setQueryTimeout(30);			
//			
//			Settings.getInstance();
//			Schema schema = Settings.getSchema();
//			
//			statement.executeUpdate("BEGIN");
//			Iterator<Entry<String, Table>> tItr = schema.getTables().entrySet().iterator();			
//			while(tItr.hasNext()) {
//				Entry<String, Table> tableEntry = tItr.next();
//				if (dropExisting) {
//					statement.executeUpdate("DROP TABLE IF EXISTS " + tableEntry.getKey());
//				}
//				
//				//build the sql command
//				Iterator<Entry<String, Column>> cItr = tableEntry.getValue().getColumns().entrySet().iterator();
//				StringBuilder columnDef = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableEntry.getKey() + " (");				
//				while(cItr.hasNext()) {
//					Entry<String, Column> columnEntry = cItr.next();
//					columnDef.append("'" + columnEntry.getValue().dbname + "'" + " " + columnEntry.getValue().dbtype + " " + columnEntry.getValue().constraint);
//					if(cItr.hasNext()) {						
//						columnDef.append(", ");
//					}
//				}
//				columnDef.append(")");
//				// Create the table
//				statement.execute(columnDef.toString());
//			}						
//			statement.execute("COMMIT");
//		} catch (Exception e) {
//			Logger.log(e.getMessage(), true);
//		}
//	}
	
//	public void update (String table, HashMap<String, Object> columnValuePair) {	
//		try {
//			StringBuilder params = new StringBuilder("(");
//			LinkedList<Object> vals = new LinkedList<Object>();
//			StringBuilder cols = new StringBuilder("(");
//			Iterator<Entry<String, Object>> itr = columnValuePair.entrySet().iterator();
//						
//			while(itr.hasNext()) {
//				Entry<String, Object> entry = itr.next();								
//				Settings.getInstance();
//				String dbColumn = Settings.getSchema().getTable(table).getColumn(entry.getKey()).dbname;
//				
//				if (dbColumn == null) {
//					Logger.log("Reference to table: " + table + " column: " + entry.getKey() + " not found in schema", true);
//				} else {				
//					params.append("?");					
//					cols.append(dbColumn);
//					vals.add(entry.getValue());
//				}
//				
//				params.append(itr.hasNext() ? ", " : ")");
//				cols.append(itr.hasNext() ? ", " : ")");
//			}
//				final PreparedStatement pStmt = connection.prepareStatement("insert or replace into " + table + " " + cols.toString() + " values " + params.toString());
//				
//				//System.out.println(vals.size());
//				
//				int i = 1;
//				for (Object o : vals) {
//					//System.out.println(i + ": " + o.toString());
//					pStmt.setObject(i++, o);
//				}				
//				pStmt.executeUpdate();								
//				
//		} catch (SQLException e) {
//			Logger.log(e.getMessage(), true);
//		}			
//	}
	
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
					resultArray[row][col] = rs.getObject(col + 1);
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
