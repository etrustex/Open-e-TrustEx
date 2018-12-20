package eu.europa.ec.etrustex.domain.routing;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;

@Entity
@Table(name = "ETR_TB_ENDPOINT")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Endpoint {

	private static final String JMS_ENDPOINT_TYPE = "JMS";
	private static final String WS_ENDPOINT_TYPE = "WS";
	private static final String AMQP_ENDPOINT_TYPE = "AMQP";

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
		
	@Column(name ="EDP_ACTIVE_FLAG", nullable = false)
	private Boolean isActive;
	
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Endpoint endpoint = (Endpoint) o;

        return id != null ? id.equals(endpoint.id) : endpoint.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public String getType() {
		if (this instanceof JMSEndpoint) {
			return JMS_ENDPOINT_TYPE;
		} else if (this instanceof WSEndpoint) {
			return WS_ENDPOINT_TYPE;
		} else if (this instanceof AMQPEndpoint) {
			return AMQP_ENDPOINT_TYPE;
		} else {
			return null;
		}
	}
}
