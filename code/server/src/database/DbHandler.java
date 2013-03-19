package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import settings.Settings;
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
//	private ResultSet rs = null;
	
	static {
		// create tables on first use
		System.out.println("static constructor"); 
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
				System.out.println("prepared statement added: " + pStatement[i].getA() + " = " + pStatement[i].getB());
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
	
	public static void buildTables(boolean replace) {
		System.out.println("build table method");
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:stats.db");
			Statement statement = con.createStatement();
			statement.setQueryTimeout(10);
			con.setAutoCommit(false);
			
			String[] tables = Settings.getNodeText("database:tables:table");
			System.out.println("table count: " + tables.length);
			
			for (int i = 0; i < tables.length; i++) {
				System.out.println("building table: " + tables[i]); 
				if (replace) {
					statement.execute("DROP TABLE IF EXISTS " + tables[i]);
				}
				
				String primaryKey =  Settings.getNodeText("database:tables:" + tables[i] + ":primary-key")[0];
				String[] columns =  Settings.getNodeText("database:tables:" + tables[i] + ":column:name");
				String[] types =  Settings.getNodeText("database:tables:" + tables[i] + ":column:dbtype");
				
				StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tables[i] + " (");
				for (int c = 0; c < columns.length; c++) {
					sb.append("'" + columns[c] + "' " + types[c]);
					sb.append( c + 1 == columns.length ? ", PRIMARY KEY (" + "'" + primaryKey + "'" + "))" : ", " );
				}
				
				System.out.println("table build statement: " + sb.toString()); 
				statement.execute(sb.toString());
			}
					
			con.setAutoCommit(true);
			con.close();
			
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setAutoCommit(boolean auto) throws SQLException {
		connection.setAutoCommit(auto);
	}
	
	public void executePs (String stName, Object[] params) {
		try {
			PreparedStatement s = ps.get(stName);
			if (s == null) {
				throw new Exception("prepared statement " + stName + " not found.");
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
