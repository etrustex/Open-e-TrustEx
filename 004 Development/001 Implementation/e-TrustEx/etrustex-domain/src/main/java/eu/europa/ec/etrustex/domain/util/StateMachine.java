package eu.europa.ec.etrustex.domain.util;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "ETR_TB_STATE_MACHINE")
public class StateMachine implements Serializable {

	private static final long serialVersionUID = 1248402442777480692L;

	@Id
	@Column(name = "SM_ID", nullable = false)
	@GeneratedValue
	private Long id;

	@Column(name = "SM_DEFINITION")
	@Lob
	private String definition;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}
}
