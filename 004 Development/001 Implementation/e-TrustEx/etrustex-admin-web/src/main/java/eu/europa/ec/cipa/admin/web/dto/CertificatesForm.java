package eu.europa.ec.cipa.admin.web.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import eu.europa.ec.cipa.etrustex.domain.Certificate;
import eu.europa.ec.cipa.etrustex.domain.Party;

public class CertificatesForm implements Serializable {
	private Party party;
	private CertificateWrapper newCertificate;
	private List<CertificateWrapper> certificates = new ArrayList<>();
	private String resultMsg;
	
	public Party getParty() {
		return party;
	}
	
	public void setParty(Party party) {
		this.party = party;
	}

	public CertificateWrapper getNewCertificate() {
		return newCertificate;
	}

	public void setNewCertificate(CertificateWrapper newCertificate) {
		this.newCertificate = newCertificate;
	}

	public List<CertificateWrapper> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<CertificateWrapper> certificates) {
		this.certificates = certificates;
	}
	
	public String getResultMsg() {
		return resultMsg;
	}
	
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	
	public boolean contains(Certificate certificate) {
		if(CollectionUtils.isNotEmpty(certificates) && certificate != null && certificate.getId() != null) {
			for (CertificateWrapper certificateWrapper : certificates) {
				if(certificateWrapper.getCertificate() != null && certificateWrapper.getCertificate().getId().equals(certificate.getId())) {
					return true;
				}
			}
		}
		
		return  false;
	}
}
