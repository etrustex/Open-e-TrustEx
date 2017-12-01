package eu.europa.ec.cipa.admin.web.dto;

import eu.europa.ec.cipa.etrustex.domain.Certificate;

import java.io.Serializable;

public class CertificateWrapper implements Serializable {
	private Certificate certificate;
	
	/*
	 * Used as a flag set during user's operations like "Ok" and "Cancel"
	 * Read when the party is saved
	 */
	private Boolean addToParty;
	
	public CertificateWrapper(Certificate certificate) {
		this.certificate = certificate;
		this.addToParty = false;
	}

    public Long getId() {
        return certificate.getId();
    }
	public Certificate getCertificate() {
		return certificate;
	}
	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}
	public Boolean getAddToParty() {
		return addToParty;
	}
	public void setAddToParty(Boolean add) {
		this.addToParty = add;
	}
}
