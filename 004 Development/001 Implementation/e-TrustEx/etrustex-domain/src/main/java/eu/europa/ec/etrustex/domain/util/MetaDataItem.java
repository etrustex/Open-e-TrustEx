package eu.europa.ec.etrustex.domain.util;

import eu.europa.ec.etrustex.domain.*;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.types.MetaDataItemType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="ETR_TB_METADATA" )
public class MetaDataItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="MD_ID", nullable = false)
	@GeneratedValue
	private Long id;

	@Embedded
	private EntityAccessInfo accessInfo;

	@Column(name = "MD_TYPE")
	private String rawItemType;
	
	@Column(name = "MD_VALUE")
	@Lob
	private String value;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "PID_TRA_ID",nullable=true)
	private Transaction tansaction;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "PID_DOC_ID",nullable=true)
	private Document document;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "PID_PRO_ID",nullable=true)
	private Profile profile;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "PID_ICA_ID",nullable=true)
	private InterchangeAgreement interchangeAgreement;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "MD_SENDER_ID",nullable=true)
	private Party sender;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "MD_BUSINESSDOMAIN_ID",nullable=true)
	private BusinessDomain businessDomain;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}

	public MetaDataItemType getItemType(){;
		return MetaDataItemType.resolve(getRawItemType());
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public InterchangeAgreement getInterchangeAgreement() {
		return interchangeAgreement;
	}

	public void setInterchangeAgreement(InterchangeAgreement interchangeAgreement) {
		this.interchangeAgreement = interchangeAgreement;
	}
	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public String getRawItemType() {
		return rawItemType;
	}

	public void setRawItemType(String rawItemType) {
		this.rawItemType = rawItemType;
	}

	public Party getSender() {
		return sender;
	}

	public void setSender(Party sender) {
		this.sender = sender;
	}

	public BusinessDomain getBusinessDomain() {
		return businessDomain;
	}

	public void setBusinessDomain(BusinessDomain businessDomain) {
		this.businessDomain = businessDomain;
	}
	
}
