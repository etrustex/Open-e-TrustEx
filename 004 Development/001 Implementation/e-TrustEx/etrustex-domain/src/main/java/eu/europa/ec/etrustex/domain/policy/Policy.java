package eu.europa.ec.etrustex.domain.policy;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import eu.europa.ec.etrustex.domain.Transaction;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;

@Entity
@Table(name = "ETR_TB_POLICY")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "POL_TYPE",discriminatorType=DiscriminatorType.STRING)
public abstract class Policy implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="POL_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Embedded
	private EntityAccessInfo accessInfo;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="POL_BD_ID")
	private BusinessDomain businessDomain;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="POL_TRA_ID")
	private Transaction transaction;
	
	@Column(name = "POL_ACTIVE_FLAG")
	private Boolean activeFlag = false;
	
	@Column(name = "POL_VALUE")
	private Integer value;
	
	@Column(name = "POL_FLEXIBILITY")
	private Integer flexibility;
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public EntityAccessInfo getAccessInfo() {
		return accessInfo;
	}

	public BusinessDomain getBusinessDomain() {
		return businessDomain;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public Boolean getActiveFlag() {
		return activeFlag;
	}

	public Integer getValue() {
		return value;
	}

	public Integer getFlexibility() {
		return flexibility;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}

	public void setBusinessDomain(BusinessDomain businessDomain) {
		this.businessDomain = businessDomain;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public void setActiveFlag(Boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public void setFlexibility(Integer flexibility) {
		this.flexibility = flexibility;
	}
}