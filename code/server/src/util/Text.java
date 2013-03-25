package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class Text {
	private Text(){
		throw new IllegalStateException( "Do not instantiate this class, use the static methods." );
	}
	
	/**
	 * Finds all matches for pattern in text. Returns the groups specified in the groups array.
	 * @param text - String to be searched for pattern.
	 * @param regex - Grouped pattern to be used.
	 * @param groups - Array of pattern groups to be used in result.
	 * @param appendBetween - String to be appended between pattern matches.
	 * @return String containing the specified groups from all pattern matches with provided 
	 * 	string appended between matches.
	 */
	public static String findAll(String text, String regex, int[] groups, String appendBetween) {		
		Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE).matcher(text);
		StringBuilder sb = new StringBuilder();

		boolean hasMatch = false;
		while (hasMatch = matcher.find()) {			
			System.out.println("foundMatch: " + hasMatch);
			for (int i = 0; i < groups.length; i++) {
				if (matcher.groupCount() < groups[i]) {
					System.out.println("Matched groups don't include: " + i);
				} else if (matcher.group(groups[i]) == null) {
					Logger.log("No match found for group " + i, true);
				} else {
					sb.append(matcher.group(groups[i]));
				}					
			}
			sb.append(appendBetween);
		}
		System.out.println(hasMatch);
		return sb.toString();
	}
}
