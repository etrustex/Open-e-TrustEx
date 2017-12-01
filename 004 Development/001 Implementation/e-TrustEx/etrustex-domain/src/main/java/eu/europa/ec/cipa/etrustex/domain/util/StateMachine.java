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
@Table(name = "ETR_TB_STATE_MACHINE")
public class StateMachine {

	@Id
	@Column(name = "SM_ID", nullable = false)
	@GeneratedValue
	private Long id;

	@Column(name = "SM_DEFINITION")
	@Lob
	@Basic(fetch = FetchType.LAZY)
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
}
