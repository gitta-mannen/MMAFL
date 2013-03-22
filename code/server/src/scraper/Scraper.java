package scraper;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import settings.Settings;
import settings.Constants.AppType;
import util.Logger;

abstract class Scraper {
	protected final String name;
	protected final AppType[] types;
	protected final String[] regexes;
//	protected final int groups = 3;
//	protected final int[] usedGroups = {2}; 
	
	public Scraper(String name) {
		this.name = name;
		regexes = Settings.getNodeText("scrapers:" + name + ":scrape-field:regex");
		String[] temp = Settings.getNodeText("scrapers:" + name + ":scrape-field:apptype");
		types = new AppType[temp.length];
		
		for (int i = 0; i < temp.length; i++) {
			types[i] = Enum.valueOf(AppType.class, temp[i].toUpperCase());
		}
		
		if (regexes.length != types.length) {
			throw new IndexOutOfBoundsException("Each field has to have a type.");
		}	
	}		
		
	Object[][] scrape (String text) throws ParseException {
		String[] processed = preProcess(text);
		Object[][] results = new Object[processed.length][];
		
		for (int i = 0; i < processed.length; i++){
			results[i] = stringToObject(parseFirst(processed[i], regexes), types);
		}
		
		return results; 
	}

	protected abstract String[] preProcess(String text);
	protected abstract Object stringToObject (String s, AppType type) throws ParseException;
	protected abstract Object[] stringToObject(String[] strings, AppType[] types) throws ParseException;
	
	/**
	 * Takes a list of regexes and returns the first matching results in the supplied string. 
	 * @param text
	 * @param regexes
	 * @return
	 */
	public static String[] parseFirst(String text, String[] regexes) {
		String[] results = new String[regexes.length];		
		
		for (int i = 0; i < regexes.length; i++) {
			results[i] = findFirst(text, regexes[i]);					
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
	public static String findFirst(String text, String regex) {
		Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE).matcher(text);
		
		if (matcher.find() && matcher.groupCount() == 3) {
			return matcher.group(2);
		} else {
			Logger.log("No match found for regex: " + regex + " group count: " + matcher.groupCount(), true);
			return "";
		}
	}

}
