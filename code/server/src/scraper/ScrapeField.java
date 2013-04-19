package scraper;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

class ScrapeField {
	public final int id;
	public final String name, regex, type, formatString;
	public final boolean required, notEmpty, concat;
	public final String xPath;
	
	@JsonCreator
	public ScrapeField(@JsonProperty("name") String name, @JsonProperty("regex") String regex, 
					   @JsonProperty("xPath") String xPath, @JsonProperty("type") String type,
					   @JsonProperty("formatString") String formatString,
					   @JsonProperty("id") int id,
					   @JsonProperty("required") boolean required, @JsonProperty("notEmpty") boolean notEmpty,
					   @JsonProperty("concat") boolean concat) {
		super();
		this.name = name;
		this.regex = regex;
		this.xPath = xPath;
		this.type = type;
		this.required = required;
		this.notEmpty = notEmpty;
		this.formatString = formatString;		
		this.id = id;
		this.concat = concat;		
	}		
}