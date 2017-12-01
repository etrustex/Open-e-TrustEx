package eu.europa.ec.cipa.etrustex.domain.routing;

import eu.europa.ec.cipa.etrustex.domain.*;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.types.EndpointMessageType;

import javax.persistence.*;

@Entity
@Table(name = "ETR_TB_ENDPOINT")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Endpoint {

	@Id
	@Column(name ="EDP_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
    @ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name="EDP_CRED_ID")
	private Credentials credentials;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "EDP_TRA_ID")
	private Transaction tansaction;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "EDP_BD_ID")
	private BusinessDomain businessDomain;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "EDP_PTY_ID")
	private Party party;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "EDP_ICA_ID")
	private InterchangeAgreement interchangeAgreement;
	
	@Column(name ="EDP_MSG_TYPE", nullable = false)
	@Enumerated(EnumType.STRING)
	private EndpointMessageType endointMessageType;
	
	@Column(name ="EDP_ACTIVE_FLAG", nullable = false)
	private Boolean isActive;
	
	@Column(name ="EDP_AUTH_FLAG", nullable = false)
	private Boolean isAuthenticationRequired;
	
	@Column(name ="EDP_USE_PROXY")
	private Boolean useProxy;
	@Column(name ="EDP_PROXY_HOST")
	private String proxyHost;
	@Column(name ="EDP_PROXY_PORT")
	private Integer proxyPort;

	@ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.DETACH})
	@JoinColumn(name="EDP_PROXY_CRED_ID")
	private Credentials proxyCredential;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "EDP_PRO_ID")
	private Profile profile;
	
	@Embedded
	private EntityAccessInfo accessInfo;
	
	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}
	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
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


	public Credentials getProxyCredential() {
		return proxyCredential;
	}

	public void setProxyCredential(Credentials proxyCredential) {
		this.proxyCredential = proxyCredential;
	}

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
	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	public BusinessDomain getBusinessDomain() {
		return businessDomain;
	}

	public void setBusinessDomain(BusinessDomain businessDomain) {
		this.businessDomain = businessDomain;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	
}
