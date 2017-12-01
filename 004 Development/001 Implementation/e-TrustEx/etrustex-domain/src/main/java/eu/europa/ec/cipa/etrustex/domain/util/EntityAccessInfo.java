package eu.europa.ec.cipa.etrustex.domain.util;

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
	
	
}
