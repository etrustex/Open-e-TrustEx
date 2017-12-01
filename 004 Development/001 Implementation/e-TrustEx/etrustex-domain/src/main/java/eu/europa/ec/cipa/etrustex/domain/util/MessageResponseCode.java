package eu.europa.ec.cipa.etrustex.domain.util;

import eu.europa.ec.cipa.etrustex.domain.Document;
import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.domain.Transaction;

import javax.persistence.*;

@Entity
@Table(name="ETR_TB_MSG_RESPONSE_CODE" )
public class MessageResponseCode {
	
	@Id
	@Column(name ="RSC_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Column(name ="RSC_CD", nullable = false)
	private String responseCodeKey;
	
	@Column(name ="RSC_VAL", nullable = false)
	private String responseCodeValue;
	
	@Column(name ="RSC_STS_CD", nullable = true)
	private String statusCode;
	
	

	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "RSC_TRA_ID",nullable=true)
	private Transaction tansaction;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "RSC_DOC_ID",nullable=true)
	private Document document;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "RSC_PRO_ID",nullable=true)
	private Profile profile;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "RSC_ICA_ID",nullable=true)
	private InterchangeAgreement interchangeAgreement;
	
	@Embedded
	private EntityAccessInfo accessInfo;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResponseCodeKey() {
		return responseCodeKey;
	}

	public void setResponseCodeKey(String responseCodeKey) {
		this.responseCodeKey = responseCodeKey;
	}

	public String getResponseCodeValue() {
		return responseCodeValue;
	}

	public void setResponseCodeValue(String responseCodeValue) {
		this.responseCodeValue = responseCodeValue;
	}

	public Transaction getTansaction() {
		return tansaction;
	}

	public void setTansaction(Transaction tansaction) {
		this.tansaction = tansaction;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public InterchangeAgreement getInterchangeAgreement() {
		return interchangeAgreement;
	}

	public void setInterchangeAgreement(InterchangeAgreement interchangeAgreement) {
		this.interchangeAgreement = interchangeAgreement;
	}

	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}
	
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

}
