package scraper;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.w3c.dom.Document;

import settings.Constants.AppType;
import util.Logger;

public class DocumentScraper {
	ScraperSetting ss;
	public DocumentScraper(File settings) {
		//inflate settings
		try {
			ss = new ObjectMapper().readValue(settings, ScraperSetting.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static Document getCleanDOM(String page) throws ParserConfigurationException {
		CleanerProperties props = new CleanerProperties();
		props.setTranslateSpecialEntities(true);
		props.setTransResCharsToNCR(true);
		props.setOmitComments(true);	
		TagNode tagNode = (new HtmlCleaner(props).clean(page));
		return (new DomSerializer(new CleanerProperties()).createDOM(tagNode));
	}
	
	public HashMap<String, Object[]> scrape (Document doc) {	
		return  resolveFields(ss.fields.values(), doc);	
	}
	
	public HashMap<String, Object[]> scrape (String page) {			
		Document doc;		
		try {
			//clean and parse the html
			doc = getCleanDOM(page);
		} catch (ParserConfigurationException e) {
			Logger.log("ParserConfigurationException " + e.getMessage(), true);
			return null;
		}				
		return  resolveFields(ss.fields.values(), doc);		
	}
	
	//Finds all nodes for each field and passes them to have them converted to objects to be put in a hashmap.
	private static HashMap<String, Object[]> resolveFields (Collection<ScrapeField> fields, Document doc) {
		HashMap<String, Object[]> results = new HashMap<String, Object[]>();
		
		for (ScrapeField field : fields) {
			try {				
				// use XPath to get node text content from all nodes in field
	            String[] nodesText = util.XML.evaluate(field.xPath, doc);
				 
				if (nodesText == null || nodesText.length < 1) {
					if (field.required) {
						throw new ScrapeException("required field missing or wrong path supplied: " + field.name);
					} else {
						results.put(field.name, null);
					}
				} else {
					//resolve node text to objects
					Object[] nodes = resolveField(nodesText, field);
					results.put(field.name, nodes);
				}
			} catch (ScrapeException se) {
				Logger.log("Exception scraping field, field not parsed: " + field, true);
				Logger.log(se.getMessage(), true);		
			} catch (XPathExpressionException e) {
				Logger.log("xPath exception, field not parsed: " + field, true);
				Logger.log(e.getMessage(), true);
			}							
		}
		return results;
	}
	
	//extract the text from each node in the current field and converts it to an object.
	private static Object[] resolveField(String[] nodesText, ScrapeField field) throws ScrapeException {
		Object[] nodesObject = new Object[nodesText.length];
		for (int i = 0; i < nodesText.length; i++) {
			String temp = Scraper.findNamedGroups(nodesText[i], field.regex);			
			
			if ((temp == null || temp.isEmpty()) && field.notEmpty) {
				throw new ScrapeException("this field does not allow empty nodes. Node index: " + i, field);
			} 
			
			try {
				nodesObject[i] = stringToObject(temp, Enum.valueOf(AppType.class, field.type));
			} catch (ParseException e) {
				throw new ScrapeException("Error parsing String: " + temp + " . Node index: " + i, e);
			}	
		}
		return nodesObject;
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