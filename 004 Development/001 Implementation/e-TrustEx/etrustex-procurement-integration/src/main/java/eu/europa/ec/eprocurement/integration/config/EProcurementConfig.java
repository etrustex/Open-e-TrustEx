package eu.europa.ec.eprocurement.integration.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Import;

@Configurable
@Import({EprocurementSoapEndpointConfig.class})
public class EProcurementConfig {

}
