package eu.europa.ec.cipa.etrustex.integration.ack;


public interface IAckSignatureGenerator {
	
	String signMessage(String message);

}
