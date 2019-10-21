package eu.europa.ec.etrustex.integration.ack;


public interface IAckSignatureGenerator {
	
	String signMessage(String message);

}
