/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.dto;

import java.io.Serializable;

public class DocumentSearchForm implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name_search;
    private String localName_search;
    private String typeCode_search;
    private Boolean searchOnLoad;

    public String getName_search() {
        return name_search;
    }

    public void setName_search(String name) {
        this.name_search = name;
    }

    public String getLocalName_search() {
        return localName_search;
    }

    public void setLocalName_search(String localName) {
        this.localName_search = localName;
    }

    public String getTypeCode_search() {
        return typeCode_search;
    }

    public void setTypeCode_search(String documentTypeCode) {
        this.typeCode_search = documentTypeCode;
    }

    public Boolean getSearchOnLoad() {
        return searchOnLoad;
    }

    public void setSearchOnLoad(Boolean searchOnLoad) {
        this.searchOnLoad = searchOnLoad;
    }
}