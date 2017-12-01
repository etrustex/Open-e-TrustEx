package eu.europa.ec.cipa.admin.web.dto;

import eu.europa.ec.cipa.admin.web.utils.ConfigurationTypeEnum;
import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.types.EndpointMessageType;

import java.io.Serializable;

public class EndpointForm implements Serializable{
	private static final long serialVersionUID = 1L;
	private ConfigurationTypeEnum configurationType;
	private Long id;
	private Credentials credentials;
	private Transaction tansaction;
	private BusinessDomain businessDomain;
	private Party party;
	private InterchangeAgreement interchangeAgreement;
	private EndpointMessageType endointMessageType;
	private Boolean isActive;
	private Boolean isAuthenticationRequired;
	private Boolean useProxy;
	private String proxyHost;
	private Integer proxyPort;
	private Credentials proxyCredential = new ProxyCredentials();
	private Profile profile;
	
	// JMSEndpoint
	private String providerUrl;
	private String initalContextFactory;
	private String destinationJndiName;
	private String connectionFactoryJndiName;
	private String messageConverterClass;
	private Boolean isSupportingReplyTo;
	
	// WSEndpoint
	private String  wsEndpointURL;
	private Boolean signCall;
	private Boolean checkResponseSignature;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Credentials getCredentials() {
		return credentials;
	}
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	public Transaction getTansaction() {
		return tansaction;
	}
	public void setTansaction(Transaction tansaction) {
		this.tansaction = tansaction;
	}
	public BusinessDomain getBusinessDomain() {
		return businessDomain;
	}
	public void setBusinessDomain(BusinessDomain businessDomain) {
		this.businessDomain = businessDomain;
	}
	public Party getParty() {
		return party;
	}
	public void setParty(Party party) {
		this.party = party;
	}
	public InterchangeAgreement getInterchangeAgreement() {
		return interchangeAgreement;
	}
	public void setInterchangeAgreement(InterchangeAgreement interchangeAgreement) {
		this.interchangeAgreement = interchangeAgreement;
	}
	public EndpointMessageType getEndointMessageType() {
		return endointMessageType;
	}
	public void setEndointMessageType(EndpointMessageType endointMessageType) {
		this.endointMessageType = endointMessageType;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Boolean getIsAuthenticationRequired() {
		return isAuthenticationRequired;
	}
	public void setIsAuthenticationRequired(Boolean isAuthenticationRequired) {
		this.isAuthenticationRequired = isAuthenticationRequired;
	}
	public Boolean getUseProxy() {
		return useProxy;
	}
	public void setUseProxy(Boolean useProxy) {
		this.useProxy = useProxy;
	}
	public String getProxyHost() {
		return proxyHost;
	}
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}
	public Integer getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}
	public Credentials getProxyCredential() {
		return proxyCredential;
	}
	public void setProxyCredential(Credentials proxyCredential) {
		this.proxyCredential = proxyCredential;
	}
	public Profile getProfile() {
		return profile;
	}
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	public String getProviderUrl() {
		return providerUrl;
	}
	public void setProviderUrl(String providerUrl) {
		this.providerUrl = providerUrl;
	}
	public String getInitalContextFactory() {
		return initalContextFactory;
	}
	public void setInitalContextFactory(String initalContextFactory) {
		this.initalContextFactory = initalContextFactory;
	}
	public String getDestinationJndiName() {
		return destinationJndiName;
	}
	public void setDestinationJndiName(String destinationJndiName) {
		this.destinationJndiName = destinationJndiName;
	}
	public String getConnectionFactoryJndiName() {
		return connectionFactoryJndiName;
	}
	public void setConnectionFactoryJndiName(String connectionFactoryJndiName) {
		this.connectionFactoryJndiName = connectionFactoryJndiName;
	}
	public String getMessageConverterClass() {
		return messageConverterClass;
	}
	public void setMessageConverterClass(String messageConverterClass) {
		this.messageConverterClass = messageConverterClass;
	}
	public Boolean getIsSupportingReplyTo() {
		return isSupportingReplyTo;
	}
	public void setIsSupportingReplyTo(Boolean isSupportingReplyTo) {
		this.isSupportingReplyTo = isSupportingReplyTo;
	}
	public String getWsEndpointURL() {
		return wsEndpointURL;
	}
	public void setWsEndpointURL(String wsEndpointURL) {
		this.wsEndpointURL = wsEndpointURL;
	}
	public Boolean getSignCall() {
		return signCall;
	}
	public void setSignCall(Boolean signCall) {
		this.signCall = signCall;
	}
	public Boolean getCheckResponseSignature() {
		return checkResponseSignature;
	}
	public void setCheckResponseSignature(Boolean checkResponseSignature) {
		this.checkResponseSignature = checkResponseSignature;
	}
	public ConfigurationTypeEnum getConfigurationType() {
		return configurationType;
	}
	public void setConfigurationType(ConfigurationTypeEnum configurationType) {
		this.configurationType = configurationType;
	}
}