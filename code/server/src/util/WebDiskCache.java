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
import java.net.URL;

public class WebDiskCache {
	public static final int CACHE_MODE_DEBUG = 1;
	public static final int CACHE_MODE_OFF = 2;
	public static final int CACHE_MODE_INSERTONLY = 4;
	public static final int CACHE_MODE_OFFLINE = 8;
	private final URL domain;
	private final File fileRoot;
	private final String defualtExtension = ".htm";
	private final int flags;
		
	public WebDiskCache (String domain, String fileRoot) throws MalformedURLException {
		this(domain, fileRoot, CACHE_MODE_DEBUG);		
	}
	
	public WebDiskCache (String domain, String fileRoot, int flags) throws MalformedURLException{
		this.domain = new URL(domain);
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
	
	public String getPage(String webPath) throws Exception {
		String filePath = cleanPath(webPath);
		File file = new File(fileRoot + filePath);
				
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
	
	private String cleanPath(String webPath) {
		String filePath = webPath.replace("/", "\\");
		if(hasExtension(filePath)) {
			return filePath;
		} else if (filePath.endsWith("\\")) {
			filePath = filePath.replaceAll("\\\\+$", "");
		}		
		return filePath + defualtExtension; 
	}

	private String cachePage(String webPath, File file) throws IOException {		
		URL url = new URL(domain + webPath);
		Logger.log("Getting page from web", true);
		String content = streamToString(url.openConnection().getInputStream());
		stringTofile(content, file);
		return content;
	}
	
	public void stringTofile (String s, File file) throws IOException {
		 BufferedWriter out = new BufferedWriter(new FileWriter(file));
	        out.write(s);
	        out.close();
	}
	
	// Checks whether a file path has a file type extension
	public boolean hasExtension(String filePath) {
		return filePath.matches("\\.[^.\\\\/:*?\"<>|\\r\\n]+$");
	}
	
	public static String streamToString(java.io.InputStream is) {
	    @SuppressWarnings("resource")
		java.util.Scanner s = new java.util.Scanner(is, "ISO-8859-1").useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	public static String fileToString(File file) throws FileNotFoundException {	
		Logger.log("Getting page from disk cache", true);
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
