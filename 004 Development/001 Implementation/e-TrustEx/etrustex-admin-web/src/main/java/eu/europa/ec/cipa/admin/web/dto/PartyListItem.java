/**
 * 
 */
package eu.europa.ec.cipa.admin.web.dto;

import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author batrian Represents a party which is supposed to act as a second party
 *         in the multiple interchange agreement creation
 * 
 */
public class PartyListItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long businessDomainId;
	private Boolean exists;
	private String partyName;
	private String partyCredentials;
	private List<String> partyIdentifiers; // format "type value"
	private List<String> thirdParties;
	private String message;
	private String statusColor;//green = success, red = error
	private Boolean isValid;

	public PartyListItem() {
	}
	
	public PartyListItem(Party party, List<Party> thirdParties) {
		super();
		
		this.id = party.getId();
		this.exists = false;
		this.partyName = party.getName();
		if (party.getCredentials() != null){
			this.partyCredentials = party.getCredentials().getUser();
		}
		this.partyIdentifiers = new ArrayList<String>();
		this.thirdParties = new ArrayList<String>();
		
		if (CollectionUtils.isNotEmpty(party.getIdentifiers())){
			for (PartyIdentifier partyIdentifier : party.getIdentifiers()) {
				this.partyIdentifiers.add(partyIdentifier.getSchemeId() + " " + partyIdentifier.getValue());
			}
		}
		
		
		if (CollectionUtils.isNotEmpty(thirdParties)){
			for (Party thirdParty : thirdParties) {
				this.thirdParties.add(thirdParty.getName());
			}
		}
			
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getExists() {
		return exists;
	}

	public void setExists(Boolean exists) {
		this.exists = exists;
	}
	
	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public List<String> getPartyIdentifiers() {
		return partyIdentifiers;
	}

	public void setPartyIdentifiers(List<String> partyIdentifiers) {
		this.partyIdentifiers = partyIdentifiers;
	}

	public List<String> getThirdParties() {
		return thirdParties;
	}

	public void setThirdParties(List<String> thirdParties) {
		this.thirdParties = thirdParties;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatusColor() {
		return statusColor;
	}

	public void setStatusColor(String statusColor) {
		this.statusColor = statusColor;
	}

	public String getPartyCredentials() {
		return partyCredentials;
	}

	public void setPartyCredentials(String partyCredentials) {
		this.partyCredentials = partyCredentials;
	}

	public Long getBusinessDomainId() {
		return businessDomainId;
	}

	public void setBusinessDomainId(Long businessDomainId) {
		this.businessDomainId = businessDomainId;
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartyListItem that = (PartyListItem) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
