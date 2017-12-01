package eu.europa.ec.cipa.admin.web.dto;

import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.PartyIdentifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PartyForm implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long roleId;
	private Long businessDomainId;

	// for view mode
	private String partyName;
	private String roleName;
	private String via3rdPartyName;
    private List<PartyIdentifier> identifiers;
	
	// for interchange agreement batch creation
	private List<String> delegatedParties;
	
	// for errors in multiple interchange agreement creation
	private String error;

	public PartyForm() {
	}
	
	public PartyForm(Party p) {
		super();
		
		if (p != null) {
			this.id = p.getId();
			this.partyName = p.getName();
			this.businessDomainId = p.getBusinessDomain().getId();
			this.identifiers = new ArrayList<>(p.getIdentifiers());
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getVia3rdPartyName() {
		return via3rdPartyName;
	}

	public void setVia3rdPartyName(String via3rdPartyName) {
		this.via3rdPartyName = via3rdPartyName;
	}

	public List<String> getDelegatedParties() {
		return delegatedParties;
	}

	public void setDelegatedParties(List<String> delegatedParties) {
		this.delegatedParties = delegatedParties;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Long getBusinessDomainId() {
		return businessDomainId;
	}

	public void setBusinessDomainId(Long businessDomainId) {
		this.businessDomainId = businessDomainId;
	}

    public List<PartyIdentifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<PartyIdentifier> identifiers) {
        this.identifiers = identifiers;
    }
}
