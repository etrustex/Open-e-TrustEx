/**
 * 
 */
package eu.europa.ec.etrustex.domain.routing;

import eu.europa.ec.etrustex.domain.Party;
import eu.europa.ec.etrustex.domain.Profile;
import eu.europa.ec.etrustex.domain.admin.BusinessDomain;
import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;
import eu.europa.ec.etrustex.types.EventNotificationType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author guerrpa
 *
 */
@Entity
@Table(name = "CPA_TB_EVT_NOTIF")
public class EventNotification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name ="EVT_ID", nullable = false)
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="EVT_PRO_ID")
	private Profile profile;

	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="EVT_CBD_ID")
	private BusinessDomain businessDomain;

	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name ="EVT_PTY_ID")
	private Party party;

    @Column(name = "EVT_TYPE")
    @Enumerated(EnumType.STRING)
    private EventNotificationType eventType;

	@Embedded
	private EntityAccessInfo accessInfo = new EntityAccessInfo();

	public EventNotification() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
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

	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}


    public EventNotificationType getEventType() {
        return eventType;
    }

    public void setEventType(EventNotificationType eventType) {
        this.eventType = eventType;
    }
}
