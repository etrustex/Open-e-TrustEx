package eu.europa.ec.cipa.admin.web.dto;

import java.io.Serializable;

import eu.europa.ec.cipa.admin.web.utils.ConfigurationTypeEnum;
import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.types.EndpointMessageType;

public class EndpointSearchForm implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private ConfigurationTypeEnum configurationType;
	private EndpointMessageType endointMessageType_search;
	private Boolean isActive_search;
	private String username;
	private String proxyUsername;
	private BusinessDomain businessDomain_search;
	private Profile profile_search;
	private Transaction tansaction_search;
	private InterchangeAgreement interchangeAgreement_search;
	private Party party_search;
	
	private Boolean searchOnLoad;

	public ConfigurationTypeEnum getConfigurationType() {
		return configurationType;
	}

	public void setConfigurationType(ConfigurationTypeEnum configurationType) {
		this.configurationType = configurationType;
	}

	public EndpointMessageType getEndointMessageType_search() {
		return endointMessageType_search;
	}

	public void setEndointMessageType_search(
			EndpointMessageType endointMessageType_search) {
		this.endointMessageType_search = endointMessageType_search;
	}

	public Boolean getIsActive_search() {
		return isActive_search;
	}

	public void setIsActive_search(Boolean isActive_search) {
		this.isActive_search = isActive_search;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProxyUsername() {
		return proxyUsername;
	}

	public void setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
	}

	public BusinessDomain getBusinessDomain_search() {
		return businessDomain_search;
	}

	public void setBusinessDomain_search(BusinessDomain businessDomain_search) {
		this.businessDomain_search = businessDomain_search;
	}

	public Profile getProfile_search() {
		return profile_search;
	}

	public void setProfile_search(Profile profile_search) {
		this.profile_search = profile_search;
	}

	public Transaction getTansaction_search() {
		return tansaction_search;
	}

	public void setTansaction_search(Transaction tansaction_search) {
		this.tansaction_search = tansaction_search;
	}

	public InterchangeAgreement getInterchangeAgreement_search() {
		return interchangeAgreement_search;
	}

	public void setInterchangeAgreement_search(
			InterchangeAgreement interchangeAgreement_search) {
		this.interchangeAgreement_search = interchangeAgreement_search;
	}

	public Party getParty_search() {
		return party_search;
	}

	public void setParty_search(Party party_search) {
		this.party_search = party_search;
	}

	public Boolean getSearchOnLoad() {
		return searchOnLoad;
	}

	public void setSearchOnLoad(Boolean searchOnLoad) {
		this.searchOnLoad = searchOnLoad;
	}
}