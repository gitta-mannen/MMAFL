package settings;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;
import util.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stugatz
 * Schema singleton object holds the database schema. It's static and doesn't change during the process execution  .
 */
public class Settings {
	private static final String settingsFile = System.getProperty("user.dir") + "/settings.xml";
	private static final Settings instance;
	private static Document settings;
	
	static {	   
		instance = new Settings();	    
	}

	public static Settings getInstance() {
		return instance;
	}

	private Settings() {
		reloadSettings();
	}
	
	public void reloadSettings() {
		try {
			settings = readSettingsFile();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			Logger.log(e.getMessage(), true);
		}	
	}
	
	private Document readSettingsFile() throws ParserConfigurationException, FileNotFoundException, SAXException, IOException {
		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = dBuilder.parse (new FileInputStream(new File(settingsFile)));
		doc.normalize();
		return doc;
	}

	public static String[] getNodeText(String qualifier) {
		List<Node> temp = getNodes(qualifier);
		String[] result = new String[temp.size()];
		for (int i = 0; i < temp.size(); i++) {
			result[i] = temp.get(i).getTextContent();
		}
		
		if(result.length == 0) {
			Logger.log("Settings not found: " + qualifier, true);
		}
		
		return result;
	}
	
	// returns a list of nodes that match the end of the qualifier chain
	public static List<Node> getNodes(String qualifier) {
		String[] elemTree = qualifier.split(":");
		int depth = 0;
		final List<Node> l = new ArrayList<Node>();
		parse(settings, l, settings.getDocumentElement(), elemTree, depth);
		return l;
	}	
	
	// recursive method, not used immediately
	private static void parse(final Document doc, final List<Node> list, final Element e, String[] nodeTree, int depth) {
        final NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final Node n = children.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE && (n.getNodeName().equals(nodeTree[depth])) ) {    
                if (depth < (nodeTree.length - 1)) {
                	parse(doc, list, (Element) n, nodeTree, (depth + 1));
                } else if (depth == (nodeTree.length - 1)) {
                	list.add(n);
                }
            }
        }
    }

}





