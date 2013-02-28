package GUI;

import javax.swing.SwingUtilities;
import database.Settings;
import database.StatsHandler;
import server.HttpServer;


public class GUIengine {
	public static void main(String[] args) { 
		 try {
			 	//Get settings
			 	Settings.getInstance();
			 	//Update schema table
			 	StatsHandler db = new StatsHandler();
			 	db.resetSchema(Settings.getInstance().getSchema());
			 
			 	// Run http server
				HttpServer httpServer = new HttpServer();
	            Thread thread = new Thread( httpServer );
	            thread.start();
	            
	            // run gui
	            SwingUtilities.invokeLater(new Runnable() {
	                public void run() {
	                	new GUImenu();
	                }
	            });
			} catch (Exception e) {
				System.out.println(e);
			}
	}
}
