package eu.europa.ec.cipa.etrustex.domain;

import eu.europa.ec.cipa.etrustex.domain.util.EntityAccessInfo;

import javax.persistence.*;
import java.io.InputStream;
import java.io.Serializable;


@Entity
@Table(name="ETR_TB_MESSAGE_BINARY")
public class MessageBinary implements Serializable {
	
	
	@Id
	@Column(name ="MSG_BIN_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Column(name="MSG_BIN_MIME")
	private String mimeCode;
	
	@Column(name="MSG_BIN_TYPE")
	private String binaryType;
	
	@Column(name="MSG_BIN_FILE_ID", unique=true, nullable=true)
	private String fileId;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "MSG_BIN_MSG_ID")
	private Message message;
	
	@Lob
	@Column(name="MSG_BIN_FILE", nullable=true)
	@Basic(fetch=FetchType.LAZY)
	private byte[] binary;	
	
	@Column(name="MSG_BIN_SIZE",nullable=true)
	private Long size;
	
	@Column(name="IV",nullable=true)
	private byte[] iv;
		
	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	@Transient
	private InputStream binaryIs;
	
	@Embedded
	private EntityAccessInfo accessInfo;
	
	public EntityAccessInfo getAccessInfo() {
        return accessInfo != null ? accessInfo : new EntityAccessInfo();
	}

	public void setAccessInfo(EntityAccessInfo accessInfo) {
		this.accessInfo = accessInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMimeCode() {
		return mimeCode;
	}

	public void setMimeCode(String mimeCode) {
		this.mimeCode = mimeCode;
	}


	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public byte[] getBinary() {
		return binary;
	}

	public void setBinary(byte[] binary) {
		this.binary = binary;
	}
	public String getBinaryType() {
		return binaryType;
	}

	public void setBinaryType(String binaryType) {
		this.binaryType = binaryType;
	}
	public InputStream getBinaryIs() {
		return binaryIs;
	}

	public void setBinaryIs(InputStream binaryIs) {
		this.binaryIs = binaryIs;
	}
	
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	

	public byte[] getIv() {
		return iv;
	}

	public void setIv(byte[] iv) {
		this.iv = iv;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((binaryType == null) ? 0 : binaryType.hashCode());
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((mimeCode == null) ? 0 : mimeCode.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
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
		MessageBinary other = (MessageBinary) obj;
		if (binaryType == null) {
			if (other.binaryType != null)
				return false;
		} else if (!binaryType.equals(other.binaryType))
			return false;
		if (fileId == null) {
			if (other.fileId != null)
				return false;
		} else if (!fileId.equals(other.fileId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mimeCode == null) {
			if (other.mimeCode != null)
				return false;
		} else if (!mimeCode.equals(other.mimeCode))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		return true;
	}
	
	

}
