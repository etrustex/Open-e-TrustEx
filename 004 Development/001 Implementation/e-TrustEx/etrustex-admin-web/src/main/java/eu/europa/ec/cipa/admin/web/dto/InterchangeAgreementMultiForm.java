package eu.europa.ec.cipa.admin.web.dto;

import java.io.Serializable;
import java.util.List;

public class InterchangeAgreementMultiForm extends InterchangeAgreementForm implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long secondPartyRoleId;
	
	private List<PartyListItem> partyListItems;
	
//	private MultiPartySelectionForm secondPartySelection;
//	
//	public MultiPartySelectionForm getSecondPartySelection() {
//		return secondPartySelection;
//	}
//
//	public void setSecondPartySelection(MultiPartySelectionForm secondPartySelection) {
//		this.secondPartySelection = secondPartySelection;
//	}
	
	@Override
	public void setSecondParty(PartyForm secondParty) {
		throw new NoSuchFieldError("InterchangeAgreementMultiForm does not implement secondParty");
	}
	
	@Override
	public PartyForm getSecondParty() {
		throw new NoSuchFieldError("InterchangeAgreementMultiForm does not implement secondParty");
	}

	public Long getSecondPartyRoleId() {
		return secondPartyRoleId;
	}

	public void setSecondPartyRoleId(Long roleId) {
		this.secondPartyRoleId = roleId;
	}

	public List<PartyListItem> getPartyListItems() {
		return partyListItems;
	}

	public void setPartyListItems(List<PartyListItem> partyListItems) {
		this.partyListItems = partyListItems;
	}
}
