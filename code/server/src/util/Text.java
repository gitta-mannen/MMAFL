package util;

import settings.Constants.AppType;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Text {
	private Text(){
		throw new IllegalStateException( "Do not instantiate this class." );
	}
	
	/**
	 * Parses a string and converts it to the appropriate Object.
	 * @param s - String to be converted.
	 * @param type - Enumerator indicating the type.
	 * @return - Object created by parsing or null if type isn't recognized.
	 * @throws ParseException
	 */
	public static Object stringToObject (String s, AppType type) throws ParseException {
		 switch (type) {
         case DATE:
        	 return (new SimpleDateFormat("MMM. dd, yyyy").parse(s));
		case LONG: case DOUBLE: case INTEGER:
             return (NumberFormat.getInstance(Locale.US).parseObject(s));
		case STRING:
             return s;
		default:
        	 Logger.log("Type not recognized", true);
        	 return null;
		 }
	}
	
	/**
	 * Finds the first match for a given regex in the given string.
	 * Assumes the regex has three groups and extracts the middle one.
	 * @param text
	 * @param regex
	 * @param group
	 * @return
	 */
	public static String findFirst (String text, String regex) {
		Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE).matcher(text);
		matcher.find();
		return matcher.group(2);								
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
