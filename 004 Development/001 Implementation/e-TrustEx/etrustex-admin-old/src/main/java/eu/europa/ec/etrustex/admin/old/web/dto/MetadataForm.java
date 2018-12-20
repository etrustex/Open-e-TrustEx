package eu.europa.ec.etrustex.admin.old.web.dto;

import eu.europa.ec.etrustex.domain.*;
import eu.europa.ec.etrustex.types.MetaDataItemType;

import java.io.Serializable;

public class MetadataForm implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private MetaDataItemType metaDataItemType;
    private String value;
    private Transaction tansaction;
    private Document document;
    private Profile profile;
    private InterchangeAgreement interchangeAgreement;
    private Party party;

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

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }
}
