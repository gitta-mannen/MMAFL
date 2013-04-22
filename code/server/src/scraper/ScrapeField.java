package scraper;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

class ScrapeField {
	public final String name, regex, type;
	public final boolean required, notEmpty, concat;
	public final String xPath;
	
	@JsonCreator
	public ScrapeField(@JsonProperty("name") String name, @JsonProperty("regex") String regex, 
					   @JsonProperty("xPath") String xPath, @JsonProperty("type") String type,
					   @JsonProperty("required") boolean required, @JsonProperty("notEmpty") boolean notEmpty,
					   @JsonProperty("concat") boolean concat) {
		super();
		this.name = name;
		this.regex = regex;
		this.xPath = xPath;
		this.type = type;
		this.required = required;
		this.notEmpty = notEmpty;		
		this.concat = concat;		
	}		
}