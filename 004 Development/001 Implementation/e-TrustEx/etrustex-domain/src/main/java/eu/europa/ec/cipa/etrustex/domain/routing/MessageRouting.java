    /**
 * 
 */
package eu.europa.ec.cipa.etrustex.domain.routing;

import eu.europa.ec.cipa.etrustex.domain.Message;
import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;

import javax.persistence.*;
import java.util.Date;

/**
 * @author chiricr
 *
 */
@Entity
@Table(name = "ETR_TB_MESSAGE_ROUTING")
public class MessageRouting {
	
	@Id
	@Column(name ="MR_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="MR_ENDPOINT_ID")
	private Endpoint endpoint;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name="MR_MESSAGE_ID")
	private Message message;
	
	@Column(name ="MR_PROCESSED_FLAG")
	private Boolean processed;
	
	@Embedded
	private EntityAccessInfo accessInfo = new EntityAccessInfo();
	
	public MessageRouting() {}
	
	public MessageRouting(Endpoint endpoint, Message message) {
		this.endpoint = endpoint;
		this.message = message;
		this.processed = Boolean.FALSE;
		accessInfo.setCreationDate(new Date());
		accessInfo.setCreationId("TRUSTEX");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Boolean getProcessed() {
		return processed;
	}

	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}

	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}
	
	
}
