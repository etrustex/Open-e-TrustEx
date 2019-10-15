package eu.europa.ec.etrustex.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="ETR_TB_MSG_BIN_VALUE")
public class MessageBinaryValue {

	
	@Id
	@Column(name ="MSG_BIN_VAL_ID", nullable = false)
	@GeneratedValue
	private Long id;
		
	@Lob
	@Column(name="MSG_BIN_VAL_VALUE", nullable=false)
	private byte[] value;

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}
}
