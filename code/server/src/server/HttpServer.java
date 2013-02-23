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
    final static File webRoot = new File("\\www\\");
    final static long timeOut = 500;
    private InputStreamReader is;
	private DataOutputStream os;
    
    // Receives socket from server thread
    public HttpRequest(Socket socket) {
        this.socket = socket;        
    }

    @Override
    final public void run() {
    	 System.out.println("*** Server accepted new connection on port: " + socket.getPort() +
                 " thread id: " + Thread.currentThread().getId() +  " ," + Thread.activeCount() + " active threads" + " ***");
    
    	long startTime;
    	String requestLine;
    	HashMap request;
    	boolean entered;
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
			int i = 0;
			int o = 0;
			StringTokenizer fields;
			request = new HashMap();
			startTime = System.currentTimeMillis();
			
			while (true) {								
				System.out.println("	>> Outer: " + ++o);				
				entered = false;
		        while ((br.ready() && (requestLine = br.readLine()).length() != 0) ) {		        	
		        	System.out.println("	>> Inner: " + ++i);
		        	System.out.println(requestLine);
		        	fields = new StringTokenizer(requestLine, ": ");
		        	request.put(fields.nextToken(), fields.nextToken(""));		
		        	entered = true;
		        }
		        
		        if (startTime + timeOut < System.currentTimeMillis()) {
		        	System.out.println("	>>> timed out, leaving");	
		        	break;
		        } else if (entered) {
		        	// Data was read, call handle method and then reset timer
		        	startTime = System.currentTimeMillis();
		        	System.out.println("	>>>data read");
		        	processRequest(request);
		        	request.clear();
		        }
		          else {		        	  
		        	  System.out.println("	>>>Waiting for: " + (System.currentTimeMillis() - startTime));
		        	  Thread.sleep(100);
		        }
            		        
			}
			
			// Close DB connection, sockets and streams
			db.close();             
			br.close();
			socket.close();
			System.out.println("Closing thread: " + Thread.currentThread().getId());	    
			 
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
        StringBuilder entityBody = new StringBuilder();
       
        statusLine = "HTTP/1.0 200 OK";
        contentTypeLine = "Content-type: " + 
                contentType( request.get("Request URL").toString() ) + CRLF;     
        
        // build html body
        ArrayList<Event> events = db.get(new Event());
        for (Event event : events) {
        	entityBody.append(event.toHtmlString());
		}
				                         
        // Send the status line.
        os.writeBytes(statusLine);

        // Send the content type line.
        os.writeBytes(contentTypeLine);

        // Send a blank line to indicate the end of the header lines.
        os.writeBytes(CRLF);        

        os.writeBytes("<HTML> <HEAD> <TITLE> Events </TITLE> </HEAD> <BODY> <table border=''1''> ");
        os.writeBytes("<tr> <th> id </th> <th> name </th> <th> date </th>  <th> location </th> <th> organization </th> <th> attendence </th> </tr>");
        os.writeBytes(entityBody.toString());
        os.writeBytes("</table> </BODY> </HTML>");
        
        // Close socket.
        os.close();       
           
    }
    
    /* not used
    private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception {
       // Construct a 1K buffer to hold bytes on their way to the socket.
       byte[] buffer = new byte[1024];
       int bytes = 0;

       // Copy requested file into the socket's output stream.
       while((bytes = fis.read(buffer)) != -1 ) {
          os.write(buffer, 0, bytes);
       }
    }
    */
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