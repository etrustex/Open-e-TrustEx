/**
 * 
 */
package eu.europa.ec.cipa.admin.web.utils;

public enum MsgConverterClassEnum {
	BACKOFFICEMESSAGCONVERTER("eu.europa.ec.cipa.etrustex.integration.dispatcher.BackOfficeMessagConverter"),
	FRONTOFFICEMESSAGCONVERTER("eu.europa.ec.cipa.etrustex.integration.dispatcher.FrontOfficeMessagConverter"),
	SOAPOVERJMSMESSAGECONVERTER("eu.europa.ec.cipa.etrustex.integration.util.SoapOverJmsMessageConverter");

	private String name;

	MsgConverterClassEnum(String s) {
		this.name = s;
	}

	public String getName() {
		return this.name;
	}
}
