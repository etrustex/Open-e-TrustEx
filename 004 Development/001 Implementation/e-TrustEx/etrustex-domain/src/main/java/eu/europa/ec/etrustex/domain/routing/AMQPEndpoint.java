package eu.europa.ec.etrustex.domain.routing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity           
@Table(name="ETR_TB_ENDPOINT_AMQP")
@PrimaryKeyJoinColumn(name="ID", referencedColumnName="EDP_ID")
public class AMQPEndpoint extends Endpoint {
	
	@Column(name ="EDP_AMQP_PROV_URL", nullable = true)
	private String providerUrl;

	public String getProviderUrl() {
		return providerUrl;
	}

	public void setProviderUrl(String providerUrl) {
		this.providerUrl = providerUrl;
	}
	
	

}
