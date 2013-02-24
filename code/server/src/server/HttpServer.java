package server;

import java.io.* ;
import java.net.* ;
import java.util.* ;

import database.Event;
import database.StatsHandler;

/**
 * httpServer main class
 * @author Stugatz
 * Accepts connections on server socket and then delegates them to request threads on dynamically assigned sockets.
 */
public class HttpServer implements Runnable {
    final static int port = 999;
    private ServerSocket socket;
    
	@Override
	public void run() {     
		try {
			socket = new ServerSocket(port);
			
		} catch (IOException e) {
			System.out.println("Failed to initialize server socket, stopping server.");
		} 
        
		HttpRequest httpRequest;               
        while (true) {
            try {
				httpRequest = new HttpRequest( socket.accept() );
	            Thread thread = new Thread( httpRequest );
	            thread.start();
			} catch (IOException e) {
				System.out.println("Filed to accept connection, socket error.");
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
	private StatsHandler db;
    private Socket socket;
    final static String CRLF = "\r\n";
    // Shouldn't be used in production
    final static File webRoot = new File(System.getProperty("user.dir") + "\\www\\");
    private InputStreamReader is;
	private DataOutputStream os;
    
    // Receives socket from server thread
    public HttpRequest(Socket socket) {
        this.socket = socket;        
    }

    @Override
    final public void run() {
    	 System.out.println("*** Server accepted new connection on port: " + socket.getPort() +
                 " thread id: " + Thread.currentThread().getId() +  " ***");
    
    	long startTime = System.currentTimeMillis();
    	String requestLine;
    	HashMap<String, String> request;
    	db = new StatsHandler();
    	
	    try {
	    	// Each request thread starts it's on database connection which should be thread safe on normally compiled SqLite drivers.
       	 	db = new StatsHandler();
	        // Get a reference to the socket's input and output streams.
			is = new InputStreamReader( socket.getInputStream() );
			os = new DataOutputStream( socket.getOutputStream() );
			// Set up input stream filters.
			BufferedReader br = new BufferedReader (is);
	        // Extract the request fields
			StringTokenizer fields;
			request = new HashMap<String, String>();	
			boolean firstLine = true;
			
	        while ((br.ready() && (requestLine = br.readLine()).length() != 0) ) {
	        	System.out.println(requestLine);
	        	fields = new StringTokenizer(requestLine, ": ");	     
	        	
	        	if(firstLine && fields.countTokens() == 3) {
	        		request.put("Request-Method", fields.nextToken());
	        		request.put("Request-URL", fields.nextToken());
	        		request.put("Http-Version", fields.nextToken());
	        		firstLine = false;
	        	} else {
	        		request.put(fields.nextToken(), fields.nextToken("").substring(2));
	        	}
	        }	        	       
	        
	        if(!request.isEmpty()) {
		        System.out.println(request.toString());
	        	processRequest(request);
	        }

			// Close DB connection, sockets and streams
			db.close();             
			br.close();
			os.close();
			socket.close();
			System.out.println("Thread: " + Thread.currentThread().getId() + " Closing thread");	    
			 
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }    
    }
    
    
    /**
     * @throws Exception
     * Handles the request
     */
    private void processRequest(HashMap request) throws Exception
    {	           
        // Construct the response message.
        String statusLine = null;
        String contentTypeLine = null;
        String requestUrl = null;
       
        if (request.containsKey("Request-URL") && request.containsKey("Request-Method")) {          
        	if (request.get("Request-Method").toString().equals("GET")) {
        		requestUrl = request.get("Request-URL").toString();
                                
                if (requestUrl.equals("/")) {
                	requestUrl = "/index.html";                	
                }
                
                File file = new File(webRoot, requestUrl);
                System.out.println("Client sent request for: " + file.getPath());
                
                if (requestUrl.endsWith("events.html") || requestUrl.endsWith("events.html/")) {
                	System.out.println("sending events");
                	StringBuilder page = new StringBuilder();
                	
            		page.append("HTTP/1.0 200 OK");
            		page.append(CRLF);
                    page.append("Content-type: text/html");
                    page.append(CRLF);
                    page.append(CRLF);
                    // build html body
                    page.append("<!DOCTYPE HTML PUBLIC &quot-//W3C//DTD HTML 4.01 Transitional//EN&quot &quothttp://www.w3.org/TR/html4/loose.dtd&quot>");
                    page.append(CRLF);
                    page.append("<HTML><HEAD><TITLE>Events</TITLE></HEAD> <BODY><table border='1'>");
                    page.append(CRLF);
                    page.append("<tr><th> id </th><th> name </th><th> date </th><th> location </th><th> organization </th><th> attendence </th></tr>");
                    page.append(CRLF);
                    
                    ArrayList<Event> events = db.get(new Event());
                    for (Event event : events) {
                    	page.append(event.toHtmlString());
                    	page.append(CRLF);
            		}                    
 
                    page.append("</table></BODY></HTML>");
                    page.append(CRLF);
                    os.writeBytes(page.toString());
                    
                } else {
                	FileInputStream fis = null;
                    boolean fileExists = true;
                    try {
                    	fis = new FileInputStream(file.getPath());
                    } catch (FileNotFoundException e) {
                        fileExists = false;
                    }
                    
                    if (fileExists)	{
                    	System.out.println(requestUrl);
                        sendBytes(fis, os);
                        fis.close();
                        os.writeBytes(CRLF);
                        
	                } else {
	                	System.out.println("sending not found message");
	                	
	                	statusLine = "HTTP/1.0 404 Not Found";
	                    contentTypeLine = "Content-type:  text/html" + CRLF;
	                    String fileMissing = "<HTML>" + 
	                            "<HEAD><TITLE>Not Found</TITLE></HEAD>" +
	                            "<BODY>The requested file was not found on the server.</BODY></HTML>";	          
	                    // Send the status line.
	                    os.writeBytes(statusLine);
	                    // Send the content type line.
	                    os.writeBytes(contentTypeLine);
	                    // Send a blank line to indicate the end of the header lines.
	                    os.writeBytes(CRLF);      
	                    os.writeBytes(fileMissing);
	                    os.writeBytes(CRLF);
	                    
	                }
                }
        		
        	} else {
        		System.out.println("Request method not recognized");
        	}
            
        } else {
        	System.out.println("Headers missing");
        }
           
    }
    
  
    private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception {
       // Construct a 1K buffer to hold bytes on their way to the socket.
       byte[] buffer = new byte[1024];
       int bytes = 0;

       // Copy requested file into the socket's output stream.
       while((bytes = fis.read(buffer)) != -1 ) {
          os.write(buffer, 0, bytes);
       }
    }

    private static String contentType(String fileName)
    {
            if(fileName == "/" || fileName.endsWith(".htm") || fileName.endsWith(".html")) {
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