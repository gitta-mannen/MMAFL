package scraper;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.w3c.dom.Document;

import settings.Constants.AppType;
import util.Logger;

public abstract class HtmlScraper {
	
	private HtmlScraper() {}
	
	private static TagNode getNodes(String page) {
		CleanerProperties props = new CleanerProperties();
		props.setTranslateSpecialEntities(true);
		props.setTransResCharsToNCR(true);
		props.setOmitComments(true);	
		return (new HtmlCleaner(props).clean(page));
	}
	
	public static HashMap<String, Object[]> scrape (String page, File settings) {	
		//inflate settings
		ScraperSetting ss;
		try {
			ss = new ObjectMapper().readValue(settings, ScraperSetting.class);
		} catch (Exception e) {
			Logger.log("Exception getting settings, no data was parsed.", true);
			Logger.log(e.getMessage(), true);
			return null;
		}				
		//get nodes from the html page
		TagNode tagNode = getNodes(page);		
		//scrape the nodes and put the result in the hashmap
		HashMap<String, Object[]> results = resolveFields(ss.fields.entrySet(), tagNode);
		
		return results;		
	}
	
	private static HashMap<String, Object[]> resolveFields (Set<Entry<String, ScrapeField>> fields, TagNode tagNode) {
		HashMap<String, Object[]> results = new HashMap<String, Object[]>();
		for (Entry<String, ScrapeField> field : fields) {
			//get the nodes for the current field
			try {
				Object[] nodes = tagNode.evaluateXPath(field.getValue().xPath);
				if (nodes.length < 1 && field.getValue().required) {
					throw new ScrapeException("required field missing or wrong path supplied: " + field.getKey(), field.getValue());
				}				
				nodes = resolveField(nodes, field);
				results.put(field.getKey(), nodes);
			} catch (ScrapeException se) {
				Logger.log("Exception scraping field, field not parsed: " + field.getKey(), true);
				Logger.log(se.getMessage(), true);		
			} catch (XPatherException e) {
				Logger.log("xPath exception, field not parsed: " + field.getKey(), true);
				Logger.log(e.getMessage(), true);
			}							
		}
		return results;
	}
	
	private static Object[] resolveField(Object[] nodes, Entry<String, ScrapeField> field) throws ScrapeException {
		for (int i = 0; i < nodes.length; i++) {
			if (!(nodes[i] instanceof StringBuilder)) {
				throw new ScrapeException("node is of wrong type. Node index: " + i, field.getValue());
			}
			String temp = nodes[i].toString();
			temp = Scraper.findNamedGroups(temp, field.getValue().regex);
			temp = temp == null ? "" : temp;
			
			if (temp.isEmpty() && field.getValue().notEmpty) {
				throw new ScrapeException("this field does not allow empty nodes. Node index: " + i, field.getValue());
			} 
			try {
				nodes[i] = stringToObject(temp, Enum.valueOf(AppType.class, field.getValue().type));
			} catch (ParseException e) {
				throw new ScrapeException("Error parsing String: " + temp + " . Node index: " + i, e);
			}	
		}
		return nodes;
	}
	
	private static Object stringToObject(String s, AppType type) throws ParseException {		
		if (s.isEmpty()) {
			Logger.log("Empty string supplied", false);
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
        	return ms.print(p);
		case LONG: case DOUBLE: case INTEGER:
			return (NumberFormat.getInstance(Locale.US).parseObject(s));
		case STRING:
			return StringEscapeUtils.unescapeHtml4(s);
		default:
			throw new IllegalArgumentException("App type not recognized.");
		}
	}
	
}