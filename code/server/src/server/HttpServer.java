package server;

import java.io.* ;
import java.net.* ;
import java.util.* ;
import database.StatsHandler;

/**
 * httpServer main class
 * @author Stugatz
 * Accepts connections on server socket and then delegates them to request threads on dynamically assigned sockets.
 */
public class HttpServer implements Runnable {
    final static int port = 80;
    private ServerSocket socket;
    boolean stop = false;
    
	@Override
	public void run() {     
		try {
			socket = new ServerSocket(port);
			System.out.println("<Server> Server started, accepting connections on port: " + port +
                 " ,thread id: " + Thread.currentThread().getId());
		} catch (IOException e) {
			System.out.println("<Server Error> Failed to initialize server socket, stopping server."
					+ " Make sure that you aren't running another web server or a program such as skype that uses port 80."
					+ " Thread id: " + Thread.currentThread().getId());
			stop = true;
		} 
        
		HttpRequest httpRequest;               
        while (!stop) {
            try {
				httpRequest = new HttpRequest( socket.accept() );
	            Thread thread = new Thread( httpRequest );
	            thread.start();
			} catch (IOException e) {
				System.out.println("<Server Error> Failed to accept connection, socket error."
						+ " Thread id: " + Thread.currentThread().getId());
			}

        }
              		
	}

	@Override
	protected void finalize() throws Throwable {
		// Makes sure server socket is closed.
		socket.close();
		super.finalize();
	}

}

/**
 * @author Stugatz
 *
 */
class HttpRequest implements Runnable{
	private HashMap<String, String> tables;	
	private StatsHandler db;
    private Socket socket;
    final static String CRLF = "\r\n";
    // Shouldn't be used in production
    final static File webRoot = new File(System.getProperty("user.dir") + "\\www\\");
    private InputStreamReader is;
	private DataOutputStream os;
	private long requestTime = System.currentTimeMillis();
	// How many times to retry before timeout. Tries every 5 MS.
    private final int RETRIES = 100;
    // Receives socket from server thread
    public HttpRequest(Socket socket) {
        this.socket = socket;        
    }

    @Override
    final public void run() {
    	 System.out.println("<Server> accepted new connection on port: " + socket.getPort() +
                 " ,thread id: " + Thread.currentThread().getId());
    	 
    	HashMap<String, String> request;
    	db = new StatsHandler();
    	
    	// hackkkkkkkkkkkk...
    	tables = new HashMap<String, String>();
    	tables.put(".fighters", "fighters");
    	tables.put(".events", "events");
    	    	
	    try {
	    	// Each request thread starts it's on database connection which should be thread safe on normally compiled SqLite drivers.
       	 	db = new StatsHandler();
	        // Get a reference to the socket's input and output streams.
			is = new InputStreamReader( socket.getInputStream() );
			os = new DataOutputStream( socket.getOutputStream() );
			// Set up input stream filters.
			BufferedReader br = new BufferedReader (is);
	        		
			// Wait for inputStream and time out if it takes too long. 
			int i = 0; boolean stop = false;
			while(!br.ready()) {
				if (++i > RETRIES) {
					stop = true;
					break;
				}
				Thread.sleep(5);				
			}
			
			// Extract the request fields
			StringTokenizer fields;
			request = new HashMap<String, String>();	
			boolean firstLine = true;
			String requestLine = null;
			
			if (!stop) {
		        while ((requestLine = br.readLine()).length() != 0) {
		        	fields = new StringTokenizer(requestLine, ": ");	     
		        	// Handle the method line 
		        	if(firstLine && fields.countTokens() == 3) {
		        		request.put("Request-Method", fields.nextToken());
		        		request.put("Request-URL", fields.nextToken());
		        		request.put("Http-Version", fields.nextToken());
		        		firstLine = false;
		        	} else if (fields.countTokens() >= 2) {
		        		request.put(fields.nextToken(), fields.nextToken("").substring(2));
		        	} else {
		        		System.out.println("<Server Error> Read error, missing fields " + requestLine 
		        					+ " .Thread id: " + Thread.currentThread().getId());
		        		break;
		        	}
		        }	    
			}
	        
	        //process request
	        if(!request.isEmpty()) {
	        	System.out.println("<Server> Processing request, thread id: " + Thread.currentThread().getId());
	        	processRequest(request);
	        } else {
	        	System.out.println("<Server> No request recieved, timed out. Thread id: " + Thread.currentThread().getId());
	        }

			// Close DB connection, sockets and streams
			db.close();             
			br.close();
			os.close();
			socket.close();
			System.out.println("<Server> Closing request thread, thread id: " + Thread.currentThread().getId());	    
			 
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }    
    }
    
    
    /**
     * @throws Exception
     * Handles the request
     */
    private void processRequest(HashMap<String, String> request) throws Exception
    {	              	
        // Construct the response message.
        StringBuilder page = new StringBuilder();
        
        // Check for correct headers and methods
        if (request.containsKey("Request-URL") && request.containsKey("Request-Method")) {          
        	if (request.get("Request-Method").toString().equals("GET")) {
        		String requestUrl = request.get("Request-URL").toString();
                
        		// Rewrite URL
                if (requestUrl.equals("/")) {
                	requestUrl = "/index.html";                	
                } else if (requestUrl.endsWith("/")) {
                	requestUrl = requestUrl.substring(0, requestUrl.length() - 1);
                }
                
                // Check if object exists ** currently hack for database objects **            	                                
                File file = new File(webRoot, requestUrl);                
                boolean dbObject = (requestUrl.endsWith("events.html") ? true : false);
                dbObject = (requestUrl.endsWith("fighters.html") ? true : false);
                boolean objectExists = (dbObject || file.exists());
                
                
                System.out.println("<Server> Client sent request for: " + file.getPath() +  " ,thread id: " + Thread.currentThread().getId());
                
                // Build page
                if (!objectExists) {
                	// Send 404 message        
                	System.out.println("<Server> File not found, sending 404 message, thread id: " + Thread.currentThread().getId());
                	page.append("HTTP/1.0 404 Not Found" + CRLF);
                	page.append("Content-type: text/html" + CRLF);
                	page.append(CRLF);
                    page.append("<HTML><HEAD><TITLE>Not Found</TITLE></HEAD>" +
                            "<BODY>The requested file was not found on the server.</BODY></HTML>" + CRLF);	
                    
                    if (socket.isOutputShutdown()) {
                    	System.out.println("<Server Error> Socket is shut down, can't write. Thread id: " + Thread.currentThread().getId());
                    } else {
                    	os.writeBytes(page.toString());
                    }
                    
                } else if (dbObject) {
                
                	// Build page from database
                	System.out.println("<Server> Sending DB object, thread id: " + Thread.currentThread().getId());
            		page.append("HTTP/1.0 200 OK" + CRLF);
                    page.append("Content-type: text/html" + CRLF);
                    page.append(CRLF);
                    page.append("<!DOCTYPE HTML PUBLIC &quot-//W3C//DTD HTML 4.01 Transitional//EN&quot &quothttp://www.w3.org/TR/html4/loose.dtd&quot>" + CRLF);                   
                    page.append("<HTML><HEAD><TITLE>Events</TITLE></HEAD> <BODY><table border='1'>" + CRLF);
                    page.append("<tr><th> id </th><th> name </th><th> date </th><th> location </th><th> organization </th><th> attendence </th></tr>" + CRLF);
                    
                    // Get table from DB
//                    ArrayList<Event> events = db.get(new Event());
//                    for (Event event : events) {
//                    	page.append(event.toHtmlString());
//                    	page.append(CRLF);
//            		}                    
 
                    page.append("</table></BODY></HTML>" + CRLF);
                    os.writeBytes(page.toString());                    
                } else {
                	// Send static file
                	System.out.println("<Server> Sending file, thread id: " + Thread.currentThread().getId());
            		page.append("HTTP/1.0 200 OK" + CRLF);
                    page.append("Content-type: " + contentType(requestUrl) + CRLF);
                    page.append(CRLF);
                    
                    // Send the fileStream directly to the socketStream
                    FileInputStream fis = null;
                    try {
                    	fis = new FileInputStream(file.getPath());
                    	sendBytes(fis, os);
                        fis.close();
                    } catch (FileNotFoundException e) {
                    	// Should give 404
                    	System.out.println("<Server Error> IO error on serving static page, thread id: " + Thread.currentThread().getId());
                    }
                }

                System.out.println("<Server> page served in " + (System.currentTimeMillis()-requestTime + " milliseconds, " 
                		+ "thread id: " + Thread.currentThread().getId()));
        		
        	} else {
        		System.out.println("<Server> Request method not recognized, dropping connection. Thread id: " + Thread.currentThread().getId());
        	}
            
        } else {
        	System.out.println("<Server> Headers missing, dropping connection. Thread id: " + Thread.currentThread().getId());
        }
           
    }
    
    // Writes the fileStream directly to the socket. 
    private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception {
       // Construct a 1K buffer to hold bytes on their way to the socket.
       byte[] buffer = new byte[1024];
       int bytes = 0;

       // Copy requested file into the socket's output stream.
       while((bytes = fis.read(buffer)) != -1 ) {
          os.write(buffer, 0, bytes);
       }
    }

    // Extracts the file type based on file ending.
    private static String contentType(String fileName)
    {
            if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
                    return "text/html";
            }
            if( fileName.endsWith(".gif") ) {
                    return "text/gif";
            }
            if( fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                    return "text/jpeg";
            }
            
            return "application/octet-stream";
    }    
    
}