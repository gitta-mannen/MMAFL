package scraper;

import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;

public interface Task {
	void chainSameSource(Task task);
	
	void chainLinkedSource(Task task);
	
	void doTask(Document doc, Map<String, Object>fKeys);
	
	void doTask(String uri, Map<String, Object>fKeys);
	
	List<String> getFkeyLables();
}
