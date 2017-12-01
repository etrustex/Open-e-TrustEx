package eu.europa.ec.cipa.admin.web.dto;

import eu.europa.ec.cipa.etrustex.domain.CIALevel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class InterchangeAgreementForm implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private ProfileElement profile;
	private Date icaValidityStartDate;
	private PartyForm firstParty, secondParty, first3rdParty, second3rdParty;
	private CIALevel ciaLevels;
	private PartyView newParty;
	private List<SelectElement> firstPartyDelegates;
	private List<SelectElement> secondPartyDelegates;

	public InterchangeAgreementForm() {
	}

	public Date getIcaValidityStartDate() {
		return icaValidityStartDate;
	}

	public void setIcaValidityStartDate(Date validityStartDate) {
		this.icaValidityStartDate = validityStartDate;
	}

	public PartyForm getFirstParty() {
		return firstParty;
	}

	public void setFirstParty(PartyForm firstParty) {
		this.firstParty = firstParty;
	}

	public PartyForm getSecondParty() {
		return secondParty;
	}

	public void setSecondParty(PartyForm secondParty) {
		this.secondParty = secondParty;
	}

    public PartyForm getFirst3rdParty() {
        return first3rdParty;
    }

    public void setFirst3rdParty(PartyForm first3rdParty) {
        this.first3rdParty = first3rdParty;
    }

    public PartyForm getSecond3rdParty() {
        return second3rdParty;
    }

    public void setSecond3rdParty(PartyForm second3rdParty) {
        this.second3rdParty = second3rdParty;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CIALevel getCiaLevels() {
		return ciaLevels;
	}

	public void setCiaLevels(CIALevel ciaLevels) {
		this.ciaLevels = ciaLevels;
	}

	public PartyView getNewParty() {
		return newParty;
	}

	public void setNewParty(PartyView newParty) {
		this.newParty = newParty;
	}

	public ProfileElement getProfile() {
		return profile;
	}

	public void setProfile(ProfileElement profile) {
		this.profile = profile;
	}

	public List<SelectElement> getFirstPartyDelegates() {
		return firstPartyDelegates;
	}

	public void setFirstPartyDelegates(List<SelectElement> firstPartyDelegates) {
		this.firstPartyDelegates = firstPartyDelegates;
	}

	public List<SelectElement> getSecondPartyDelegates() {
		return secondPartyDelegates;
	}

	public void setSecondPartyDelegates(List<SelectElement> secondPartyDelegates) {
		this.secondPartyDelegates = secondPartyDelegates;
	}
}
