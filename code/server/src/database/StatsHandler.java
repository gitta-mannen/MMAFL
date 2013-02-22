package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatsHandler extends DbHandler {
	private Connection connection = null;

	public StatsHandler () {
		try {
			// load the sqlite-JDBC driver using the current class loader
			Class.forName("org.sqlite.JDBC");
						
			// create a database connection
			this.connection = DriverManager.getConnection("jdbc:sqlite:stats.db");

			// create statement and set timeout to 30 sec.
			Statement statement = connection.createStatement();						
			statement.setQueryTimeout(30);
			
			statement.executeUpdate("drop table if exists fighters");
			statement.executeUpdate("create table fighters (id integer)");
			
			statement.executeUpdate("drop table if exists events");
			statement.executeUpdate("create table events (id integer, name string, date string,	location string, organization string, attendance string)");
			
			statement.executeUpdate("drop table if exists fights");
			statement.executeUpdate("create table fights (id integer)");				
		} catch (ClassNotFoundException e) {
			System.err.println(e);
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println(e);
			System.err.println(e.getMessage());
		}
	}
	
	public void update (Fighter fighter) {
		throw new UnsupportedOperationException("not yet implmented for fighters");
	}
	
	public void update (Fight fight) {
		throw new UnsupportedOperationException("not yet implmented for fights");
	}
	
	public void update (Event event) {
		try {
		// create statement and set timeout to 30 sec.
		Statement statement = connection.createStatement();						
		statement.setQueryTimeout(30);				
		statement.executeUpdate("insert into events values" + event.getInserFormat());
		
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e);
			System.err.println(e.getMessage());
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
