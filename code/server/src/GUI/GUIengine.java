package GUI;

import javax.swing.SwingUtilities;
import server.HttpServer;


public class GUIengine {
	public static void main(String[] args) { 
		 try {
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
