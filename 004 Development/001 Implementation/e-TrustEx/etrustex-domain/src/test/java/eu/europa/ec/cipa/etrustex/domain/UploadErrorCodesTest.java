package eu.europa.ec.cipa.etrustex.domain;

import java.io.StringReader;
import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.s9api.Axis;
import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmSequenceIterator;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.tree.iter.AxisIterator;

import org.junit.Before;
import org.junit.Test;

import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.domain.util.MessageResponseCode;

public class UploadErrorCodesTest {

	private EntityManagerFactory factory;

	@Before
	public void initTest() throws Exception {
		factory = Persistence.createEntityManagerFactory("etrustex-test");
	}

	@Test
	public void uploadCodesTest() throws Exception {
		EntityManager entityManager = factory.createEntityManager();
		entityManager.getTransaction().begin();
		final Configuration config = new Configuration();
		final StaticQueryContext sqc = config.newStaticQueryContext();

		final DynamicQueryContext dynamicContext = new DynamicQueryContext(
				config);
		dynamicContext.setContextItem(config.buildDocument(new StreamSource(
				this.getClass().getClassLoader()
						.getResourceAsStream("ErrorCodes.xml"))));

		// Item<Item> it = (Item<Item>)exp.evaluateSingle(dynamicContext);

		// System.out.println( it.getStringValue());

		XQueryExpression exp = sqc
				.compileQuery("//ValueSet");
		
		final SequenceIterator<NodeInfo> iter = exp.iterator(dynamicContext);
		
		 Processor proc = new Processor(false);
         XPathCompiler xpath = proc.newXPathCompiler();
         DocumentBuilder builder = proc.newDocumentBuilder();

         // Load the XML document.
         // Note that builder.build() can also take a File arg.
         //StringReader reader = new StringReader();
         XdmNode doc = builder.build(new StreamSource(
 				this.getClass().getClassLoader()
				.getResourceAsStream("ErrorCodes.xml")));

         // Select all <book> nodes.
         // XPath syntax: http://www.w3schools.com/xpath/xpath_syntax.asp
         XPathSelector selector = xpath.compile("//ValueSet").load();
         selector.setContextItem(doc);

         // Evaluate the expression.
         XdmValue children = selector.evaluate();
         EntityAccessInfo info = new EntityAccessInfo();
         info.setCreationDate(Calendar.getInstance().getTime());
         info.setCreationId("ORAZISA");
         info.setModificationDate(Calendar.getInstance().getTime());
         info.setModificationId("ORAZISA");
         
         for (XdmItem item : children) {
        	 MessageResponseCode respCd = new MessageResponseCode();
             // Each book node has a title and price.
             // Get the title and show it.
             XdmNode bookNode = (XdmNode) item;
             XdmNode titleNode = getChild(bookNode, "*:Code");
             System.out.println(titleNode);
             String code = titleNode.getStringValue();
             respCd.setResponseCodeKey(code);
             String value = getChild(bookNode, "Value").getStringValue();
             respCd.setAccessInfo(info);
             respCd.setResponseCodeValue(value);
             respCd.setDocument(null);
             respCd.setInterchangeAgreement(null);
             respCd.setTansaction(null);
             respCd.setProfile(null);
           //  entityManager.persist(respCd);
             System.out.println(String.format("%s (%s)", code, value));
         }

		entityManager.getTransaction().commit();
		entityManager.close();
		// final Properties props = new Properties();
		// props.setProperty(OutputKeys.METHOD, "xml");
		// props.setProperty(OutputKeys.INDENT, "yes");
		// exp.run(dynamicContext, new StreamResult(System.out), props);
		// BusinessErrorHandlingStrategy.valueOf("TEST");
	}
	
	public  XdmNode getChild(XdmNode parent, String childName) {
		 
        XdmSequenceIterator iter = parent.axisIterator(Axis.CHILD, new QName(childName));
 
        if (iter.hasNext()) {
            return (XdmNode)iter.next();
        } else {
            return null;
        }
    }
}
