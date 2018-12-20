package eu.europa.ec.etrustex.dao.dto;

public class SlaValidationDTO {
	
	private Long slaVolume;
	private long expendedVolume;
	private Long slaMaxBinarySize;
	
	public SlaValidationDTO(Long slaVolume, long expendedVolume, Long slaMaxBinarySize) {
		this.slaVolume = slaVolume;
		this.expendedVolume = expendedVolume;
		this.slaMaxBinarySize = slaMaxBinarySize;
	}
	
	public Long getSlaVolume() {
		return slaVolume;
	}
	public void setSlaVolume(Long slaVolume) {
		this.slaVolume = slaVolume;
	}
	public long getExpendedVolume() {
		return expendedVolume;
	}
	public void setExpendedVolume(long expendedVolume) {
		this.expendedVolume = expendedVolume;
	}

	public Long getSlaMaxBinarySize() {
		return slaMaxBinarySize;
	}

	public void setSlaMaxBinarySize(Long slaMaxBinarySize) {
		this.slaMaxBinarySize = slaMaxBinarySize;
	}

	public boolean isSlaVolumeExceeded() {
		return slaVolume != null ? (expendedVolume > slaVolume) : false;
	}
	

}
