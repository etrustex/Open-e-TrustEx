package eu.europa.ec.etrustex.domain.routing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity           
@Table(name="ETR_TB_ENDPOINT_WS")
@PrimaryKeyJoinColumn(name="ID", referencedColumnName="EDP_ID")
public class WSEndpoint extends Endpoint{

	@Column(name ="EDP_WS_URL", nullable = false)
	private String  wsEndpointURL;
	@Column(name ="EDP_WS_SING_FL", nullable = false)
	private Boolean signCall;
	@Column(name ="EDP_WS_REQUIRE_SIGNED_RESP_FL", nullable = false)
	private Boolean checkResponseSignature;
	
	
	public String getWsEndpointURL() {
		return wsEndpointURL;
	}
	public void setWsEndpointURL(String wsEndpointURL) {
		this.wsEndpointURL = wsEndpointURL; 
	}
	public Boolean getSignCall() {
		return signCall;
	}
	public void setSignCall(Boolean signCall) {
		this.signCall = signCall;
	}
	public Boolean getCheckResponseSignature() {
		return checkResponseSignature;
	}
	public void setCheckResponseSignature(Boolean checkResponseSignature) {
		this.checkResponseSignature = checkResponseSignature;
	}

}
