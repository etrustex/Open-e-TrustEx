package eu.europa.ec.cipa.etrustex.domain.query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import eu.europa.ec.cipa.etrustex.domain.Document;


@Entity
@Table(name = "ETR_TB_EXTRACTION_CONFIG")
public class DataExtractionConfig {
	
	@Id
	@Column(name = "DE_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Column(name ="DE_KEY", nullable = false)
	private String key;
	
	@Column(name ="DE_XPATH", nullable = false)
	private String xpath;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TRA_DOC_ID")
	private Document document;
	
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

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

}
