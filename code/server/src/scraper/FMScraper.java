package scraper;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import settings.Settings;
import settings.Constants.AppType;
import util.Logger;

public class FMScraper extends Scraper {
	private final String delimiter;
	private final String extract;
	
	public FMScraper (String name) {
		super(name);
		
		delimiter = Settings.getNodeText("scrapers:" + name + ":pre-process:index-delimiter")[0];
		extract = Settings.getNodeText("scrapers:" + name + ":pre-process:extract")[0];
	}
	
	@Override
	protected String[] preProcess(String text) {
		if (!extract.equals("")) {
			text = Scraper.findFirst(text, extract);
		} 
		
		if (!delimiter.equals("")) {
			return text.split(delimiter);
		}
		else {
			return new String[]{text};
		}
	}
		
	/**
	 * Parses a string and converts it to the appropriate Object.
	 * @param s - String to be converted.
	 * @param type - Enumerator indicating the type.
	 * @return - Object created by parsing, or null if type isn't recognized.
	 * @throws ParseException
	 */
	protected Object stringToObject (String s, AppType type) throws ParseException {
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

	protected Object[] stringToObject(String[] strings, AppType[] types) throws ParseException {		
		if (strings.length != types.length) { 
			throw new IndexOutOfBoundsException("strings length doesn't match types length."); 
		}
		
		Object[] results = new Object[strings.length];
		for (int i = 0; i < strings.length; i++) {
			results[i] = stringToObject(strings[i], types[i]);
		}
		return results;
	}
}