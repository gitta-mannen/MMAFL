package scraper;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

class ScrapeField {
	public final String name, regex, type, formatString;
	public final boolean required, notEmpty;
	public XPathExpression xPath;
	private String xPathString;
	
	
	@JsonCreator
	public ScrapeField(@JsonProperty("name") String name, @JsonProperty("regex") String regex, 
					   @JsonProperty("xPath") String xPath, @JsonProperty("type") String type,
					   @JsonProperty("formatString") String formatString,
					   @JsonProperty("required") boolean required, @JsonProperty("notEmpty") boolean notEmpty) throws XPathExpressionException {
		super();
		this.name = name;
		this.regex = regex;
		this.xPathString = xPath;
		this.type = type;
		this.required = required;
		this.notEmpty = notEmpty;
		this.formatString = formatString;		
		
		XPathFactory xfactory = XPathFactory.newInstance();
		XPath xpath = xfactory.newXPath();
		this.xPath = xpath.compile(this.xPathString);	
	}		
}