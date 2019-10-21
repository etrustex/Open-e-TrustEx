package eu.europa.ec.etrustex.integration.util;

import java.io.InputStream;
import java.io.Reader;

import javax.xml.XMLConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import eu.europa.ec.etrustex.services.IXmlService;

public class ResourceToDatabaseResolver implements LSResourceResolver {

	IXmlService xs;

	private static final Logger logger = LoggerFactory
			.getLogger(ResourceToDatabaseResolver.class);

	public ResourceToDatabaseResolver(IXmlService xs) {
		this.xs = xs;
	}

	@Override
	public LSInput resolveResource(String type, String namespaceURI,
			String publicId, String systemId, String baseURI) {
		logger.trace(
				"request to resolve resource for type [{}], namespaceURI [{}], publicId [{}], systemId [{}], baseURI [{}]",
				type, namespaceURI, publicId, systemId, baseURI);
		LSInput result = null;
		if (type.equals(XMLConstants.W3C_XML_SCHEMA_NS_URI)
				&& systemId != null) {
			result = new DatabaseLSInput(systemId);
			logger.trace("resolved resource");
		} else {
			logger.debug("cannot resolve type [{}], ignoring resolve request", type);
		}
		return result;
	}

	private final class DatabaseLSInput implements LSInput {

		String key;
		private String content;

		public DatabaseLSInput(String systemId) {
			int lastSlashAt = systemId.lastIndexOf('/');
			int lastBackslashAt = systemId.lastIndexOf('\\');
			int pos = Math.max(lastSlashAt, lastBackslashAt);
			if (pos == -1) {
				key = systemId;
			} else {
				key = systemId.substring(pos + 1);
			}
			logger.trace("extracted key [{}] from systemId [{}]", key, systemId);
		}

		@Override
		public void setSystemId(String systemId) {
			logger.trace("setSystemId({})", systemId);
			// IGNORE
		}

		@Override
		public void setStringData(String stringData) {
			logger.trace("setStringData({})", stringData);
			// IGNORE
		}

		@Override
		public void setPublicId(String publicId) {
			logger.trace("setPublicId({})", publicId);
			// IGNORE
		}

		@Override
		public void setEncoding(String encoding) {
			logger.trace("setEncoding({})", encoding);
			// IGNORE
		}

		@Override
		public void setCharacterStream(Reader characterStream) {
			logger.trace("setCharacterStream({})",
					characterStream == null ? null : characterStream.getClass()
							.getName());
			// IGNORE
		}

		@Override
		public void setCertifiedText(boolean certifiedText) {
			logger.trace("setCertifiedText({})", certifiedText);
			// IGNORE
		}

		@Override
		public void setByteStream(InputStream byteStream) {
			logger.trace("setByteStream({})", byteStream == null ? null
					: byteStream.getClass().getName());
			// IGNORE
		}

		@Override
		public void setBaseURI(String baseURI) {
			logger.trace("setBaseURI({})", baseURI);
			// IGNORE
		}

		@Override
		public String getSystemId() {
			logger.trace("getSystemId()");
			return null;
		}

		@Override
		public String getStringData() {
			logger.trace("getStringData()");
			if (content == null) {
				content = xs.retrieveXmlDocument(key);
			}
			logger.trace("getStringData() length [{}]", content == null ? null : content.length());
			return content;
		}

		@Override
		public String getPublicId() {
			logger.trace("getPublicId()");
			return null;
		}

		@Override
		public String getEncoding() {
			logger.trace("getEncoding()");
			return null;
		}

		@Override
		public Reader getCharacterStream() {
			logger.trace("getCharacterStream()");
			return null;
		}

		@Override
		public boolean getCertifiedText() {
			logger.trace("getCertifiedText()");
			return false;
		}

		@Override
		public InputStream getByteStream() {
			logger.trace("getByteStream()");
			return null;
		}

		@Override
		public String getBaseURI() {
			logger.trace("getBaseURI()");
			return null;
		}
	}

}
