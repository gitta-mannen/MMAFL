package scraper;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class HtmlParser {

	private HtmlParser() {}
	
	// th-header
	// tr-header
	// td-header
	// index/details
	
	public static void parse (String text) {
		Stack<String> elems = new Stack<String>();
		String regex = "(<[^!](\\w)[^<>]+>)([^<>][^<>])()";
		Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL).matcher(text);				
		
		while (matcher.find()) {
			String e = elems.pop();
			System.out.println(e);
		}
		
		while (!elems.isEmpty()) { 
			System.out.println(elems.pop());
		}
				
	}

}
