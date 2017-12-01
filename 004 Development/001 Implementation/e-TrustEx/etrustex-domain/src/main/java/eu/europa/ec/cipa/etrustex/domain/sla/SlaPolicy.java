package eu.europa.ec.cipa.etrustex.domain.sla;

import eu.europa.ec.cipa.etrustex.domain.Transaction;
import eu.europa.ec.cipa.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.cipa.etrustex.types.FrequencyType;
import eu.europa.ec.cipa.etrustex.types.SlaType;

import javax.persistence.*;

@Entity
@Table(name="ETR_TB_SLA_POLICY")
public class SlaPolicy {

	
	@Override
	public String toString() {
		return "SLA Policy [id=" + id +  "]";
	}

	@Id
	@Column(name ="SLA_ID", nullable = false)
	@GeneratedValue
	private Long id;

	@Embedded
	private EntityAccessInfo accessInfo;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="SLA_BD_ID")
	private BusinessDomain businessDomain;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="SLA_TRA_ID")
	private Transaction transaction;	
	
	@Column(name = "SLA_ACTIVE_FL")
	private Boolean activeFlag;
	
	@Column(name = "SLA_VALUE")
	private Integer value;
	
	@Column(name = "SLA_FLEXIBILITY")
	private Integer flexibility;
	
	@Column(name = "SLA_TYPE")
	@Enumerated(EnumType.STRING)
	private SlaType type;
	
	@Column(name = "SLA_FREQUENCY")
	@Enumerated(EnumType.STRING)
	private FrequencyType frequency;
	
	public SlaPolicy(){
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}
	
	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Boolean getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getFlexibility() {
		return flexibility;
	}

	public void setFlexibility(Integer flexibility) {
		this.flexibility = flexibility;
	}

	public SlaType getType() {
		return type;
	}

	public void setType(SlaType type) {
		this.type = type;
	}

	public FrequencyType getFrequency() {
		return frequency;
	}

	public void setFrequency(FrequencyType frequency) {
		this.frequency = frequency;
	}

	public BusinessDomain getBusinessDomain() {
		return businessDomain;
	}

	public void setBusinessDomain(BusinessDomain businessDomain) {
		this.businessDomain = businessDomain;
	}

		
}
