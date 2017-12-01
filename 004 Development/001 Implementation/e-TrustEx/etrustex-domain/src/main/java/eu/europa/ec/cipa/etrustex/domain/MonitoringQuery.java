/**
 * 
 */
package eu.europa.ec.cipa.etrustex.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author chiricr
 *
 */
@Entity
@Table(name="ETR_TB_MONITORING_QUERY")
public class MonitoringQuery {
	
	@Id
	@Column(name ="MON_ID", nullable = false)
	@GeneratedValue
	private Long id;
	
	@Column(name ="MON_QUERY", nullable = false)
	private String query;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	
	

}
