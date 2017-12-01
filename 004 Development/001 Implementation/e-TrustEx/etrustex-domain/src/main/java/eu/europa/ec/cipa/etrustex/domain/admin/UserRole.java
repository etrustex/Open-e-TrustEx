package eu.europa.ec.cipa.etrustex.domain.admin;

import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="CPA_TB_USER_ROLE")
public class UserRole implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="RDL_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Embedded
	private EntityAccessInfo accessInfo;

	@Column(name ="RDL_CODE", nullable = false,unique=true)
	private String code;
	
	@Column(name ="RDL_DESC", nullable = false,unique=true)
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}

	
	@Override
	public String toString() {
		return "UserRole [id=" + id + ", code=" + code + ", description=" + description + "]";
	}


	@Override
    public boolean equals(final Object obj) {
        if ((obj == null) || !(obj instanceof UserRole)) {
            return false;
        }
        
        final UserRole otherUserRole = (UserRole) obj;
        return new EqualsBuilder().append(getCode(), otherUserRole.getCode())
        		.isEquals();
    }
	
	@Override
    public int hashCode() {
        return new HashCodeBuilder().append(getCode())
        		.toHashCode();
    }
	
	public boolean equalsById(final Object obj) {
        if ((obj == null) || !(obj instanceof UserRole)) {
            return false;
        }
		
        final UserRole other = (UserRole) obj;
        return new EqualsBuilder().append(getId(), other.getId())
        		.isEquals();
	}

}
