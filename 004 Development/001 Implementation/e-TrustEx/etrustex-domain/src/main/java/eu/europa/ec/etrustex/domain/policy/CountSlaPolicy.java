package eu.europa.ec.etrustex.domain.policy;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "SLA_COUNT")
public class CountSlaPolicy extends Policy implements Serializable  {

	private static final long serialVersionUID = 7162998330718976586L;
	
	
}

