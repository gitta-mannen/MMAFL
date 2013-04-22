package scraper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xpath.internal.domapi.XPathStylesheetDOM3Exception;

import settings.Constants.AppType;

public class DocumentScraper {
	private Map<String, Pattern> patterns; 
	private Map<String, XPathExpression> xPaths;
	private ScraperSetting ss;

	public DocumentScraper(ScraperSetting ss) throws ScrapeException {
		//compile xpath and regex patterns
		this.ss = ss;
		patterns = new HashMap<String, Pattern> (ss.fields.size());
		xPaths = new HashMap<String, XPathExpression> (ss.fields.size());
		XPathFactory xfactory = XPathFactory.newInstance();

		for (ScrapeField sf : ss.fields.values()) {
			patterns.put(sf.name, Pattern.compile(sf.regex, Pattern.MULTILINE | Pattern.DOTALL));
			try {
				xPaths.put(sf.name, xfactory.newXPath().compile(sf.xPath));
			} catch (XPathExpressionException e) {
				throw new ScrapeException(e, sf);
			}
		}
	}

	public LinkedHashMap<String, Object[]> scrape (Document doc) throws ScrapeException {			
		LinkedHashMap<String, Object[]> results =  resolveFields(ss.fields.values(), doc);		
		if (ss.tableFormat) {
			int length = 0;
			for (Map.Entry<String, Object[]> entry : results.entrySet()) {
				if (length == 0) {
					length = entry.getValue().length;
				} else {
					if (length != entry.getValue().length) {
						throw new ScrapeException("Scraper returned fields with different lengths.");
					}
				}
			}
		}
		return results;
	}

	//Finds all nodes for each field and passes them to have them converted to objects to be put in a hashmap.
	private LinkedHashMap<String, Object[]> resolveFields (Collection<ScrapeField> fields, Document doc) throws ScrapeException {
		LinkedHashMap<String, Object[]> results = new LinkedHashMap<String, Object[]>();

		for (ScrapeField field : fields) {

			// use XPath to get node text content from all nodes in field
			String[] nodesText;
			try {
				nodesText = scraper.DocumentScraper.evaluate(xPaths.get(field.name), doc);

			} catch (XPathExpressionException e) {
				throw new ScrapeException(e, field);
			}

			if (nodesText == null || nodesText.length < 1) {
				if (field.required) {
					throw new ScrapeException("required field missing or wrong path supplied", field);
				} else {
					results.put(field.name, null);
				}
			} else {
				//resolve node text to objects
				Object[] nodes = resolveField(nodesText, field);			
				//concat fields separated by comma
				if (field.concat == true) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < nodes.length; i++) {
						if (nodes[i] == null) {
							continue;
						}
						sb.append(nodes[i]);							
						sb.append(", ");
					}
					results.put(field.name, new Object[]{sb.toString().trim().replaceAll("[,]$", "")});
				} else {
					results.put(field.name, nodes);	
				}

			}

		}
		return results;
	}

	//extract the text from each node in the current field and converts it to an object.
	private Object[] resolveField(String[] textNodes, ScrapeField field) throws ScrapeException {
		Object[] serObjNodes = new Object[textNodes.length];
		for (int i = 0; i < textNodes.length; i++) {
			String temp;
			if (field.regex == null || field.regex.isEmpty()) {
				temp = textNodes[i];
			} else {
				temp = DocumentScraper.findNamedGroups(textNodes[i], patterns.get(field.name));				
			}

			if (temp == null || temp.isEmpty()) {
				if (field.notEmpty) {
				throw new ScrapeException("this field does not allow empty nodes.", field, textNodes);
				} else {
					serObjNodes[i] = null;
				}
			} else {
				try {
					serObjNodes[i] = stringToObject(temp, Enum.valueOf(AppType.class, field.type));
				} catch (ParseException e) {
					throw new ScrapeException(e, field, textNodes);
				}
			}
		}
		return serObjNodes;
	}

	private static Object stringToObject(String s, AppType type) throws ParseException {			
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

	// Returns the connotation of all the matched named groups in the given text for the supplied regex.
	public static String findNamedGroups(String text, Pattern p) {				
		Matcher matcher = p.matcher(text);
		StringBuilder sb = new StringBuilder();
		Set<String> namedGroups = getNamedGroups(p);

		if (namedGroups.equals(null) || namedGroups.isEmpty()) {
			throw new IllegalArgumentException("No named group in regex");
		}		

		while (matcher.find() && matcher.groupCount() > 0) {
			for (String namedGroup : namedGroups) {
				sb.append(matcher.group(namedGroup));				
			}
		}

		if (sb.length() == 0) {				
			return null;
		} else {				
			return sb.toString();
		}
	}

	/**
	 * Gets the named groups of a given pattern.
	 * The method accomplishes this by using reflection to access a
	 * 	 private method of the Pattern class.  
	 * @param regex
	 * @return Set of the names of the named groups in the given Pattern.
	 */
	@SuppressWarnings("unchecked")
	static Set<String> getNamedGroups(Pattern regex) {
		try {
			Method namedGroupsMethod = Pattern.class.getDeclaredMethod("namedGroups");
			namedGroupsMethod.setAccessible(true);

			Map<String, Integer> namedGroups = null;
			namedGroups = (Map<String, Integer>) namedGroupsMethod.invoke(regex);

			if (namedGroups == null) {
				throw new InternalError("named groups returned null");
			}

			return Collections.unmodifiableMap(namedGroups).keySet();

		} catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException
				| InvocationTargetException | SecurityException e) {
			throw new InternalError(e.getMessage());
		}			
	}

	public static String[] evaluate(XPathExpression expr, Document doc) throws XPathExpressionException {	
		String[] result;
		NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

		if (nodes.getLength() < 1) {
			return null;
		} else {
			result = new String[nodes.getLength()];
			for (int i = 0; i < nodes.getLength(); i++) {
				result[i] = nodes.item(i).getTextContent();
			}
			return result;
		}				
	}

}