package eu.europa.ec.etrustex.admin.old.web.dto;

import eu.europa.ec.etrustex.dao.dto.PartyListItemDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InterchangeAgreementMultiForm extends InterchangeAgreementForm implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long secondPartyRoleId;

    private List<PartyListItemDTO> partyListItems = new ArrayList<>();

    @Override
    public PartyForm getSecondParty() {
        throw new NoSuchFieldError("InterchangeAgreementMultiForm does not implement secondParty");
    }

    @Override
    public void setSecondParty(PartyForm secondParty) {
        throw new NoSuchFieldError("InterchangeAgreementMultiForm does not implement secondParty");
    }

    public Long getSecondPartyRoleId() {
        return secondPartyRoleId;
    }

    public void setSecondPartyRoleId(Long roleId) {
        this.secondPartyRoleId = roleId;
    }

    public List<PartyListItemDTO> getPartyListItems() {
        return partyListItems;
    }

    public void setPartyListItems(List<PartyListItemDTO> partyListItems) {
        this.partyListItems = partyListItems;
    }
}
