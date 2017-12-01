package eu.europa.ec.cipa.etrustex.integration.ack;

import java.io.IOException;

import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.integration.message.TrustExMessageHeader;
import freemarker.template.TemplateException;

public interface IAckGenerator {
	
	String generateAck(Transaction transaction, TrustExMessageHeader header) throws IOException, TemplateException;

}
