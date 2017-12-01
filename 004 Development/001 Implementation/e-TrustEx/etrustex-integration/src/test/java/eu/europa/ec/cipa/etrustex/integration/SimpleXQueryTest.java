package eu.europa.ec.cipa.etrustex.integration;

import static org.junit.Assert.fail;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.trans.XPathException;

import org.junit.Assert;
import org.junit.Test;

public class SimpleXQueryTest {

	@Test
	// TODO: revisit this class
	public void testAuthtorisationHeaderQuery() {
		final Configuration config = new Configuration();
		final StaticQueryContext sqc = config.newStaticQueryContext();
		// final XQueryExpression exp =
		// sqc.compileQuery("//*:DocumentBundle/*:DocumentWrapperReference");
		final DynamicQueryContext dynamicContext = new DynamicQueryContext(config);

		CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();
		System.out.println(asciiEncoder.canEncode("UC108_SC642_MS12@@@:#####"));
		// we check for special characters
		if (!asciiEncoder.canEncode("UC108_SC642_MS12@@@:#####")) {
			System.out.println("Non ASCCI String");
		}

		try {
			dynamicContext.setContextItem(config.buildDocument(new StreamSource(this.getClass().getClassLoader()
					.getResourceAsStream("xquerytest.xml"))));

			// Item<Item> it = (Item<Item>)exp.evaluateSingle(dynamicContext);

			// System.out.println( it.getStringValue());

			XQueryExpression exp;

			exp = sqc.compileQuery("//*:DocumentBundle/*:DocumentWrapperReference/*:ID");
			final SequenceIterator iter = exp.iterator(dynamicContext);
			while (true) {
				NodeInfo item = (NodeInfo) iter.next();
				if (item != null) {
					System.out.println(item.getStringValue());
				} else
					break;
			}

			try {
				exp = sqc.compileQuery("//*:XYZ");
				Assert.assertTrue(exp.evaluate(dynamicContext).isEmpty());
			} catch (XPathException e) {
				fail("exception thrown");
			}

		} catch (XPathException e) {
			fail("exception thrown");
		}
		// We make sure the wrong X-pathcondition has thrown an exception

		// final Properties props = new Properties();
		// props.setProperty(OutputKeys.METHOD, "xml");
		// props.setProperty(OutputKeys.INDENT, "yes");
		// exp.run(dynamicContext, new StreamResult(System.out), props);
		// BusinessErrorHandlingStrategy.valueOf("TEST");
	}
}
