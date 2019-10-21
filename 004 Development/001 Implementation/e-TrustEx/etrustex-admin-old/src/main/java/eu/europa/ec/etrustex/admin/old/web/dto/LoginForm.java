/**
 *
 */
package eu.europa.ec.etrustex.admin.old.web.dto;

import eu.europa.ec.etrustex.domain.admin.User;

import java.io.Serializable;

/**
 * @author abatrinu
 */
public class LoginForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private User user;
    private Long businessDomainId;
    //	private BusinessDomainElement businessDomain;
    private String errorKey;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getBusinessDomainId() {
        return businessDomainId;
    }

    public void setBusinessDomainId(Long businessDomainId) {
        this.businessDomainId = businessDomainId;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }

//	public BusinessDomainElement getBusinessDomain() {
//		return businessDomain;
//	}
//
//	public void setBusinessDomain(BusinessDomainElement businessDomain) {
//		this.businessDomain = businessDomain;
//	}

}
