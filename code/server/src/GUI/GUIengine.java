package GUI;

import server.HttpServer;


public class GUIengine {
	
	public GUIengine() {
		new GUImenu();
		//new GUIfightcard();
	}
	
	public static void main(String[] args) {
		 new GUIengine();
		 
		 try {
				HttpServer httpServer = new HttpServer();
	            Thread thread = new Thread( httpServer );
	            thread.start();
			} catch (Exception e) {
				System.out.println(e);
			}
	}
}
