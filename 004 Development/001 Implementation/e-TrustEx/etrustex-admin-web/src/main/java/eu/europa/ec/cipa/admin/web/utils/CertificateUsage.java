package eu.europa.ec.cipa.admin.web.utils;


public enum CertificateUsage {
	KEY_ENCIPHERMENT("Encryption"); // AUTHENTICATION("Authentication"), 

	private String name;
	
	CertificateUsage(String name) {
		this.name = name;
	} 

	public String getName() {
        return this.name;
    }
	
/*	public static String[] descValues() {
		String[] usages = new String[CertificateUsage.values().length];
    	
		int i = 0;
		for(CertificateUsage usage : CertificateUsage.values()) {
    		usages[i++] = usage.desc();
    	}
    	
    	return usages;
	}*/
}
