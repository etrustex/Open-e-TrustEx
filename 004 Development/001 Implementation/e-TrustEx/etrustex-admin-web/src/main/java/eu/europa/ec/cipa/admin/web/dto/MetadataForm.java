package eu.europa.ec.cipa.admin.web.dto;

import java.io.Serializable;

import eu.europa.ec.cipa.etrustex.domain.Document;
import eu.europa.ec.cipa.etrustex.domain.InterchangeAgreement;
import eu.europa.ec.cipa.etrustex.domain.Profile;
import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.types.MetaDataItemType;

public class MetadataForm implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private MetaDataItemType metaDataItemType;
	private String value;
	private Transaction tansaction;
	private Document document;
	private Profile profile;
	private InterchangeAgreement interchangeAgreement;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public MetaDataItemType getMetaDataItemType() {
		return metaDataItemType;
	}
	public void setMetaDataItemType(MetaDataItemType metaDataItemType) {
		this.metaDataItemType = metaDataItemType;
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
}
