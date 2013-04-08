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

public class WebDiskCache {
	public static final int CACHE_MODE_DEBUG = 1;
	public static final int CACHE_MODE_OFF = 2;
	public static final int CACHE_MODE_INSERTONLY = 4;
	public static final int CACHE_MODE_OFFLINE = 8;
	private final URI host;
	private final File fileRoot;
	private final String defualtExtension = ".htm";
	private final int flags;
		
	public WebDiskCache (URI host, URI fileRoot) throws MalformedURLException {
		this(host, fileRoot, CACHE_MODE_DEBUG);		
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
	
	public String getPage(URI webPath) throws Exception {
		URI filePath = new URI(host.getHost() + addExtension(webPath).getPath() );
		File file = new File(fileRoot.toURI().resolve(filePath.getPath()));
		
		boolean exists = file.exists();		
		if(!exists) {
			file.getParentFile().mkdirs();
		}
		
		if (checkFlag(CACHE_MODE_DEBUG)) {
			if (exists) {
				return fileToString(file);
			} else {
				return cachePage(webPath, file);
			}			
		} else if (checkFlag(CACHE_MODE_INSERTONLY)) {
			return cachePage(webPath, file);
		} else {
			throw new Exception("flag not recognized");
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
