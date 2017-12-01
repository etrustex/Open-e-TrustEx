package eu.europa.ec.cipa.etrustex.domain;

import javax.persistence.*;

@Entity
@DiscriminatorValue("JMS")
public class JMSCredentials extends Credentials {
}
