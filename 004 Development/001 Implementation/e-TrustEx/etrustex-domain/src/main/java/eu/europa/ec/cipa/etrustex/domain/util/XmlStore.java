package eu.europa.ec.cipa.etrustex.domain.util;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "ETR_TB_XML_STORE")
public class XmlStore {

	@Id
	@Column(name = "XS_ID", nullable = false)
	@GeneratedValue
	private Long id;

	@Column(name = "XS_KEY", nullable = false, unique = true)
	private String key;

	@Column(name = "XS_VALUE")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String value;

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

}
