package eu.europa.ec.etrustex.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import eu.europa.ec.etrustex.domain.util.EntityAccessInfo;

@Entity
@Table(name="ETR_TB_HR_TEMPLATE" )
public class HumanReadableTemplate implements Serializable {

	private static final long serialVersionUID = 2495671424702725506L;

	
	@Id
	@Column(name ="HRT_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Embedded
	private EntityAccessInfo accessInfo;
	
	@Column(name = "HRT_NAME", nullable = false)
	private String name;
	
	@Column(name = "HRT_XSLT", nullable = false)
	@Lob
	private String xslt;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "HRT_TRA_ID",nullable=true)
	private Transaction transaction;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "HRT_DOC_ID",nullable=true)
	private Document document;

	@Column(name="HRT_IS_DEFAULT", nullable = false)
	private Boolean isDefault;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EntityAccessInfo getAccessInfo() {
		return accessInfo;
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return xslt;
	}

	public void setValue(String value) {
		this.xslt = value;
	}

	public Transaction getTansaction() {
		return transaction;
	}

	public void setTansaction(Transaction tansaction) {
		this.transaction = tansaction;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public String toString() {
		return "HumanReadableTemplate [id=" + id + ", accessInfo=" + accessInfo + ", name=" + name + ", value=" + xslt
				+ ", tansaction=" + transaction + ", document=" + document + ", isDefault=" + isDefault + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((xslt == null) ? 0 : xslt.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HumanReadableTemplate other = (HumanReadableTemplate) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (xslt == null) {
			if (other.xslt != null)
				return false;
		} else if (!xslt.equals(other.xslt))
			return false;
		return true;
	}
	
	
}
