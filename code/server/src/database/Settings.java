package database;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


/**
 * @author Stugatz
 * Schema singleton object holds the database schema. It's static and doesn't change during the process execution  .
 */
public class Settings {
	private static final String settingsFile = System.getProperty("user.dir") + "/settings.xml";
	private static final Settings instance;
	private static HashMap<String[],String> schema = new HashMap<String[],String>();

	static {	   
		instance = new Settings();	    
	}

	public static Settings getInstance() {
		return instance;
	}

	private Settings() {
		try {
			File fXmlFile = new File(settingsFile);			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			buildSchema(doc);
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

		} catch (org.xml.sax.SAXParseException pe) {
			pe.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void buildSchema(Document doc) {
		NodeList nodeList = (doc.getDocumentElement().getElementsByTagName("table"));				
		traverse(nodeList, "", "");		
	}
	
	private void traverse(NodeList nodeList, String table, String column) {		
		String attribute, value;
		for (int count = 0; count < nodeList.getLength(); count++) {
			Node elementNode = nodeList.item(count);
			if (elementNode.hasAttributes()) {
				//System.out.println();
				//System.out.print(elementNode.getNodeName() + " type: " + elementNode.getNodeType());
				
				NamedNodeMap nodeMap = elementNode.getAttributes();
				for (int i = 0; i < nodeMap.getLength(); i++) {
					Node attrNode = nodeMap.item(i);
					//System.out.print(" " + attrNode.getNodeName() + " = ");
					//System.out.print(attrNode.getNodeValue());
					
					if (attrNode.getNodeName().equals("name")) {
						if (elementNode.getNodeName().equals("table")) {
							table = attrNode.getNodeValue();
						} else if (elementNode.getNodeName().equals("column")) {
							column = attrNode.getNodeValue();
						} else if (elementNode.getNodeName().equals("attr")) {
							attribute = attrNode.getNodeValue();
							value = elementNode.getTextContent();
							schema.put(new String[]{table, column, attribute}, value);
							Logger.log(table + " " + column + " " + attribute + " " + value, true);
						} else {
							Logger.log("parsed schema object not regognized", true);
						}
					}
				}				
			}

			if (elementNode.hasChildNodes()) {
				traverse(elementNode.getChildNodes(), table, column);
			}

			//System.out.println("<<" + tempNode.getNodeValue());
		}	
	}
	
	public HashMap<String[], String> getSchema() {
		return schema;
	}
	
	/*
	private void printNode(Node n) {	  
	    int type = n.getNodeType();

	    switch (type) {
	        case Node.ATTRIBUTE_NODE:
	        	System.out.print.print("ATTR:");
	            printlnCommon(n);
	            break;

	        case Node.CDATA_SECTION_NODE:
	            out.print("CDATA:");
	            printlnCommon(n);
	            break;

	        case Node.COMMENT_NODE:
	            out.print("COMM:");
	            printlnCommon(n);
	            break;

	        case Node.DOCUMENT_FRAGMENT_NODE:
	            out.print("DOC_FRAG:");
	            printlnCommon(n);
	            break;

	        case Node.DOCUMENT_NODE:
	            out.print("DOC:");
	            printlnCommon(n);
	            break;

	        case Node.DOCUMENT_TYPE_NODE:
	            out.print("DOC_TYPE:");
	            printlnCommon(n);
	            NamedNodeMap nodeMap = ((DocumentType)n).getEntities();
	            indent += 2;
	            for (int i = 0; i < nodeMap.getLength(); i++) {
	                Entity entity = (Entity)nodeMap.item(i);
	                echo(entity);
	            }
	            indent -= 2;
	            break;

	        case Node.ELEMENT_NODE:
	            out.print("ELEM:");
	            printlnCommon(n);

	            NamedNodeMap atts = n.getAttributes();
	            indent += 2;
	            for (int i = 0; i < atts.getLength(); i++) {
	                Node att = atts.item(i);
	                echo(att);
	            }
	            indent -= 2;
	            break;

	        case Node.ENTITY_NODE:
	            out.print("ENT:");
	            printlnCommon(n);
	            break;

	        case Node.ENTITY_REFERENCE_NODE:
	            out.print("ENT_REF:");
	            printlnCommon(n);
	            break;

	        case Node.NOTATION_NODE:
	            out.print("NOTATION:");
	            printlnCommon(n);
	            break;

	        case Node.PROCESSING_INSTRUCTION_NODE:
	            out.print("PROC_INST:");
	            printlnCommon(n);
	            break;

	        case Node.TEXT_NODE:
	            out.print("TEXT:");
	            printlnCommon(n);
	            break;

	        default:
	            out.print("UNSUPPORTED NODE: " + type);
	            printlnCommon(n);
	            break;
	    }

	    indent++;
	    for (Node child = n.getFirstChild(); child != null;
	         child = child.getNextSibling()) {
	        echo(child);
	    }
	    indent--;
	}	
	
	*/
}


class Tree {
	String type, value;
	
	private Tree sibling, child;
	
	public Tree() {
		super();
	}
	
	public Tree(Tree sibling, Tree child) {
		super();
		this.sibling = sibling;
		this.child = child;
	}

	public void setSibling(Tree sibling) {
		this.sibling = sibling;
	}

	public void setChild(Tree child) {
		this.child = child;
	}

	public Tree getSibling() {
		return sibling;
	}
	
	public Tree getChild() {
		return child;
	}
}

