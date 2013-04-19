package scraper;

import java.util.Map;

import javax.xml.xpath.XPathExpressionException;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class ScraperSetting {	 
	public final String name;
	public final boolean tableFormat;
	public final Map<String, ScrapeField> fields;
	 
	 @JsonCreator
	public ScraperSetting(	@JsonProperty("name") String name,
							@JsonProperty("tableFormat") boolean tableFormat,
							@JsonProperty("field") Map<String, ScrapeField> fields)  {
		 this.name = name;
		 this.fields = fields;		 		 
		 this.tableFormat = tableFormat;
	 }
	 
}