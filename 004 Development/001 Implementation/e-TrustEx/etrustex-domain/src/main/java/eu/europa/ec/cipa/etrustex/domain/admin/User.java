package eu.europa.ec.cipa.etrustex.domain.admin;

import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="CPA_TB_USER")
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name ="CUS_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Embedded
	private EntityAccessInfo accessInfo;
	
	@Column(name ="CUS_NAME", nullable = false, unique=true)
	private String name;
	
	@Column(name ="CUS_PASSW")
	private String password;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL , fetch = FetchType.EAGER, orphanRemoval=true)
	private Set<UserAccessRights> accessRights= new HashSet<UserAccessRights>();
	

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public Set<UserAccessRights> getAccessRights() {
		return accessRights;
	}

	public void setAccessRights(Set<UserAccessRights> accessRights) {
		this.accessRights = accessRights;
	}
	
	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}


	/**
	 * Adds new access rights to the user. It is used for keeping relationships consistency.  
	 * @param accessRights
	 */
    public void addAccessRights(UserAccessRights accessRights) {
    	if (this.accessRights.contains(accessRights)){
    		return;
    	}
    	
        this.accessRights.add(accessRights);
        if (accessRights.getUser() != this) {
        	accessRights.setUser(this);
        }
    }
    
    /**
	 * Removes access rights from the party. It is used for keeping relationships consistency.  
	 * @param accessRights
	 */
    public void removeAccessRights(UserAccessRights accessRights) {
    	if (!this.accessRights.contains(accessRights)){
    		return;
    	}
    	
        this.accessRights.remove(accessRights);
        accessRights.setUser(null);
    }
    
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", numberOfAccessRights="
				+ ((accessRights != null && !accessRights.isEmpty()) ? accessRights.size() : 0) + "]";
	}


	@Override
    public boolean equals(final Object obj) {
        if ((obj == null) || !(obj instanceof User)) {
            return false;
        }
        
        final User otherUser = (User) obj;
        return new EqualsBuilder().append(getName(), otherUser.getName())
        		.isEquals();
    }
	
	@Override
    public int hashCode() {
        return new HashCodeBuilder().append(getName())
        		.toHashCode();
    }
	
	
	
	

}
