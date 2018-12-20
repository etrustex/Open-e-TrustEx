/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.dto;

import eu.europa.ec.etrustex.dao.dto.PartyListItemDTO;
import eu.europa.ec.etrustex.domain.admin.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class SessionUserInformation implements UserDetails {

    private static final long serialVersionUID = 1L;
    List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
    private Long userId;
    private String username;
    private BusinessDomainElement businessDomain;
    private List<BusinessDomainElement> businessDomains;
    private UserRole role;
    private List<PartyListItemDTO> parties;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long id) {
        this.userId = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BusinessDomainElement getBusinessDomain() {
        return businessDomain;
    }

    public void setBusinessDomain(BusinessDomainElement businessDomain) {
        this.businessDomain = businessDomain;
    }

    public List<BusinessDomainElement> getBusinessDomains() {
        return businessDomains;
    }

    public void setBusinessDomains(List<BusinessDomainElement> businessDomains) {
        this.businessDomains = businessDomains;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<PartyListItemDTO> getParties() {
        return parties;
    }

    public void setParties(List<PartyListItemDTO> parties) {
        this.parties = parties;
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority("ROLE_" + this.getRole().getCode()));
        return auths;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
