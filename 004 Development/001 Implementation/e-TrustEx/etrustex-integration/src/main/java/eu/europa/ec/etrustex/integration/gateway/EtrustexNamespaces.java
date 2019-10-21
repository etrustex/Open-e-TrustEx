package eu.europa.ec.etrustex.integration.gateway;

public enum EtrustexNamespaces {
	
	//UBL
	ECCBC_0_1_NS("ec:schema:xsd:CommonBasicComponents-0.1"),
	ECCBC_1_0_NS("ec:schema:xsd:CommonBasicComponents-1"),
	ECCAC_0_1_NS("ec:schema:xsd:CommonAggregateComponents-0.1"),
	ECCAC_1_0_NS("ec:schema:xsd:CommonAggregateComponents-1"),
	ECCAC_2_0_NS("ec:schema:xsd:CommonAggregateComponents-2"),
	CBC_2_0_NS("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"),
	CAC_2_0_NS("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"),
	EXT_2_0_NS("urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2"),
	
	//Common
	ENVELOPE_QNAME("http://schemas.xmlsoap.org/soap/envelope/"),
	
	//Maindocs
	MAINDOC_DOC_WRAPPER_REQUEST_1_0("ec:schema:xsd:DocumentWrapperRequest-1"),
	
	//wsdl NAMESPACES
	WSDL_DOC_WRAPPER_1_0 ("ec:services:wsdl:DocumentWrapper-1"),
	WSDL_DOC_WRAPPER_2_0 ("ec:services:wsdl:DocumentWrapper-2"),
	WSDL_DOC_1_0 ("ec:services:wsdl:Document-1"),
	
	//xsd NAMESPACES
	DOCUMENT_WRAPPER_XSD_NS_01("ec:schema:xsd:DocumentWrapper-1"),
	DOCUMENT_WRAPPER_REQUEST_XSD_NS_01("ec:schema:xsd:DocumentWrapperRequest-1");
	
	private String ns;
	
	EtrustexNamespaces(String ns) {
		this.ns = ns;
	}

	public String getNamespace() {
		return ns;
	}
}

