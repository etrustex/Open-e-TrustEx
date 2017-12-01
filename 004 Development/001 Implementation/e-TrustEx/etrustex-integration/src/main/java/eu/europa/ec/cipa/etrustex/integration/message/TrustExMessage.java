package eu.europa.ec.cipa.etrustex.integration.message;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eu.europa.ec.cipa.etrustex.integration.exception.BusinessException;

public class TrustExMessage<K> implements Serializable{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private TrustExMessageHeader header;
	private K payload;
	private Set<TrustExMessageBinary> binaries = new HashSet<TrustExMessageBinary>();
	private Map<String,String> additionalInfo;
	
	public Set<TrustExMessageBinary> getBinaries() {
		return binaries;
	}
	public void setBinaries(Set<TrustExMessageBinary> binaries) {
		this.binaries = binaries;
	}
	public void addBinary(TrustExMessageBinary binary) {
		this.binaries.add(binary);
	}

	public TrustExMessage(K payload){
		this.payload = payload;
	}
	public TrustExMessageHeader getHeader() {
		return header;
	}

	public void setHeader(TrustExMessageHeader header) {
		this.header = header;
	}

	public K getPayload() {
		return payload;
	}

	public void setPayload(K payload) {
		this.payload = payload;
	}
	public Map<String, String> getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(Map<String, String> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	
}
