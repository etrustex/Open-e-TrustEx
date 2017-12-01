package eu.europa.ec.cipa.etrustex.domain.query;

import eu.europa.ec.cipa.etrustex.domain.Message;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ETR_TB_QUERY_RESULT")
public class QueryResult implements Serializable {
	
	@Id
	@Column(name = "QR_ID", nullable = false)
	@GeneratedValue
	private Long id;

	@Column(name ="QR_KEY", nullable = false)
	private String key;
	
	@Column(name ="QR_VALUE", nullable = false)
	private String value;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "QR_MSG_ID")
	private Message message;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
}
