package GUI;

import java.util.HashMap;

import javax.swing.SwingUtilities;
import database.Settings;
import database.StatsHandler;
import server.HttpServer;


public class GUIengine {
	public static void main(String[] args) { 
		 try {			 	
			 	//Get settings
			 	Settings.getInstance();
			 	// reset the tables on startup
			 	StatsHandler db = new StatsHandler();
			 	db.resetTables();
			 	db.close();
			 	
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
