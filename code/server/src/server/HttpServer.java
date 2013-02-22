package server;

import java.io.* ;
import java.net.* ;
import java.util.* ;

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
    Socket socket;
    		final static String CRLF = "\r\n";
    		final static File webRoot = new File("E:\\code\\java\\httpServer\\www");
    
    public HttpRequest(Socket socket) {
        this.socket = socket;
    }

    @Override
    final public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e);
        }
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
                " thread id: " + Thread.currentThread().getId() + " ***");
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

        // Prepend a "." so that file request is within the current directory.
                //fileName = "." + fileName;        
        File file = new File(webRoot, fileName);
        System.out.println("Client sent request for: " + file.getPath());
                
        // Open the requested file.
        FileInputStream fis = null;
        boolean fileExists = true;
        try {
                fis = new FileInputStream(file.getPath());
        } catch (FileNotFoundException e) {
                fileExists = false;
        }
        
        // Construct the response message.
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;
        if (fileExists) {
                statusLine = "HTTP/1.0 200 OK";
                contentTypeLine = "Content-type: " + 
                        contentType( fileName ) + CRLF;
        } else {
                statusLine = "HTTP/1.0 404 Not Found";
                contentTypeLine = "Content-type:  text/html" + CRLF;
                entityBody = "<HTML>" + 
                        "<HEAD><TITLE>Not Found</TITLE></HEAD>" +
                        "<BODY>The requested file was not found on the server.</BODY></HTML>";
        }
        
        // Send the status line.
        os.writeBytes(statusLine);

        // Send the content type line.
        os.writeBytes(contentTypeLine);

        // Send a blank line to indicate the end of the header lines.
        os.writeBytes(CRLF);        

        // Send the entity body.
        if (fileExists)	{
                sendBytes(fis, os);
                fis.close();
        } else {
                os.writeBytes(entityBody);
        }
        
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