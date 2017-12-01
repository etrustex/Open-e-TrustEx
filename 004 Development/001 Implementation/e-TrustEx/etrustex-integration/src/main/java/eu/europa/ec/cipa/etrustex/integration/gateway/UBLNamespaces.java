package eu.europa.ec.cipa.etrustex.integration.gateway;

public enum UBLNamespaces {
	ECCBC_0_1_NS("ec:schema:xsd:CommonBasicComponents-0.1"),
	ECCBC_1_0_NS("ec:schema:xsd:CommonBasicComponents-1"),
	ECCAC_0_1_NS("ec:schema:xsd:CommonAggregateComponents-0.1"),
	ECCAC_1_0_NS("ec:schema:xsd:CommonAggregateComponents-1"),
	ECCAC_2_0_NS("ec:schema:xsd:CommonAggregateComponents-2"),
	CBC_2_0_NS("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"),
	CAC_2_0_NS("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"),
	EXT_2_0_NS("urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2"),
	
	//Maindocs
	MAINDOC_DOC_WRAPPER_REQUEST_1_0("ec:schema:xsd:DocumentWrapperRequest-1"),
	
	//wsdl NAMESPACES
	WSDL_DOC_WRAPPER_2_0 ("ec:services:wsdl:DocumentWrapper-2"); 
	
	
	private String ns;
	
	UBLNamespaces(String ns) {
		this.ns = ns;
	}

	public String getNamespace() {
		return ns;
	}
}

