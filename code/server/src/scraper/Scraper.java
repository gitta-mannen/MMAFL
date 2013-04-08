package scraper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import settings.Settings;
import settings.Constants.AppType;
import util.Logger;

abstract class Scraper {
	protected final String name;
	protected final AppType[] types;
	protected final String[] regexes;
	
	public Scraper(String name) {
		this.name = name;
		regexes = Settings.getNodeText("scrapers:" + name + ":scrape-field:regex");
		String[] temp = Settings.getNodeText("scrapers:" + name + ":scrape-field:apptype");
//		types = new AppType[temp.length];
		types = Arrays.copyOf(temp, temp.length, AppType[].class);
		
//		for (int i = 0; i < temp.length; i++) {
//			types[i] = Enum.valueOf(AppType.class, temp[i].toUpperCase());
//		}
		
		if (regexes.length != types.length) {
			throw new IndexOutOfBoundsException("Each field has to have a type.");
		}	
	}		
	
	public String getName() {
		return name;
	}
	
	public Object[][] scrape (String text) {
		try {
			String[] processed = preProcess(text);
			Object[][] results = new Object[processed.length][];
			
			for (int i = 0; i < processed.length; i++){
				results[i] = stringToObject(parseFirst(processed[i], regexes), types);
			}
			return results;
		} catch (Exception e) {
			Logger.log(e.getMessage(),true);
		}
		 return null;
	}

	protected abstract String[] preProcess(String text) throws Exception;
	protected abstract Object stringToObject (String s, AppType type) throws ParseException, Exception;
	protected abstract Object[] stringToObject(String[] strings, AppType[] types) throws ParseException, Exception;
	
	/**
	 * Takes a list of regexes and returns the first matching results in the supplied string. 
	 * @param text
	 * @param regexes
	 * @return
	 * @throws Exception 
	 */
	public static String[] parseFirst(String text, String[] regexes) throws Exception {
		String[] results = new String[regexes.length];				
		for (int i = 0; i < regexes.length; i++) {
			results[i] = findNamedGroups(text, regexes[i]);					
		}		
		return results;
	}	
	
	/**
	 * Finds the first match for a given regex in the given string.
	 * Assumes the regex has three groups and extracts the middle one.
	 * @param text
	 * @param regex
	 * @param group
	 * @return
	 */
	@Deprecated
	public static String findFirst(String text, String regex) {
		Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL).matcher(text);				
		if (matcher.find() && matcher.groupCount() == 3) {
			return matcher.group(2);
		} else {
			Logger.log("No match found for regex: " + regex + " group count: " + matcher.groupCount(), false);
			return "";
		}
	}
	
	// Returns the connotation of all the matched named groups in the given text for the supplied regex.
	public static String findNamedGroups(String text, String regex) throws ScrapeException {
		Pattern p = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
		Matcher matcher = p.matcher(text);
		StringBuilder sb = new StringBuilder();
		Set<String> namedGroups = null;
		try {
			namedGroups = getNamedGroups(p);
		} catch (Exception e) {
			throw new ScrapeException("Error while getting named groups", e);
		}

		if (namedGroups.equals(null) || namedGroups.isEmpty()) {
			throw new ScrapeException("No named group in regex");
		}		
		
		if (matcher.find() && matcher.groupCount() > 0) {
			for (String namedGroup : namedGroups) {
				sb.append(matcher.group(namedGroup));				
			}
			
			return sb.toString();
		} else {
			Logger.log("No match found for regex: " + regex + " group count: " + matcher.groupCount(), false);
			return null;
		}
	}
	
	/**
	 * Gets the named groups of a given pattern.
	 * The method accomplishes this by using reflection to access a
	 * 	 private method of the Pattern class.  
	 * @param regex
	 * @return Set of the names of the named groups in the given Pattern.
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	  private static Set<String> getNamedGroups(Pattern regex)
	      throws NoSuchMethodException, SecurityException,
	             IllegalAccessException, IllegalArgumentException,
	             InvocationTargetException {

	    Method namedGroupsMethod = Pattern.class.getDeclaredMethod("namedGroups");
	    namedGroupsMethod.setAccessible(true);

	    Map<String, Integer> namedGroups = null;
	    namedGroups = (Map<String, Integer>) namedGroupsMethod.invoke(regex);

	    if (namedGroups == null) {
	    	throw new InternalError();
	    }

	    return Collections.unmodifiableMap(namedGroups).keySet();
	  }

}
