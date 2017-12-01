package eu.europa.ec.cipa.admin.web.dto;

import java.io.Serializable;
import java.util.Set;

import eu.europa.ec.cipa.etrustex.domain.CIALevel;
import eu.europa.ec.cipa.etrustex.domain.Document;
import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.domain.Role;

public class TransactionForm implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String namespace;
	private String requestLocalName;
	private String responseLocalName;
	private Role senderRole;
	private Role receiverRole;
	private Document document;
	private String version;
	private CIALevel ciaLevel;
	private Set<Profile> profiles;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getRequestLocalName() {
		return requestLocalName;
	}
	public void setRequestLocalName(String requestLocalName) {
		this.requestLocalName = requestLocalName;
	}
	public String getResponseLocalName() {
		return responseLocalName;
	}
	public void setResponseLocalName(String responseLocalName) {
		this.responseLocalName = responseLocalName;
	}
	public Role getSenderRole() {
		return senderRole;
	}
	public void setSenderRole(Role senderRole) {
		this.senderRole = senderRole;
	}
	public Role getReceiverRole() {
		return receiverRole;
	}
	public void setReceiverRole(Role receiverRole) {
		this.receiverRole = receiverRole;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public CIALevel getCiaLevel() {
		return ciaLevel;
	}
	public void setCiaLevel(CIALevel ciaLevel) {
		this.ciaLevel = ciaLevel;
	}
	public Set<Profile> getProfiles() {
		return profiles;
	}
	public void setProfiles(Set<Profile> profiles) {
		this.profiles = profiles;
	}
}
