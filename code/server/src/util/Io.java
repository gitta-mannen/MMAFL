package util;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Holds some simple IO functions.
 * Has a private constructor as it's not meant to be instantiated.
 * @author Stugatz
 */
public final class IO {
	private IO() {};
	
	/**
	 * Takes a stream and returns it as a string.
	 * @param is - input stream.
	 * @return - stream content as string.
	 */
	public static String streamToString(java.io.InputStream is) {
	    @SuppressWarnings("resource")
		java.util.Scanner s = new java.util.Scanner(is, "ISO-8859-1").useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	public static String fileToString(String uri) throws FileNotFoundException {
	    @SuppressWarnings("resource")
		java.util.Scanner s = new java.util.Scanner(new File(uri), "ISO-8859-1").useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
}
