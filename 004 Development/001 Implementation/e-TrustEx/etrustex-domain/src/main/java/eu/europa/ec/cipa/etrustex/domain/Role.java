package eu.europa.ec.cipa.etrustex.domain;

import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name= "ETR_TB_ROLE")
public class Role implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name ="ROL_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Column(name="ROL_NAME",nullable=false,unique=true)
	private String name;
	
	@Column(name="ROL_CODE",nullable=false,unique=true)
	private String code;
	
	@Column(name="ROL_IS_TECHNICAL")
	private Boolean technicalFlag;
	
	@Column(name="ROL_IS_BIDIRECTIONAL")
	private Boolean bidirectionalFlag;

	@Embedded
	private EntityAccessInfo accessInfo;
	
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

	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getTechnicalFlag() {
		return technicalFlag;
	}

	public void setTechnicalFlag(Boolean technicalFlag) {
		this.technicalFlag = technicalFlag;
	}

	public Boolean getBidirectionalFlag() {
		return bidirectionalFlag;
	}

	public void setBidirectionalFlag(Boolean bidirectionalFlag) {
		this.bidirectionalFlag = bidirectionalFlag;
	}
	
	@Override
	public String toString() {
		return "Role [id=" + id + ", code=" + code + ", name=" + name + ", technicalFlag=" + technicalFlag + "]";
	}
	
	@Override
    public boolean equals(final Object obj) {
        
		if ((obj == null) || !(obj instanceof Role)) {
            return false;
        }
        
        final Role otherRole = (Role) obj;
        return new EqualsBuilder().append(getName(), otherRole.getName())
        		.append(getCode(), otherRole.getCode())
        		.isEquals();
    }
	
	@Override
    public int hashCode() {
        return new HashCodeBuilder().append(getName())
        		.append(getCode())
        		.toHashCode();
    }
	
	
	public boolean equalsById(final Object obj) {
        if ((obj == null) || !(obj instanceof Role)) {
            return false;
        }
		
        final Role other = (Role) obj;
        return new EqualsBuilder().append(getId(), other.getId())
        		.isEquals();
	}


}
