package eu.europa.ec.etrustex.integration.util;

import org.springframework.stereotype.Component;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class ETrustExLSResourceResolver implements LSResourceResolver {


    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        return new ETrustExLSInput(publicId, systemId, baseURI);
    }
}
