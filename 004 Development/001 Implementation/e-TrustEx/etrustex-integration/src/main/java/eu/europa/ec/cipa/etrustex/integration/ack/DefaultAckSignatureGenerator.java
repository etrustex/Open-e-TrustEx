package eu.europa.ec.cipa.etrustex.integration.ack;

import org.springframework.stereotype.Component;

@Component("defaultAckSignatureGenerator")
public class DefaultAckSignatureGenerator implements IAckSignatureGenerator {

	@Override
	public String signMessage(String message) {
		return "";
	}

}
