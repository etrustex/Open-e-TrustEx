/**
 * 
 */
package eu.europa.ec.etrustex.dao.dto;

import eu.europa.ec.etrustex.domain.PartyIdentifier;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author guerrpa
 *
 * To be used for lists of parties where not all properties need to be populated,
 * e.g. in UI select dropdowns
 */
public class PartyListItemDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
    private String name;
    private String credentials;
    private List<String> identifiers; // format "type value"
    private Collection<String> thirdParties;
    private String message;
    private Boolean isValid;

    public PartyListItemDTO() {
    }

    public PartyListItemDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public PartyListItemDTO(Long id, String name, Collection<PartyIdentifier> partyIdentifiers, Collection<String> thirdParties) {
        this.id = id;
        this.name = name;

        List<String> identifiersList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(partyIdentifiers)){
            for (PartyIdentifier partyIdentifier : partyIdentifiers) {
                identifiersList.add(partyIdentifier.getSchemeId() + " " + partyIdentifier.getValue());
            }

            Collections.sort(identifiersList);
        }
        this.identifiers = identifiersList;

        this.thirdParties = thirdParties;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<String> identifiers) {
        this.identifiers = identifiers;
    }

    public Collection<String> getThirdParties() {
        return thirdParties;
    }

    public void setThirdParties(Collection<String> thirdParties) {
        this.thirdParties = thirdParties;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartyListItemDTO dto = (PartyListItemDTO) o;

        return id != null ? id.equals(dto.id) : dto.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
