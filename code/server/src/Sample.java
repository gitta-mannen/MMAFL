import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sample
{
  public static void main(String[] args) throws ClassNotFoundException
  {
    // load the sqlite-JDBC driver using the current class loader
    Class.forName("org.sqlite.JDBC");

    Connection connection = null;
    try
    {
      // create a database connection -- gör en gång
      connection = DriverManager.getConnection("jdbc:sqlite:fightstats.db");
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.

      // en gång per scraper class
      statement.executeUpdate("drop table if exists fighters");
      statement.executeUpdate("create table fighters (id integer, name string, nickname string)");
      
      // här initierar du scrape klassen (fighter etc)
      
      //scarpe loop
	      // Här scrapar du
	      
	      // en gång per scrape(fighter, event etc.)
	      statement.executeUpdate("insert into fighters values(1, 'bertil', 'the nightmare')");
	      statement.executeUpdate("insert into fighters values(2, 'rötivar', 'cripple')");
      
      // Gör servern inte scrapern
      ResultSet rs = statement.executeQuery("select * from person");
      while(rs.next())
      {
        // read the result set
        System.out.println("name = " + rs.getString("name"));
        System.out.println("id = " + rs.getInt("id"));
      }
    }
    
    // exception handeling och avslutning av connection
    catch(SQLException e)
    {
      // if the error message is "out of memory", 
      // it probably means no database file is found
      System.err.println(e.getMessage());
    }
    finally
    {
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e);
      }
    }
  }
}