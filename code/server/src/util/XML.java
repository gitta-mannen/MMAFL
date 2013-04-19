package util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class XML {

	private XML() {}

	public static Document getCleanDOM(String page) throws ParserConfigurationException {
		CleanerProperties props = new CleanerProperties();
		props.setTranslateSpecialEntities(true);
		props.setTransResCharsToNCR(true);
		props.setOmitComments(true);	
		TagNode tagNode = (new HtmlCleaner(props).clean(page));
		return (new DomSerializer(new CleanerProperties()).createDOM(tagNode));
	}
	
	public static String[] getPathText(String path, Document doc) throws XPathExpressionException {	
		String[] result;
		XPathFactory xfactory = XPathFactory.newInstance();
		XPath xpath = xfactory.newXPath();
		XPathExpression expr = xpath.compile(path);		
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
	
	public static Document parseXML (File file) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(file);		
	}
	
	public static Document parseXML (String s) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(s));
		return builder.parse(is);		
	}
	
}
