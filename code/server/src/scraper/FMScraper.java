package scraper;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.*;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

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
//		System.out.println("string to parse: '" + s + "' has type: " + type);
		if (s.isEmpty()) {
			Logger.log("Empty string supplied", true);
			return null;
		}
		
		switch (type) {
		case DATE:
			Date date = new SimpleDateFormat("MMM. dd, yyyy", Locale.ENGLISH).parse(s);
			return new SimpleDateFormat("yyyy-MM-dd").format(date);
		 case TIME:
        	PeriodFormatter ms = new PeriodFormatterBuilder()
        	.minimumPrintedDigits(2)
            .printZeroAlways()            
        	.appendMinutes()
            .appendSeparator(":")
            .appendSeconds()
            .toFormatter();
        	Period p = ms.parsePeriod(s);
        	//use this if you want the time in seconds(long)
	        	//Duration d = p.toStandardDuration();  
	        	//return d.getStandardSeconds();
        	return ms.print(p);

		case LONG: case DOUBLE: case INTEGER:
			return (NumberFormat.getInstance(Locale.US).parseObject(s));
		case STRING:
			return s;
		case HTML:		
			// replace <br /> tags with ';' except for when followed by whitespace then delete tags
			String tagLess = s.replaceAll("(<br />)(?=\\S)", ";").replaceAll("(<[^>]+>)", "");
			return StringEscapeUtils.unescapeHtml4(tagLess);
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