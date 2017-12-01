package eu.europa.ec.cipa.etrustex.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import eu.europa.ec.cipa.etrustex.types.SemaphoreType;

@Entity
@Table(name="ETR_TB_SEMAPHORE")
public class Semaphore {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name ="SEM_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Column(name = "SEM_NAME")
	@Enumerated(EnumType.STRING)
	private SemaphoreType type;
	
	@Column(name = "SEM_LOCKED")
	private Boolean locked = false;
	
	@Column(name="SEM_LAST_UPDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;

	public SemaphoreType getType() {
		return type;
	}

	public void setType(SemaphoreType type) {
		this.type = type;
	}
	
	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
