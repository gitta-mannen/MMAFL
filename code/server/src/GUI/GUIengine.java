package gui;

import javax.swing.SwingUtilities;
import database.StatsHandler;
import server.HttpServer;
import util.Logger;

public class GUIengine {
	public static void main(String[] args) { 
		 try {			 	
			 	// reset the tables on startup			 	
			 	// parameter false means that existing tables aren't overwritten
			 	StatsHandler db = new StatsHandler();
			 	db.resetTables(false);
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
				Logger.log(e.getMessage(), true);
			}
	}
}
