package eu.europa.ec.etrustex.domain.routing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


@Entity           
@Table(name="ETR_TB_ENDPOINT_JMS")
@PrimaryKeyJoinColumn(name="ID", referencedColumnName="EDP_ID")
public class JMSEndpoint extends Endpoint{
	
//	@Id
//	@Column(name ="EDP_JMS_ID", nullable = false)
//	@GeneratedValue
//	private Long id;
//	
	@Column(name ="EDP_JMS_PROV_URL", nullable = true)
	private String providerUrl;
	@Column(name ="EDP_JMS_INIT_CONT_FACT", nullable = true)
	private String initalContextFactory;
	@Column(name ="EDP_JMS_DEST_JNDI_NM", nullable = false)
	private String destinationJndiName;
	@Column(name ="EDP_JMS_CFACT_NM", nullable = true)
	private String connectionFactoryJndiName;
	@Column(name ="EDP_JMS_MESS_CON_CLASS", nullable = true)
	private String messageConverterClass;
	@Column(name ="EDP_REPLY_TO_FLAG", nullable = false)
	private Boolean isSupportingReplyTo;
	
	
	
	public Boolean getIsSupportingReplyTo() {
		return isSupportingReplyTo;
	}
	public void setIsSupportingReplyTo(Boolean isSupportingReplyTo) {
		this.isSupportingReplyTo = isSupportingReplyTo;
	}
	public String getMessageConverterClass() {
		return messageConverterClass;
	}
	public void setMessageConverterClass(String messageConverterClass) {
		this.messageConverterClass = messageConverterClass;
	}
	public String getProviderUrl() {
		return providerUrl;
	}
	public void setProviderUrl(String providerUrl) {
		this.providerUrl = providerUrl;
	}
	public String getInitalContextFactory() {
		return initalContextFactory;
	}
	public void setInitalContextFactory(String initalContextFactory) {
		this.initalContextFactory = initalContextFactory;
	}
	public String getDestinationJndiName() {
		return destinationJndiName;
	}
	public void setDestinationJndiName(String destinationJndiName) {
		this.destinationJndiName = destinationJndiName;
	}
	public String getConnectionFactoryJndiName() {
		return connectionFactoryJndiName;
	}
	public void setConnectionFactoryJndiName(String connectionFactoryJndiName) {
		this.connectionFactoryJndiName = connectionFactoryJndiName;
	}
	

}
