package eu.europa.ec.etrustex.domain.policy;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue(value = "RETENTION")
public class RetentionPolicy extends Policy implements Serializable  {

	private static final long serialVersionUID = 7162998330718976586L;
	
	
}
