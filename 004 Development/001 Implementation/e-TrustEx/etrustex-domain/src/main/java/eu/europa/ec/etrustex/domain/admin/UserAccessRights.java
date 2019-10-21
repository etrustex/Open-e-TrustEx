package eu.europa.ec.etrustex.domain.admin;

import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;

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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessInfo == null) ? 0 : accessInfo.hashCode());
		result = prime * result + ((businessDomain == null) ? 0 : businessDomain.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((party == null) ? 0 : party.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAccessRights other = (UserAccessRights) obj;
		if (accessInfo == null) {
			if (other.accessInfo != null)
				return false;
		} else if (!accessInfo.equals(other.accessInfo))
			return false;
		if (businessDomain == null) {
			if (other.businessDomain != null)
				return false;
		} else if (!businessDomain.equals(other.businessDomain))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (party == null) {
			if (other.party != null)
				return false;
		} else if (!party.equals(other.party))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}




}
