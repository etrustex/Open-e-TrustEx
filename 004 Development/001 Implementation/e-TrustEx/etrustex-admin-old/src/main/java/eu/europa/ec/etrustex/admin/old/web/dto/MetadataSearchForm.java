package eu.europa.ec.etrustex.admin.old.web.dto;

import eu.europa.ec.etrustex.domain.*;
import eu.europa.ec.etrustex.types.MetaDataItemType;

import java.io.Serializable;

public class MetadataSearchForm implements Serializable {
    private static final long serialVersionUID = 1L;

    private MetaDataItemType metaDataItemType_search;
    private String value_search;
    private Transaction tansaction_search;
    private Document document_search;
    private Profile profile_search;
    private InterchangeAgreement interchangeAgreement_search;
    private Party party_search;
    private Boolean searchOnLoad;

    public MetaDataItemType getMetaDataItemType_search() {
        return metaDataItemType_search;
    }

    public void setMetaDataItemType_search(MetaDataItemType metaDataItemType_search) {
        this.metaDataItemType_search = metaDataItemType_search;
    }

    public String getValue_search() {
        return value_search;
    }

    public void setValue_search(String value_search) {
        this.value_search = value_search;
    }

    public Transaction getTansaction_search() {
        return tansaction_search;
    }

    public void setTansaction_search(Transaction tansaction_search) {
        this.tansaction_search = tansaction_search;
    }

    public Document getDocument_search() {
        return document_search;
    }

    public void setDocument_search(Document document_search) {
        this.document_search = document_search;
    }

    public Profile getProfile_search() {
        return profile_search;
    }

    public void setProfile_search(Profile profile_search) {
        this.profile_search = profile_search;
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
