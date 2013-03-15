package settings;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import util.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import settings.Constants.AppType;
/**
 * @author Stugatz
 * Schema singleton object holds the database schema. It's static and doesn't change during the process execution  .
 */
public class Settings {
	private static final String settingsFile = System.getProperty("user.dir") + "/settings.xml";
	private static final Settings instance;
	private static Schema schema;
	
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
			buildSchema(readSettingsFile());
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
		
	private void buildSchema(Document doc) {
		schema = new Schema();
		NodeList tableList = (doc.getDocumentElement().getElementsByTagName("table"));				
		for (int tCount = 0; tCount < tableList.getLength(); tCount++) {
			Node tableNode = tableList.item(tCount);
			String tableName = tableNode.getAttributes().getNamedItem("name").getNodeValue();
			schema.addTable(tableName);
			//System.out.println("table: " + tableName);
			
			NodeList columnList = tableNode.getChildNodes();				
			for (int cCount = 0; cCount < columnList.getLength(); cCount++) {				
				Node columnNode = columnList.item(cCount);
				if (columnNode.hasAttributes()) {
					String columnName = columnNode.getAttributes().getNamedItem("name").getNodeValue();
					Column column = schema.getTable(tableName).addColumn(columnName);
					//System.out.println("\tcolumn: " + columnName);
					
					NodeList attrList = columnNode.getChildNodes();				
					for (int aCount = 0; aCount < attrList.getLength(); aCount++) {
						Node attrNode = attrList.item(aCount);
						if (attrNode.hasAttributes()) {
							String attrName = attrNode.getAttributes().getNamedItem("name").getNodeValue();
							
							if (attrName.equals("dbname")) {
								column.dbname = attrNode.getTextContent();
							} else if (attrName.equals("dbtype")) {
								column.dbtype = attrNode.getTextContent();
							} else if (attrName.equals("constraint")) {
								column.constraint = attrNode.getTextContent();
							} else if (attrName.equals("apptype")) {
								column.apptype = Enum.valueOf(AppType.class, attrNode.getTextContent().toUpperCase());
							} else if (attrName.equals("regex")) {
								column.regex = attrNode.getTextContent();							
							} else {							
								Logger.log("Attribute not supported: " + attrName, true);
							}
											
							//System.out.println("\t\tattribute: " + attrName + " value: " + attrNode.getTextContent());
						}
					}
				}
			}			
		}
	}
	
	public static Table getTable(String table) {
		return Settings.getSchema().getTable(table);
	}
	
	public static Schema getSchema() {
		return schema;
	}
	
}





