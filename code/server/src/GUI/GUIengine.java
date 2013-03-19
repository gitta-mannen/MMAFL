package gui;

import javax.swing.SwingUtilities;
import database.DbHandler;
import server.HttpServer;
import util.Logger;

public class GUIengine {
	public static void main(String[] args) { 
		 try {	
			 
			 	// Run scraper
			 		//scheduledExecutorService...
			 
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
