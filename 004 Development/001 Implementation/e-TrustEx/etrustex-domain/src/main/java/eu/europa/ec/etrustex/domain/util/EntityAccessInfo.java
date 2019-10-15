package eu.europa.ec.etrustex.domain.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class EntityAccessInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="CRE_ID", updatable = false)
	private String creationId;
	
	@Column(name="MOD_ID")
	private String modificationId;
	
	@Column(name = "CRE_DT", nullable = true, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate = Calendar.getInstance().getTime();
	
	@Column(name = "MOD_DT", nullable = true, insertable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date modificationDate = Calendar.getInstance().getTime();
	
	public String getCreationId() {
		return creationId;
	}
	public void setCreationId(String creationId) {
		this.creationId = creationId;
	}
	public String getModificationId() {
		return modificationId;
	}
	public void setModificationId(String modificationId) {
		this.modificationId = modificationId;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getModificationDate() {
		return modificationDate;
	}
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((creationId == null) ? 0 : creationId.hashCode());
		result = prime * result + ((modificationDate == null) ? 0 : modificationDate.hashCode());
		result = prime * result + ((modificationId == null) ? 0 : modificationId.hashCode());
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
		EntityAccessInfo other = (EntityAccessInfo) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (creationId == null) {
			if (other.creationId != null)
				return false;
		} else if (!creationId.equals(other.creationId))
			return false;
		if (modificationDate == null) {
			if (other.modificationDate != null)
				return false;
		} else if (!modificationDate.equals(other.modificationDate))
			return false;
		if (modificationId == null) {
			if (other.modificationId != null)
				return false;
		} else if (!modificationId.equals(other.modificationId))
			return false;
		return true;
	}
	
	
}
