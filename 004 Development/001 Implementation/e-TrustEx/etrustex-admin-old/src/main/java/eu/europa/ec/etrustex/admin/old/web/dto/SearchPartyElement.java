/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.dto;

import eu.europa.ec.etrustex.domain.Credentials;
import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.PartyIdentifier;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author batrian
 */
public class SearchPartyElement implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String domain;
    private Boolean isThirdParty;
    private Boolean naturalPersonFlag;
    private String username;
    private List<PartyIdentifierDTO> identifiers;

    public SearchPartyElement() {
    }

    public SearchPartyElement(Party p) {
        super();

        if (p != null) {
            this.id = p.getId();
            this.name = p.getName();
            if (p.getBusinessDomain() != null) {
                this.domain = p.getBusinessDomain().getName();
            }
            this.isThirdParty = p.getThirdPartyFlag();
            this.naturalPersonFlag = p.getNaturalPersonFlag();

            Credentials credentials = p.getCredentials();
            if (credentials != null) {
                this.username = p.getCredentials().getUser();
            } else {
                this.username = null;
            }

            if (CollectionUtils.isNotEmpty(p.getIdentifiers())) {
                this.identifiers = new ArrayList<PartyIdentifierDTO>();
                for (PartyIdentifier pi : p.getIdentifiers()) {
                    this.identifiers.add(new PartyIdentifierDTO(pi));
                }
            }

        }
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

    public Boolean getIsThirdParty() {
        return isThirdParty;
    }

    public void setIsThirdParty(Boolean isThirdParty) {
        this.isThirdParty = isThirdParty;
    }

    public Boolean getNaturalPersonFlag() {
        return naturalPersonFlag;
    }

    public void setNaturalPersonFlag(Boolean naturalPersonFlag) {
        this.naturalPersonFlag = naturalPersonFlag;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<PartyIdentifierDTO> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<PartyIdentifierDTO> identifiers) {
        this.identifiers = identifiers;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

}
