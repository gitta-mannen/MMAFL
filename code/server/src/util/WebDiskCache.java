package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

//@todo
// add the missing cache options
// the getPage method should take an URI that includes host and path. It should also take (long) maxAge.
//flags override each other if set simultaneously so could just as well be an Enum.
public class WebDiskCache {	
	public static final int NEVER_EXPIRES = 1; //always fetch from cache if available, otherwise from web
	public static final int BYPASS_CACHE = 2; // only fetch from web, don't cache
	public static final int ALWAYS_EXPIRES = 4; //only fetch from disk web, cache to disk
	public static final int OFFLINE = 8; //only fetch from disk
	public static final int ONLINE = 16; //only fetch stale pages (> maxAge) from web, otherwise from disk
	private final URI host;
	private final File fileRoot;
	private final String defualtExtension = ".htm";
	private final int flags;
		
	public WebDiskCache (URI host, URI fileRoot) throws MalformedURLException {
		this(host, fileRoot, NEVER_EXPIRES);		
	}
	
	public WebDiskCache (URI host, URI fileRoot, int flags) throws MalformedURLException{
		this.host = host;
		this.fileRoot = new File(fileRoot);
		this.flags = flags;
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private void setFlag(int flag){
	//	flags |= flag;
	}
	
	public boolean checkFlag(int flag) {
		return (flags & flag) == flag;
	}	
	
	public String getPage(URI webPath) throws URISyntaxException, IOException   {
		URI filePath = new URI(host.getHost() + addExtension(webPath).getPath() );
		File file = new File(fileRoot.toURI().resolve(filePath.getPath()));
		
		boolean exists = file.exists();		
		if(!exists) {
			file.getParentFile().mkdirs();
		}
		
		if (checkFlag(NEVER_EXPIRES)) {
			if (exists) {
				return fileToString(file);
			} else {
				return cachePage(webPath, file);
			}			
		} else if (checkFlag(ALWAYS_EXPIRES)) {
			return cachePage(webPath, file);
		} else {
			throw new IllegalArgumentException("no flag set");
		}			
	}
	
	private URI addExtension(URI webPath) throws URISyntaxException {
		if(hasExtension(webPath.getPath())) {
			return webPath;
		} else {
			return new URI(webPath.getPath() + defualtExtension);
		}
	}

	private String cachePage(URI webPath, File file) throws IOException, URISyntaxException {		
		URL url = new URI(host.getScheme(), host.getHost(), host.resolve(webPath).getPath(), null).toURL();
		Logger.log("Getting page from web: " + url, true);
		String content = streamToString(url.openConnection().getInputStream());
		stringTofile(content, file, false);
		return content;
	}
	
	public static void stringTofile (String s, File file, boolean append) throws IOException {
		 BufferedWriter out = new BufferedWriter(new FileWriter(file, append));
	        out.write(s);
	        out.close();
	}
	
	// Checks whether a file path has a file type extension
	public static boolean hasExtension(String filePath) {
		return filePath.matches(".+[.][^.\\W]+");		
	}
	
	public static String streamToString(java.io.InputStream is) {
	    @SuppressWarnings("resource")
		java.util.Scanner s = new java.util.Scanner(is, "ISO-8859-1").useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	public static String fileToString(File file) throws FileNotFoundException {	
//		Logger.log("Getting page from disk cache: " + file.getPath(), true);
	    @SuppressWarnings("resource")
		java.util.Scanner s = new java.util.Scanner(file, "ISO-8859-1").useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	public static void writeStreamToFile(InputStream is, File file) throws Exception {     
		OutputStream out;	
			out = new FileOutputStream(file);
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = is.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}	
			out.flush();
			out.close();			
			is.close();			
	}
}
