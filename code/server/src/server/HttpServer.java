package server;

import java.io.* ;
import java.net.* ;
import java.util.* ;

import database.Event;
import database.StatsHandler;

/**
 * httpServer main class
 * @author Stugatz
 */
public class HttpServer {    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        int port = 999;
        ServerSocket socket = new ServerSocket(port);
        HttpRequest httpRequest;       
        
        while (true) {
            httpRequest = new HttpRequest( socket.accept() );
            Thread thread = new Thread( httpRequest );
            thread.start();
        }
        
    }

}

class HttpRequest implements Runnable{
	StatsHandler db;
    Socket socket;
    		final static String CRLF = "\r\n";
    		final static File webRoot = new File("E:\\code\\java\\httpServer\\www");
    
    public HttpRequest(Socket socket) {
        this.socket = socket;        
    }

    @Override
    final public void run() {
        try {
        	 System.out.println("-> run thread: " + Thread.currentThread().getId() + ", " + Thread.currentThread().getName());
        	 db = new StatsHandler();
             processRequest();
        } catch (Exception e) {
            System.out.println(e);
        }
        db.close();
    }
    
    private void processRequest() throws Exception
    {
        // Get a reference to the socket's input and output streams.
		InputStreamReader is = new InputStreamReader( socket.getInputStream() );
		DataOutputStream os = new DataOutputStream( socket.getOutputStream() );

		// Set up input stream filters.
		BufferedReader br = new BufferedReader (is);     
        
        // Get the request line of the HTTP request message.
        String requestLine = br.readLine();

        // Display the request line.
        System.out.println();
        System.out.println("*** Server accepted new connection on port: " + socket.getPort() +
                " thread id: " + Thread.currentThread().getId() +  " ," + Thread.activeCount() + " active threads" + " ***");
       
        System.out.println(requestLine);        
        
        // Get and display the header lines.
        String headerLine = null;
        while ((headerLine = br.readLine()).length() != 0) {
                System.out.println(headerLine);
        }
        
        // Extract the filename from the request line.
        StringTokenizer tokens = new StringTokenizer(requestLine);
        tokens.nextToken();  // skip over the method, which should be "GET"
        String fileName = tokens.nextToken();
        System.out.println("Client sent request for: " + fileName);
                        
        // Construct the response message.
        String statusLine = null;
        String contentTypeLine = null;
        StringBuilder entityBody = new StringBuilder();
       
        statusLine = "HTTP/1.0 200 OK";
        contentTypeLine = "Content-type: " + 
                contentType( fileName ) + CRLF;     
        
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
        
        // Close streams and socket.
        os.close();
        br.close();
        socket.close();    
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