package settings;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;

import com.sun.org.apache.xerces.internal.dom.ChildNode;

import database.DbHandler;

import util.Logger;
import util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import settings.Constants.AppType;
import settings.Constants.DbType;
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
    
//	public static HashMap<String, HashMap<String, String>> getNodeMap(String[] elemTree) {
//		ArrayList<Node> nodes = (ArrayList<Node>) getNodes(elemTree);
//		HashMap<String, HashMap<String, String>> hm = new HashMap<String, HashMap<String, String>>();
//		
//		for (Node node : nodes) {
//			NamedNodeMap attribs = node.getAttributes();
//			HashMap<String, String> im = new HashMap<String, String>();
//			for (int i = 0; i < attribs.getLength(); i++) {
//				im.put(attribs.item(i).getNodeName(), attribs.item(i).getNodeValue());
//				im.put("data", node.getTextContent());
//			}
//			hm.put(attribs.getNamedItem("name").getNodeValue(), im);
//		}
//		return hm;
//	}
	
	public static String[] getNodeText(String qualifier) {
		List<Node> temp = getNodes(qualifier);
		String[] result = new String[temp.size()];
		for (int i = 0; i < temp.size(); i++) {
			result[i] = temp.get(i).getTextContent();
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

	public static List<Pair<String, String>> getPreparedStatements () {
		NodeList statements = settings.getDocumentElement().getElementsByTagName("prepared-statement");
		LinkedList<Pair<String, String>> statementList = new LinkedList<Pair<String, String>>();
		for (int i = 0; i < statements.getLength(); i++) {
			Node statement = statements.item(i);
			String name = statement.getAttributes().getNamedItem("name").getNodeValue();
			String table = statement.getAttributes().getNamedItem("table").getNodeValue();
			String value = statement.getTextContent();
			statementList.add(new Pair<String, String>(name + ":" + table, value) );
		}
		return statementList;
	}
	
	public static String getScraperSetting (String taskName, String settingName) {
		NodeList scraperList = settings.getDocumentElement().getElementsByTagName("scraper-task");
		for (int i = 0; i < scraperList.getLength(); i++) {
			Node node = scraperList.item(i);
			if (node.hasAttributes()) {
				if (node.getNodeName().equals(taskName)) {
					if (node.getAttributes().getNamedItem("name").getNodeValue().equals(settingName)) {
						return node.getTextContent();
					}
				}				
			}
		}
		return null;
	}
	
	public static Iterator<String> getTables() {
		LinkedList<String> tables = new LinkedList<String>();
		NodeList tableList = settings.getDocumentElement().getElementsByTagName("table");
		for (int tCount = 0; tCount < tableList.getLength(); tCount++) {
			Node tableNode = tableList.item(tCount);
			tables.add(tableNode.getAttributes().getNamedItem("name").getNodeValue());
		}
		return tables.iterator();
	}
	
	public static Iterator<String> getTableDefs() {
		List<String> tableDef = new LinkedList<String>();
		
		NodeList tableList = settings.getDocumentElement().getElementsByTagName("table");
		for (int tCount = 0; tCount < tableList.getLength(); tCount++) {
			String tableName = null;
			String primaryKey = "";
			Node tableNode = tableList.item(tCount);
			tableName = tableNode.getAttributes().getNamedItem("name").getNodeValue();

			StringBuilder columnDef = new StringBuilder("(");
			NodeList tableChildren = tableNode.getChildNodes();				
			for (int cCount = 0; cCount < tableChildren.getLength(); cCount++) {				
				Node tableChild = tableChildren.item(cCount);
				
				if (tableChild.getNodeName().equals("table-definition")) {
					if (tableChild.getAttributes().getNamedItem("name").getNodeValue().equals("primarykey")) {
						primaryKey = "(" + tableChild.getTextContent() + ")";
					}
				} else if (tableChild.getNodeName().equals("column")) {
					String name = tableChild.getAttributes().getNamedItem("name").getNodeValue();
					String type = tableChild.getAttributes().getNamedItem("dbtype").getNodeValue();
					columnDef.append("'" + name + "'" + " " + Enum.valueOf(DbType.class, type.toUpperCase()));
					columnDef.append(", " ); 
				}				
			}	
						
			tableDef.add("CREATE TABLE IF NOT EXISTS " + tableName + " " + columnDef + " PRIMARY KEY " + primaryKey + ")");
		}
		
		return tableDef.iterator();
	}
}
//	private void buildSchema(Document doc) {
//		schema = new Schema();
//		NodeList tableList = (doc.getDocumentElement().getElementsByTagName("table"));				
//		for (int tCount = 0; tCount < tableList.getLength(); tCount++) {
//			Node tableNode = tableList.item(tCount);
//			String tableName = tableNode.getAttributes().getNamedItem("name").getNodeValue();
//			schema.addTable(tableName);
//			System.out.println("table: " + tableName);
//			
//			NodeList tableChildren = tableNode.getChildNodes();				
//			for (int cCount = 0; cCount < tableChildren.getLength(); cCount++) {				
//				Node tableChild = tableChildren.item(cCount);
//				
//				if (tableChild.hasAttributes()) {
//					String childName = tableChild.getAttributes().getNamedItem("name").getNodeValue();
//					
//					if (tableChild.getNodeName().equals("header")) {
//						if (childName.equals("child")) {
//							schema.getTable(tableName).children.add(tableChild.getTextContent());
//						} else if (childName.equals("source")) {
//							schema.getTable(tableName).sources.add(tableChild.getTextContent());
//						} else if (childName.equals("primarykey")) {
//							schema.getTable(tableName).primaryKey = (tableChild.getTextContent());
//						} else if (childName.equals("baseurl")) {
//							schema.getTable(tableName).baseUrl = (tableChild.getTextContent());
//						} else if (childName.equals("parent")) {
//							schema.getTable(tableName).parent = (tableChild.getTextContent());
//						} else if (childName.equals("inherit")) {
//							schema.getTable(tableName).inherit = (tableChild.getTextContent());
//						} else if (childName.equals("type")) {
//							schema.getTable(tableName).index = (tableChild.getTextContent().equals("index") ? true : false);	
//						} else {							
//							Logger.log("header not supported: " + childName, true);
//						}
//						
//						System.out.println("\theader " + childName + " : " + tableChild.getTextContent());
//					} else if (tableChild.getNodeName().equals("column")) {										
//						Column column = schema.getTable(tableName).addColumn(childName);	
//						System.out.println("\tcolumn: " + childName);
//						
//						
//						NodeList attrList = tableChild.getChildNodes();				
//						for (int aCount = 0; aCount < attrList.getLength(); aCount++) {
//							Node attrNode = attrList.item(aCount);
//							if (attrNode.hasAttributes()) {
//								String attrName = attrNode.getAttributes().getNamedItem("name").getNodeValue();
//								
//								if (attrName.equals("dbname")) {
//									column.dbname = attrNode.getTextContent();
//								} else if (attrName.equals("dbtype")) {
//									column.dbtype = attrNode.getTextContent();
//								} else if (attrName.equals("constraint")) {
//									column.constraint = attrNode.getTextContent();
//								} else if (attrName.equals("apptype")) {
//									column.apptype = Enum.valueOf(AppType.class, attrNode.getTextContent().toUpperCase());
//								} else if (attrName.equals("regex")) {
//									column.regex = attrNode.getTextContent();							
//								} else {							
//									Logger.log("Attribute not supported: " + attrName, true);
//								}
//							
//												
//								System.out.println("\t\tattribute: " + attrName + " value: " + attrNode.getTextContent());
//							}
//						}
//					}
//				}
//			}			
//		}
//	}
	
//	public static Table getTable(String table) {
//		return Settings.getSchema().getTable(table);
//	}
//	
//	public static Schema getSchema() {
//		return schema;
//	}
	






