/**
 * 
 */
package eu.europa.ec.cipa.etrustex.integration.util;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author chiricr
 *
 */
public class XMLUtil {
	
	private static final String UTF8_BOM = "\uFEFF";
	
	/**
	 * Recursively removes all descendant nodes of the given type
	 * @param node
	 * @param nodeType
	 * @param name
	 */
    public static void removeRecursively(Node node, short nodeType) {
    	if (node.getNodeType() == nodeType) {
    		node.getParentNode().removeChild(node);
    	} else {
    		// check the children recursively
    		NodeList list = node.getChildNodes();
    		for (int i = 0; i < list.getLength(); i++) {
    			removeRecursively(list.item(i), nodeType);
    		}
    	}
    }
    
    public static Document loadXMLFromString(String xml) throws Exception {
    	xml = removeUTF8BOM(xml);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
    }
    
    public static String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }    
    
    public static Document sourceToDom(Source s) throws Exception {
    	DOMResult r = new DOMResult();
    	TransformerFactory transformerFactory = TransformerFactory.newInstance();
//    		transformerFactory.setAttribute(XMLConstants.DEFAULT_NS_PREFIX, "http://schemas.xmlsoap.org/soap/envelope/");
    	Transformer transformer = transformerFactory.newTransformer();
    	transformer.transform(s,r);
    	return (Document)r.getNode();
    }
    
    public static String nodeToString(Node node) throws TransformerException {
        StringWriter sw = new StringWriter();
        Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.transform(new DOMSource(node), new StreamResult(sw));
        return sw.toString();
    }
}
