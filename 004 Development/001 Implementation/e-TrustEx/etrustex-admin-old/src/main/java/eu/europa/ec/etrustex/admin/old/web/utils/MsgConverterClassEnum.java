/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.utils;

public enum MsgConverterClassEnum {
    BACKOFFICEMESSAGCONVERTER("eu.europa.ec.etrustex.integration.dispatcher.BackOfficeMessagConverter"),
    FRONTOFFICEMESSAGCONVERTER("eu.europa.ec.etrustex.integration.dispatcher.FrontOfficeMessagConverter"),
    SOAPOVERJMSMESSAGECONVERTER("eu.europa.ec.etrustex.integration.util.SoapOverJmsMessageConverter");

    private String name;

    MsgConverterClassEnum(String s) {
        this.name = s;
    }

    public String getName() {
        return this.name;
    }
}
