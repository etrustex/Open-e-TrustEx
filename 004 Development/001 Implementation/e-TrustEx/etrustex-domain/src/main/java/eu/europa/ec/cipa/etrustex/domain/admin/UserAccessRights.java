package eu.europa.ec.cipa.etrustex.domain.admin;

import eu.europa.ec.cipa.etrustex.domain.Party;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;

@Entity
@Table(name="CPA_TB_USER_RIGHTS")
public class UserAccessRights implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name ="CUR_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Embedded
	private EntityAccessInfo accessInfo;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="CUR_USR_ID", unique=false, nullable=false,updatable=true)
	private User user;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="CUR_BUD_ID", unique=false, nullable=true,updatable=true)
	private BusinessDomain businessDomain;
	
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="CUR_PTY_ID", unique=false, nullable=true,updatable=true)
	private Party party;
	
	@OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="CUR_URR_ID", unique=false, nullable=true,updatable=true)
	private UserRole role;

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
		
		if (user != null){
		
			if (user.getAccessRights() == null){
				user.setAccessRights(new HashSet<UserAccessRights>());
			}
				
			if (!user.getAccessRights().contains(this)) {
				user.getAccessRights().add(this);
	        }
		}
	}


	public BusinessDomain getBusinessDomain() {
		return businessDomain;
	}


	public void setBusinessDomain(BusinessDomain businessDomain) {
		this.businessDomain = businessDomain;
	}


	public Party getParty() {
		return party;
	}


	public void setParty(Party party) {
		this.party = party;
	}


	public UserRole getRole() {
		return role;
	}


	public void setRole(UserRole role) {
		this.role = role;
	}

	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}


/*	@Override
	public String toString() {
		return "UserAccessRights [id=" + id 
				+ ", user=" + user == null ? null : user.getId()
				+ ", businessDomain=" + businessDomain == null ? null : businessDomain.getId() 
				+ ", party=" + party == null ? null : party.getId()
				+ ", role=" + role == null ? null : role.getId() + "]";
	}
	
	@Override
    public boolean equals(final Object obj) {
        if ((obj == null) || !(obj instanceof UserAccessRights)) {
            return false;
        }
        
        final UserAccessRights otherUserAccessRights = (UserAccessRights) obj;
        return new EqualsBuilder().append(getUser(), otherUserAccessRights.getUser())
        		.append(getRole(), otherUserAccessRights.getRole())
        		.append(getBusinessDomain(), otherUserAccessRights.getBusinessDomain())
        		.append(getParty(), otherUserAccessRights.getParty())
        		.isEquals();
    }
	
	@Override
    public int hashCode() {
        return new HashCodeBuilder().append(getUser())
        		.append(getRole())
        		.append(getBusinessDomain())
        		.append(getParty())
        		.toHashCode();
    }
	
	public boolean equalsById(final Object obj) {
        if ((obj == null) || !(obj instanceof UserAccessRights)) {
            return false;
        }
		
        final UserAccessRights other = (UserAccessRights) obj;
        return new EqualsBuilder().append(getId(), other.getId())
        		.isEquals();
	}
	*/

}
