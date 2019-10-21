package eu.europa.ec.etrustex.domain.policy;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import eu.europa.ec.etrustex.types.FrequencyType;

@Entity
@DiscriminatorValue(value = "SLA_VOLUME")
public class VolumeSlaPolicy extends Policy implements Serializable  {

	private static final long serialVersionUID = 7162998330718976586L;
	
	@Column(name = "POL_FREQUENCY")
	@Enumerated(EnumType.STRING)
	private FrequencyType frequency;

	public FrequencyType getFrequency() {
		return frequency;
	}

	public void setFrequency(FrequencyType frequency) {
		this.frequency = frequency;
	}
}
