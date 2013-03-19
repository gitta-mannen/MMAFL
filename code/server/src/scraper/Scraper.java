package scraper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.Logger;


abstract class Scraper {
	public Scraper() {}		

	/**
	 * Takes a list of regexes and returns the first matching results in the supplied string. 
	 * @param text
	 * @param regexes
	 * @return
	 */
	public static String[] scrapeFirst(String text, String[] regexes) {
		String[] results = new String[regexes.length];		
		
		for (int i = 0; i < regexes.length; i++) {
			results[i] = findFirst(text, regexes[i]);					
		}		
		return results;
	}	
	
	public static String textFromUrl (String url) throws MalformedURLException, IOException {
		return util.IO.streamToString( (new URL(url)).openConnection().getInputStream());
	}
	/**
	 * Finds the first match for a given regex in the given string.
	 * Assumes the regex has three groups and extracts the middle one.
	 * @param text
	 * @param regex
	 * @param group
	 * @return
	 */
	public static String findFirst(String text, String regex) {
		Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE).matcher(text);
		matcher.find();
		if (matcher.groupCount() == 3) {
			return matcher.group(2);
		} else {
			Logger.log("No match found", true);
			return "";
		}
	}
	/**
	 * Finds all matches for the given regex in the given string.
	 * Assumes the regex has three groups and extracts the middle one.
	 * @param text
	 * @param regex
	 * @param group
	 * @return
	 */
	public static LinkedList<String> findAll (String text, String regex) {
		LinkedList<String> list = new LinkedList<String>();
		Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE).matcher(text);
		while(matcher.find() && matcher.groupCount() == 3) {	
			list.add(matcher.group(2));
		}
		
		return list;
	}
	

}
